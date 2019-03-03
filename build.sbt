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
  "org.scala-lang.modules" %% "scala-xml" % "1.1.1",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "ch.qos.logback" % "logback-classic" % "1.1.2"
)

assemblyJarName in assembly := "author-map-dblp.jar"

mainClass in (Compile, run) := Some("AuthorMapping")