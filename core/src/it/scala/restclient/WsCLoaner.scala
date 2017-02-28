package restclient

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.{ExecutionContext, Future}


/**
  * Created by Nauman Badar on 2017-02-28
  */
object WsCLoaner {

  private implicit val system = ActorSystem()
  private implicit val materializer = ActorMaterializer()

  private val wsClient = AhcWSClient()

  def loan[T](f: AhcWSClient => Future[T])(implicit executionContext: ExecutionContext): Future[T] = {
    f(wsClient)
      .andThen { case _ => wsClient.close() }
      .andThen { case _ => system.terminate() }
  }

}
