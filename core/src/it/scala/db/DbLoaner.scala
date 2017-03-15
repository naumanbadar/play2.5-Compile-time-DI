package db

import com.typesafe.config.ConfigFactory
import play.api.db.{Database, Databases}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by Nauman Badar on 2017-03-01
  */
trait DbLoaner {

  lazy val config = ConfigFactory.load().getConfig("database")
  lazy val dbServer: String = config.getString("server")
  lazy val dbName: String = config.getString("dbname")
  lazy val dbLogin: String = config.getString("login")
  lazy val dbPassword: String = config.getString("password")


  lazy val database: Database = Databases(
    driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    url = s"jdbc:sqlserver://$dbServer;databaseName=$dbName",
    name = s"$dbServer/$dbName",
    config = Map(
      "username" -> dbLogin,
      "password" -> dbPassword,
      "hikaricp.connectionTestQuery" -> "SELECT 'TRUE'"
    )
  )

  def loan[T](f: Database => T): T = {
    try {

      f(database)
    }
    finally {
      database.shutdown()
    }
  }

  //for functions which return a Future[T], it make sures that db is not closed until the future has completed
  def loanWithFuture[T](f: Database => Future[T])(implicit executionContext: ExecutionContext): Future[T] = {
    f(database) andThen {
      case _ => database.shutdown()
    }
  }
}
