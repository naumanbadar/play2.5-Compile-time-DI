import sbt._

object Dependencies {

  val playAnorm = "com.typesafe.play" %% "anorm" % "2.5.3"
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0"
  val logback = "ch.qos.logback" % "logback-classic" % "1.1.7"
  val typesafeConfig = "com.typesafe" % "config" % "1.3.1"
  val scalamock = "org.scalamock" %% "scalamock" % "3.5.0"
  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"
  val scalaTestPlay = "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1"
  val msSqlServer = "com.microsoft.sqlserver" % "mssql-jdbc" % "6.1.0.jre8"
  val playJson =  "com.typesafe.play" %% "play-json" % "2.5.12"

}