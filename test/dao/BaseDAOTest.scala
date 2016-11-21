package dao

import anorm._
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

import scala.io.Source

/**
  * Created by Nauman Badar on 16/10/10.
  */
class BaseDAOTest extends FlatSpec with Matchers with BeforeAndAfterAll {


  //test dao
  val dao = new BaseDAO(BaseDAO.instantiateNewDatabase){}

  //resurrect the database
  override protected def beforeAll(): Unit = {
    dao.database.withConnection {
      implicit con =>
        SQL(Source.fromFile(getClass.getResource("/sql/bootstrapTestDb.sql").getPath).mkString).execute()

    }
  }

}
