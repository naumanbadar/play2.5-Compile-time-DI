package dao

import anorm._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}
import play.api.db.{Database, Databases}

import scala.io.Source

/**
  * Created by Nauman Badar on 16/10/10.
  */
class DAOTest extends FlatSpec with Matchers with BeforeAndAfterAll {


  behavior of "DAOTest"

  //test dao object
  object dao extends DAO {
    lazy val dbServer = sys.env("DATABASE_SERVER")
    lazy val dbName = sys.env("DATABASE_NAME")
    lazy val dbLogin = sys.env("DATABASE_LOGIN")
    lazy val dbPassword = sys.env("DATABASE_PASSWORD")

    override val database: Database =
      Databases(
        driver = "net.sourceforge.jtds.jdbc.Driver",
        url = s"jdbc:jtds:sqlserver://$dbServer/$dbName",
        name = s"$dbServer/$dbName",
        config = Map(
          "username" -> dbLogin,
          "password" -> dbPassword,
          "hikaricp.connectionTestQuery" -> "SELECT 'TRUE'"
        )
      )
  }

  //resurrect the database
  override protected def beforeAll(): Unit = {
    dao.database.withConnection {
      implicit con =>
        SQL(Source.fromFile(getClass.getResource("/sql/bootstrapTestDb.sql").getPath).mkString).execute()

    }
  }

}
