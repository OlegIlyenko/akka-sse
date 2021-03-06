import sbt._

object Version {
  val akka       = "2.3.11"
  val akkaHttp   = "1.0-RC4"
  val junit      = "4.12"
  val scala      = "2.11.7"
  val scalaCheck = "1.12.4"
  val scalaTest  = "2.2.5"
}

object Library {
  val akkaActor   = "com.typesafe.akka" %% "akka-actor"             % Version.akka
  val akkaHttp    = "com.typesafe.akka" %% "akka-http-experimental" % Version.akkaHttp
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit"           % Version.akka
  val junit       = "junit"             %  "junit"                  % Version.junit
  val scalaCheck  = "org.scalacheck"    %% "scalacheck"             % Version.scalaCheck
  val scalaTest   = "org.scalatest"     %% "scalatest"              % Version.scalaTest
}
