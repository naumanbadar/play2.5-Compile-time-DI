package restclient

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.{AsyncFlatSpec, BeforeAndAfterAll, FlatSpec, Matchers}
import play.api.libs.ws.ahc.AhcWSClient

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Nauman Badar on 2017-02-28
  */
class RestClientTest extends AsyncFlatSpec with Matchers with BeforeAndAfterAll {

  behavior of "RestClient"

  it should "send GET request" in {

    WsCLoaner.loan {
      wsc =>
        val rc = RestClient(
          wsc,
          "https://httpbin.org/get",
          "hdtrpt",
          "admin"
        )
        rc.getResponse
    }.map {
      res => res.status shouldBe (200)
    }
  }

}
