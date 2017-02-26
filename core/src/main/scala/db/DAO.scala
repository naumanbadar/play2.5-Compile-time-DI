package db

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import com.typesafe.scalalogging.LazyLogging
import models.Model
import play.api.db.Database

import anorm._

/**
  * Created by Nauman Badar on 2017-02-26
  */
trait DAO extends LazyLogging {

  def database: Database

  //for insertion of current date time
  def now(): String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd HH:mm:ss"))

  def insertModel(model: Model): Int = {
    database.withConnection {
      implicit con =>
        val sqlStatement = SQL(
          """
            INSERT INTO MyDatabase.dbo.MyModels (Name) VALUES ({name});
          """
        ).on(
          "name" -> model.name
        )

        sqlStatement.executeInsert(SqlParser.scalar[Int].single)
    }
  }

}

object DAO {
  def apply(db: Database): DAO = new DAO() {
    override lazy val database: Database = db
  }
}
