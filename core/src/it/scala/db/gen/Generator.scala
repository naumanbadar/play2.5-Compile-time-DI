package db.gen

import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import anorm._
import com.typesafe.scalalogging.LazyLogging

import scala.util.Random

/**
  * Created by Nauman Badar on Wed, Feb 01, 2017.
  */
object Generator extends LazyLogging {

  private val mesgTypes = Vector("Email", "SMS")

  private val statusTypes = Vector("Sent", "Failed", "Delivered")
  ('a' to 'z').toList

  def getRandomString(length: Int): String = {
    val r = Random.nextInt(length) + 1
    Random.alphanumeric.take(r).mkString
  }

  private def getRandomValueFromCollection[T](seq: Seq[T]): T = {
    seq(Random.nextInt(seq.size))
  }

  private def getRandomDate: LocalDateTime = {
    val _5days = Random.nextInt(5 * 24 * 60 * 60)
    LocalDateTime.now.minusSeconds(_5days)
  }


  private def messageType: String = getRandomValueFromCollection(mesgTypes)

  private def statusType = getRandomValueFromCollection(statusTypes)

  private def recipient = getRandomString(50)

  private def subject = getRandomString(200)

  private def body = getRandomString(1000)

  private def callback = "http://" + getRandomString(50) + ".com"

  private def date = getRandomDate

  private def isNotified = Random.nextBoolean()



  /*
    private def insertStatus()(implicit connection: Connection): Unit = {
      """INSERT INTO MessageDispatcher.dbo.Status (Id, Status, Date_Time, Is_Notified) VALUES
        |  (14, 'Sent', '2017-02-01 16:00:15', 1);"""
    }
  */


}
