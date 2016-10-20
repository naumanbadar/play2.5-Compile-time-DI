package dao

import anorm._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source

/**
  * Created by Nauman Badar on 16/10/10.
  */
class DAOTest extends FlatSpec with Matchers with BeforeAndAfterAll {

  //test dao
  object dao extends DAO {
    override lazy val dbServer = sys.env("DATABASE_SERVER")
    override lazy val dbName = sys.env("DATABASE_NAME")
    override lazy val dbLogin = sys.env("DATABASE_LOGIN")
    override lazy val dbPassword = sys.env("DATABASE_PASSWORD")
  }


  //resurrect the database
  override protected def beforeAll(): Unit = {
    dao.database.withConnection {
      implicit con =>
        SQL(Source.fromFile(getClass.getResource("/sql/testDbEvolution.sql").getPath).mkString).execute()

    }
  }

}
