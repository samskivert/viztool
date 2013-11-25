seq(samskivert.POMUtil.pomToSettings("pom.xml") :_*)

crossPaths := false

scalaVersion := "2.10.0"

autoScalaLibrary in Compile := false // no scala-library dependency

fork in Test := true

javacOptions ++= Seq("-Xlint", "-Xlint:-serial", "-source", "1.6", "-target", "1.6")

// allows SBT to run junit tests
libraryDependencies += "com.novocode" % "junit-interface" % "0.10" % "test->default"
