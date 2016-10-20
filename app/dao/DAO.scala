package dao

import com.typesafe.scalalogging.LazyLogging
import play.api.db.{Database, Databases}

/**
  * Created by Nauman Badar on 16/10/10.
  */
trait DAO extends LazyLogging {

  // read env for conf e.g. sys.env("DATABASE_SERVER")

  def dbServer: String

  def dbName: String

  def dbLogin: String

  def dbPassword: String


  val database: Database = Databases(
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
