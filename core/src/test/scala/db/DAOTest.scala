package db

import java.time.LocalDateTime

import org.scalatest.{FlatSpec, Matchers}
import play.api.db.Database

/**
  * Created by Nauman Badar on 2017-02-28
  */
class DAOTest extends FlatSpec with Matchers {

  behavior of "DAOTest"

  object dao extends DAO {
    override def database: Database = ???
  }

  it should "parseDateTime from data read from database" in {
    dao.parseDateTime("2016-06-15 08:06:28") shouldBe LocalDateTime.of(2016, 6, 15, 8, 6, 28)
  }


  it should "format LocalDateTime to database format" in {
    val localDateTime = LocalDateTime.of(2016, 1, 1, 10, 10, 10)
    dao.dateToString(localDateTime) should be
    "2016-01-01 10:10:10"
  }


}
