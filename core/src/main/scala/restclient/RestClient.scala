package restclient

import com.typesafe.scalalogging.LazyLogging
import play.api.libs.ws.{WSAuthScheme, WSClient, WSResponse}

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

/**
  * Created by Nauman Badar on 2017-02-28
  */
trait RestClient extends LazyLogging {

  def wsClient: WSClient

  def serviceUrl: String

  def user: String

  def password: String

  def getResponse(implicit executionContext: ExecutionContext): Future[WSResponse] = {

    val futureResponse = wsClient.url(serviceUrl)
      .withRequestTimeout(5.seconds)
      .withAuth(user, password, WSAuthScheme.BASIC)
      .get()


    //logging status information
    futureResponse.onComplete {
      case Success(wsr: WSResponse) =>
        wsr.status match {
          case 200 => logger.debug("WS returned 200")
          case code => logger.error(s"WS returned $code")
        }
      case Failure(ex: Throwable) =>
        logger.error(s"WS request failed with exception ${ex.toString}")
        ex.printStackTrace()
    }

    futureResponse
  }

}

object RestClient {
  def apply(_wSClient: WSClient, _serviceUrl: String, _user: String, _password: String): RestClient = new RestClient {

    override lazy val user = _user

    override lazy val wsClient = _wSClient

    override lazy val password = _password

    override lazy val serviceUrl = _serviceUrl
  }
}

