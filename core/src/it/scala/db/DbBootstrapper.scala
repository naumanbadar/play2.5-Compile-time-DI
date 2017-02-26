package db

import anorm._
import com.typesafe.config.ConfigFactory
import db.gen.Generator

import scala.io.Source
import play.api.db.{Database, Databases}


/**
  * Created by Nauman Badar on Wed, Jan 25, 2017.
  */
object DbBootstrapper {


  private lazy val config = ConfigFactory.load().getConfig("database")
  private lazy val dbServer: String = config.getString("server")
  private lazy val dbLogin: String = config.getString("login")
  private lazy val dbPassword: String = config.getString("password")


  private def loanMasterDb(f: Database => Unit): Unit = {
    val db = Databases(
        driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
        url = s"jdbc:sqlserver://$dbServer;databaseName=master",
        name = s"$dbServer/master",
        config = Map(
          "username" -> dbLogin,
          "password" -> dbPassword,
          "hikaricp.connectionTestQuery" -> "SELECT 'TRUE'"
        )
      )
    try {
      f(db)
    } finally {
      db.shutdown()
    }
  }

  def resurrectDb(): Unit = {
    loanMasterDb {
      db =>
        db.withConnection {
      implicit con =>
        Source.fromFile(getClass.getResource("/sql/DbSchema.sql").getPath).mkString.split(";").toList.map(_.trim).foreach(SQL(_).execute())
        }
    }

  }

  def populateRequests(): Unit = {
    loanMasterDb {
      db =>
        db.withConnection {
          implicit con =>

//            Generator.insertRequest()
        }
    }
  }

  def main(args: Array[String]): Unit = {
    resurrectDb()
  }

}
