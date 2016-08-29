name := """<descriptive name of your project>"""

organization := "<your company name>"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test,
  "net.sourceforge.jtds" % "jtds" % "1.3.1",
  "com.typesafe.play" %% "anorm" % "2.5.0",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0"
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

dockerBaseImage := "<your base image>"

maintainer in Docker := "your name <your email>"

//uncomment only if you need root previledges. Needed if using some external tools which need sudo access inside container.
//daemonUser in Docker := "root"

dockerExposedPorts := Seq(9000)

packageName in Docker := "<it will be your image name>"
