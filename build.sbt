import Dependencies._

val projectIdentitySettings = List(
  name := """Play 2.5 startup template with compile time Dependency Injection""",
  organization := "com.somecompany",
  organizationName := "Some Company",
  version := "1.0-SNAPSHOT"
)
val buildSettings = List(
  scalaVersion := "2.11.8",
  //show warnings
  scalacOptions ++= Seq("-feature"),
  //D - show all durations
  //F - show full stack traces
  //I - show reminder of failed and canceled tests without stack traces
  testOptions in Test += Tests.Argument("-oDFI"),
  parallelExecution in Test := false,
  fork in Test := true
)

lazy val root = (project in file("."))
  //fallback common settings
  .settings(inThisBuild(projectIdentitySettings ++ buildSettings))
  .aggregate(core, web)

lazy val core = (project in file("core"))
  //Integration Tests
  //~~~~~~~~~~~~~~~~~
  //has to have IntegrationTest scope
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(
    fork in IntegrationTest := true,
    testOptions in IntegrationTest += Tests.Argument("-oDFI")
  )
  //dependencies for core
  .settings(
  libraryDependencies ++= Seq(
    playAnorm,
    typesafeConfig,
    msSqlServer,
    playJson,
    jdbc,
    ws,
    scalaLogging,
    logback,
    scalaTest
  ))

lazy val web = (project in file("web"))
  .enablePlugins(PlayScala)
  .dependsOn(core)
  .settings(dockerSettings)
  //depdendies for web
  .settings(
    libraryDependencies ++= Seq(
      jdbc,
      cache,
      ws,
      scalaTestPlay % Test,
      msSqlServer,
      scalaLogging,
      playAnorm
    )
  )



lazy val dockerSettings = Seq(
  // No PID file as it creates problem when restarting docker container
  javaOptions in Universal += "-Dpidfile.path=/dev/null",
  dockerBaseImage := "openjdk:8-jre",
  maintainer in Docker := "Nauman Badar <naumanb@kth.se>",
  //uncomment only if you need root previledges. Needed if using some external tools which need sudo access inside container.
  //daemonUser in Docker := "root"
  dockerExposedPorts := Seq(9000),
  //  suppose to be the normalised name of project
  packageName in Docker := "multi-project-template-with-play"
)




//resolvers += "scalaz-bintray" at "http://dl.bintray.com/scalaz/releases"


// db resurrect with a tast from sbt
//~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
lazy val resurrectDb = taskKey[Unit]("Resurrect database from schema file")

val filter: ScopeFilter =
  ScopeFilter(
    inProjects(core),
    inConfigurations(IntegrationTest),
    inTasks(compile)
  )

resurrectDb := runMain.toTask(" db.DbBootstrapper").all(filter).value