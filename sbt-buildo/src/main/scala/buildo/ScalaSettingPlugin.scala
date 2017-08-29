package buildo

import sbt._
import Keys._

object ScalaSettingPlugin extends AutoPlugin {
  override def requires = plugins.JvmPlugin
  override def trigger = allRequirements

  override def buildSettings: Seq[Def.Setting[_]] = baseBuildSettings
  override def projectSettings: Seq[Def.Setting[_]] = baseSettings

  lazy val baseBuildSettings: Seq[Def.Setting[_]] = Seq(
    organization := "io.buildo"
  )

  def crossFlags(scalaVersion: String): Seq[String] =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((2, 11)) => Seq("-Yinline-warnings")
      case Some((2, 12)) => Seq("-opt-warnings")
      case _ => Nil
    }

  lazy val baseSettings: Seq[Def.Setting[_]] = Seq(
    cancelable in Global := true,
    scalacOptions ++= Seq(
      "-encoding", "utf8",
      "-deprecation", "-feature", "-unchecked", "-Xlint",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-Xfuture",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Ywarn-unused",
      "-Ywarn-unused-import"
    ) ++ crossFlags(scalaVersion.value),
    resolvers += Resolver.jcenterRepo
  )
}

