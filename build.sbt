import Dependencies._

name := """Play 2.5 startup template with compile time Dependency Injection"""

organization := "com.somecompany"

organizationName := "Some Company"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  scalaTestPlay % Test,
  msSqlServer,
  scalaLogging,
  playAnorm
)

resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"



//show warnings
scalacOptions ++= Seq("-feature")

//D - show all durations
//F - show full stack traces
//I - show reminder of failed and canceled tests without stack traces
testOptions in Test += Tests.Argument("-oDFI")




//Docker
//~~~~~~

// No PID file as it creates problem when restarting docker container
javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)

dockerBaseImage := "openjdk:8-jre"

maintainer in Docker := "Nauman Badar"

//uncomment only if you need root previledges. Needed if using some external tools which need sudo access inside container.
//daemonUser in Docker := "root"

dockerExposedPorts := Seq(9000)

packageName in Docker := normalizedName.value
