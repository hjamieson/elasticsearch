name := "logging-job"

version := "0.1"

scalaVersion := "2.13.1"

libraryDependencies ++= Seq(
  "ch.qos.logback" % "logback-classic" % "1.2.3" ,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2"
)

