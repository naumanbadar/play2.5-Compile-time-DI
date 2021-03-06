import akka.actor.{ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import org.asynchttpclient.AsyncHttpClientConfig
import play.api.ApplicationLoader.Context
import play.api.db.{Database, Databases}
import play.api.libs.ws.WSConfigParser
import play.api.libs.ws.ahc.{AhcConfigBuilder, AhcWSClient, AhcWSClientConfig}
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, LoggerConfigurator}
import router.Routes
import serviceActors.MyActor

import scala.concurrent.Future

/**
  * Created by nb on 16/11/02.
  */

/*
ApplicationLoader as copied from play docs to enable compile DI. After extending trait 'ApplicationLoader'
application.conf has to set the property 'play.application.loader' to this class.
 */

class MyApplicationLoader extends ApplicationLoader {
  override def load(context: Context): Application = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment)
    }
    new MyComponents(context).application
  }
}

class MyComponents(context: Context) extends BuiltInComponentsFromContext(context) {

  //instantiate resources which need to be cleaned up on application exits e.g. akka, database, ws client etc.


  //Database
  //~~~~~~~~

  // call this function to instantiate database and pass it to your constructor. stop hook is taken care of.
  private def instantiateNewDatabase: Database = {

    lazy val appConfig = ConfigFactory.load().getConfig("database")
    lazy val dbServer: String = appConfig.getString("server")
    lazy val dbName: String = appConfig.getString("dbname")
    lazy val dbLogin: String = appConfig.getString("login")
    lazy val dbPassword: String = appConfig.getString("password")


    lazy val database = Databases(
      driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
      url = s"jdbc:sqlserver://$dbServer;databaseName=$dbName",
      name = s"$dbServer/$dbName",
      config = Map(
        "username" -> dbLogin,
        "password" -> dbPassword,
        "hikaricp.connectionTestQuery" -> "SELECT 'TRUE'"
      )
    )

    // register stop hook
    applicationLifecycle.addStopHook(() => Future.successful(database.shutdown()))

    database
  }


  //Akka
  //~~~~

  val system: ActorSystem = ActorSystem("RenameMe")
  val master = system.actorOf(Props(classOf[MyActor]), "RenameThisActor")

  // register stop hook
  applicationLifecycle.addStopHook(() => Future.successful(system.terminate()))


  //WS
  //~~

  /** Initialisation of WS Client has been taken from plays documentation
    * at https://www.playframework.com/documentation/2.5.x/ScalaWS#Directly-creating-WSClient
    * */
  val parser = new WSConfigParser(configuration, environment)
  val config = new AhcWSClientConfig(wsClientConfig = parser.parse())
  val builder = new AhcConfigBuilder(config)
  val logging = new AsyncHttpClientConfig.AdditionalChannelInitializer() {
    override def initChannel(channel: io.netty.channel.Channel): Unit = {
      channel.pipeline.addFirst("log", new io.netty.handler.logging.LoggingHandler("debug"))
    }
  }
  val ahcBuilder = builder.configure()
  ahcBuilder.setHttpAdditionalChannelInitializer(logging)
  val ahcConfig = ahcBuilder.build()
  val wsClient = new AhcWSClient(ahcConfig)

  // register stop hook
  applicationLifecycle.addStopHook(() => Future.successful(wsClient.close()))

  //Controllers
  //~~~~~~~~~~~

  lazy val assets = new controllers.Assets(httpErrorHandler)

  //all controllers need to initialised
  lazy val homeController = new controllers.HomeController

  //provide all controller objects and asset object to Routes, can check its constructor in auto-generated router in target folder
  lazy val router = new Routes(httpErrorHandler, homeController, assets)


}