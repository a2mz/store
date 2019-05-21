scalaVersion := "2.12.8"
name := "store"
organization := "om.store"
version := "1.0"

libraryDependencies ++= {
  val catsVersion       = "1.6.0"
  val enumeratumVersion = "1.5.12"
  val playJsonVersion   = "2.6.8"
  val slf4jVersion      = "1.7.22"
  Seq(
    "org.typelevel" %% "cats-macros"   % catsVersion,
    "org.typelevel" %% "cats-kernel"   % catsVersion,
    "org.typelevel" %% "cats-core"     % catsVersion,
    "org.typelevel" %% "cats-mtl-core" % "0.4.0",
    //akka----------------------------------------------------
    "com.typesafe.akka" %% "akka-actor" % "2.4.17",
    "com.typesafe.akka" %% "akka-http"  % "10.0.6",
    //Logging-------------------------------------------------
    "org.slf4j"                  % "slf4j-api"      % slf4jVersion,
    "org.slf4j"                  % "slf4j-simple"   % slf4jVersion,
    "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
    //--------------------------------------------------------
    "com.typesafe"      % "config"                % "1.3.1",
    "com.beachape"      %% "enumeratum"           % enumeratumVersion,
    "com.beachape"      %% "enumeratum-play-json" % enumeratumVersion,
    "com.typesafe.play" %% "play-json"            % playJsonVersion,
    "de.heikoseeberger" %% "akka-http-play-json"  % "1.20.1"
  )
}

scalacOptions ++= Seq("-Xfatal-warnings",
                      "-feature",
                      "-language:higherKinds",
                      "-language:implicitConversions",
                      "-language:postfixOps",
                      "-deprecation")
