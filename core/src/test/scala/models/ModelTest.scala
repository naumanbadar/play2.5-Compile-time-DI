package models

import org.scalatest.{FlatSpec, Matchers}

/**
  * Created by Nauman Badar on 2017-02-26
  */
class ModelTest extends FlatSpec with Matchers {

  behavior of "Model"

  it should "get back name" in {
    Model("Hello").name should be("Hello")
  }

}
