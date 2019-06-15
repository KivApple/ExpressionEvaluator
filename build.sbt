name := "ExpressionEvaluator"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "org.ow2.asm" % "asm" % "7.1"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.5"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.5" % "test"

logBuffered in Test := false
