package restclient

import org.scalatest.{FlatSpec, Matchers}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by Nauman Badar on 2017-02-28
  */
class RestClientTest extends FlatSpec with Matchers {

  behavior of "RestClient"

  it should "send GET request" in new WsCLoaner {

    loan {
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
