package dao

import com.typesafe.scalalogging.LazyLogging
import play.api.db.{Database, Databases}

/**
  * Created by Nauman Badar on 16/10/10.
  */
abstract class BaseDAO(val database: Database) extends LazyLogging {}

object BaseDAO {


  /**separate instantiation of Database needed for registering to stop hooks of Play */
  def instantiateNewDatabase: Database = {
    lazy val dbServer = sys.env("DATABASE_SERVER")
    lazy val dbName = sys.env("DATABASE_NAME")
    lazy val dbLogin = sys.env("DATABASE_LOGIN")
    lazy val dbPassword = sys.env("DATABASE_PASSWORD")

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
}