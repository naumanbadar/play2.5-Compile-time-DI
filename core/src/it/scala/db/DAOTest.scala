package db

import com.typesafe.config.ConfigFactory
import models.Model
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import play.api.db.{Database, Databases}

/**
  * Created by Nauman Badar on Wed, Jan 25, 2017.
  */
class DAOTest extends FlatSpec with Matchers with BeforeAndAfterAll {


  lazy val config = ConfigFactory.load().getConfig("database")
  lazy val dbServer: String = config.getString("server")
  lazy val dbName: String = config.getString("dbname")
  lazy val dbLogin: String = config.getString("login")
  lazy val dbPassword: String = config.getString("password")


  lazy val db: Database = Databases(
    driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver",
    url = s"jdbc:sqlserver://$dbServer;databaseName=$dbName",
    name = s"$dbServer/$dbName",
    config = Map(
      "username" -> dbLogin,
      "password" -> dbPassword,
      "hikaricp.connectionTestQuery" -> "SELECT 'TRUE'"
    )
  )

  object dao extends DAO {
    override def database: Database = db
  }

  //  resurrect database
  override protected def beforeAll(): Unit = {
    DbBootstrapper.resurrectDb()
  }

  behavior of "DAO"

  it should "persist model and get back an id" in {
    noException should be thrownBy dao.insertModel(Model("Hello Model"))
  }
}