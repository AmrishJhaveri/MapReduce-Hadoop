name := "MR_Word_Count_3"

version := "0.1"

scalaVersion := "2.12.8"

assemblyMergeStrategy in assembly := {
  case PathList("META-INF", xs@_*) => MergeStrategy.discard
  case _ => MergeStrategy.first
}

// https://mvnrepository.com/artifact/org.apache.hadoop/hadoop-core
libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-core" % "1.2.1",
// "org.apache.hadoop" % "hadoop-common" % "2.8.0",
//  "org.apache.hadoop" % "hadoop-mapreduce-client-core" % "2.8.0",

"com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  //  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.scalatest" %% "scalatest" % "3.0.5" % "test",

  //MR testing Apache jar
//  "org.mockito" % "mockito-core" % "2.8.47" % Test,
//  "com.google.guava" % "guava" % "27.0.1-jre"


)

assemblyJarName in assembly := "author-map-dblp.jar"

mainClass in(Compile, run) := Some("AuthorMapping")