val scala3Version = "3.7.3"
val AkkaVersion = "2.8.8"
// val sparkVersion = "4.0.1"

lazy val root = project
  .in(file("."))
  .settings(
    name := "surplus-food-redistribution",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := scala3Version,
    libraryDependencies ++= Seq(
      "org.scalameta" %% "munit" % "1.0.0" % Test,
      "ch.qos.logback" % "logback-classic" % "1.5.20",

      // AKKA
      "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-actor-testkit-typed" % AkkaVersion % Test,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,

      // Akka HTTP for calling Python Spark API
      "com.typesafe.akka" %% "akka-http" % "10.5.3",
      "io.spray" %% "spray-json" % "1.3.6"
    )
  )
