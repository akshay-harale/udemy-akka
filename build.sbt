name := """cassandra-spark"""

version := "1.0"

scalaVersion := "2.11.8"




libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.3",
  "com.typesafe.akka" %% "akka-persistence" % "2.4.3",
  "com.typesafe.akka" %% "akka-stream" % "2.4.3",
  "org.iq80.leveldb"            % "leveldb"          % "0.7",
  "org.fusesource.leveldbjni"   % "leveldbjni-all"   % "1.8"
)

// https://mvnrepository.com/artifact/com.typesafe.akka/akka-persistence-query_2.11
libraryDependencies += "com.typesafe.akka" % "akka-persistence-query_2.11" % "2.5.0"
