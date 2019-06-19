import sbt._
import sbt.Keys._
import scala.language.reflectiveCalls

object Dependencies {

  val V = new {
    val circe = "0.12.0-M1"
    val scalatest = "3.0.8"
    val scalacheck = "1.14.0"
    val scalacheckMagnolia = "0.2.2" // no release
    val mockito = "1.9.5"
    val akka = "2.5.23"
    val akkaHttp = "10.1.8"
    val akkaPersistenceInMemory = "2.5.23" // no release
    val akkaHttpCirce = "1.25.2" // https://github.com/hseeberger/akka-http-json/pull/296
    val awscala = "0.5.+" // https://github.com/seratch/AWScala/issues/201
    val cats = "2.0.0-M4"
    val catsEffect = "2.0.0-M4"
    val monixCatnap = "3.0.0-RC2" // https://github.com/monix/monix/issues/862
    val config = "1.3.0"
    val scalacacheGuava = "0.9.3" // https://github.com/cb372/scalacache/issues/254
    val scalaLogging = "3.9.2"
    val logback = "1.2.3"
    val leveldb = "1.8"
    val mailin = "3.0.1"
    val jakartaMail = "1.6.3"
    val slick = "3.3.2"
    val postgresql = "42.2.5"
    val mysql = "8.0.15"
    val ldap = "4.0.10"
    val flyway = "5.2.4"
    val bcrypt = "0.4"
    val slf4j = "1.7.25"
    val scalameta = "4.1.12"
    val tapirCore = "0.5.2" // https://github.com/softwaremill/tapir/issues/130
    val scopt = "3.7.1"
    val scalafmtCore = "2.0.0-RC5" // https://github.com/scalameta/scalafmt/issues/1434
    val plantuml = "8059"
  }

  val circeCore = "io.circe" %% "circe-core" % V.circe
  val circeParser = "io.circe" %% "circe-parser" % V.circe
  val circeGeneric = "io.circe" %% "circe-generic" % V.circe
  val circeGenericExtras = "io.circe" %% "circe-generic-extras" % V.circe
  val scalatest = "org.scalatest" %% "scalatest" % V.scalatest
  val scalacheck = "org.scalacheck" %% "scalacheck" % V.scalacheck
  val scalacheckMagnolia = "com.github.chocpanda" %% "scalacheck-magnolia" % V.scalacheckMagnolia
  val mockito = "org.mockito" % "mockito-all" % V.mockito
  val akkaStream = "com.typesafe.akka" %% "akka-stream" % V.akka
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % V.akkaHttp
  val akkaPersistence = "com.typesafe.akka" %% "akka-persistence" % V.akka
  val akkaRemote = "com.typesafe.akka" %% "akka-remote" % V.akka
  val awscala = "com.github.seratch" %% "awscala" % V.awscala
  val catsCore = "org.typelevel" %% "cats-core" % V.cats
  val catsEffect = "org.typelevel" %% "cats-effect" % V.catsEffect
  val alleyCatsCore = "org.typelevel" %% "alleycats-core" % V.cats
  val akkaHttpCirce = "de.heikoseeberger" %% "akka-http-circe" % V.akkaHttpCirce
  val typesafeConfig = "com.typesafe" % "config" % V.config
  val scalaCacheGuava = "com.github.cb372" %% "scalacache-guava" % V.scalacacheGuava
  val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % V.scalaLogging
  val logback = "ch.qos.logback" % "logback-classic" % V.logback
  val levelDb = "org.fusesource.leveldbjni" % "leveldbjni-all" % V.leveldb
  val mailin = "com.sendinblue" % "sib-api-v3-sdk" % V.mailin
  val akkaPersistenceInMemory = "com.github.dnvriend" %% "akka-persistence-inmemory" % V.akkaPersistenceInMemory
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % V.akka
  val jakartaMail = "com.sun.mail" % "jakarta.mail" % V.jakartaMail
  val slick = "com.typesafe.slick" %% "slick" % V.slick
  val slickHikari = "com.typesafe.slick" %% "slick-hikaricp" % V.slick
  val postgresql = "org.postgresql" % "postgresql" % V.postgresql
  val bcrypt = "org.mindrot" % "jbcrypt" % V.bcrypt
  val flywayCore = "org.flywaydb" % "flyway-core" % V.flyway
  val mysql = "mysql" % "mysql-connector-java" % V.mysql
  val ldap = "com.unboundid" % "unboundid-ldapsdk" % V.ldap
  val monixCatnap = "io.monix" %% "monix-catnap" % V.monixCatnap
  val slf4jNop = "org.slf4j" % "slf4j-nop" % V.slf4j
  val diff = "ai.x" %% "diff" % "2.0"
  val scalameta = "org.scalameta" %% "scalameta" % V.scalameta
  val scopt = "com.github.scopt" %% "scopt" % V.scopt
  val tapirCore = "com.softwaremill.tapir" %% "tapir-core" % V.tapirCore
  val scalafmtCore = "org.scalameta" %% "scalafmt-core" % V.scalafmtCore
  val plantuml = "net.sourceforge.plantuml" % "plantuml" % V.plantuml

  val enumeroDependencies = List(
    scalatest,
    mockito,
  ).map(_ % Test)

  val enumeroCirceDependencies = List(
    circeCore,
  ) ++ List(
    circeParser,
    scalatest,
  ).map(_ % Test)

  val mailoDependencies = List(
    akkaStream,
    akkaHttp,
    akkaPersistence,
    akkaRemote,
    awscala,
    catsCore,
    alleyCatsCore,
    circeCore,
    circeGeneric,
    circeParser,
    typesafeConfig,
    akkaHttpCirce,
    scalaCacheGuava,
    scalaLogging,
    mailin,
    levelDb,
    jakartaMail,
  ) ++ List(
    scalatest,
    logback,
    akkaTestkit,
    akkaPersistenceInMemory,
  ).map(_ % Test)

  val toctocCoreDependencies = List(
    bcrypt,
    catsCore,
  ) ++ List(
    scalatest,
    scalacheck,
    scalacheckMagnolia,
    slf4jNop,
    catsEffect,
  ).map(_ % Test)

  val toctocSlickPostgresDependencies = List(
    postgresql,
    slick,
    slickHikari,
    catsEffect,
    monixCatnap,
  ) ++ List(
    scalatest,
    slf4jNop,
  ).map(_ % Test)

  val toctocSlickMySqlDependencies = List(
    mysql,
    slick,
    slickHikari,
    catsEffect,
    monixCatnap,
  ) ++ List(
    scalatest,
    slf4jNop,
  ).map(_ % Test)

  val toctocLdapDependencies = List(
    ldap,
    mysql,
    slick,
    slf4jNop,
    catsEffect,
  )

  val toctocCirceDependencies = List(
    circeCore,
    circeGeneric,
  ) ++ List(
    scalatest,
    scalacheck,
    scalacheckMagnolia,
  ).map(_ % Test)

  val metarpheusCoreDependencies = List(
    scalameta,
  ) ++ List(
    scalatest,
    diff,
  ).map(_ % Test)

  val metarpheusJsFacadeDependencies = List(
    circeCore,
    circeParser,
    circeGenericExtras,
  )

  val tapiroCoreDependencies = List(
    scalameta,
    scalafmtCore,
    tapirCore,
    scopt,
  )

  val docsDependencies = List(
    plantuml,
  )

}
