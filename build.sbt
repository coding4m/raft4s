import ProjectDependencies._
import ProjectSettings._
import com.typesafe.sbt.SbtMultiJvm.MultiJvmKeys.MultiJvm

version in ThisBuild := "1.0.0-SNAPSHOT"

organization in ThisBuild := "raft4s"

scalaVersion in ThisBuild := "2.11.8"

javacOptions in ThisBuild ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

externalResolvers in ThisBuild := Resolver.withDefaultResolvers(Seq.empty, mavenCentral = true)

isSnapshot in ThisBuild := true


lazy val root = (project in file("."))
  .aggregate(core)
  .settings(name := "raft4s")
  .settings(commonSettings: _*)
//      .settings(protocSettings: _*)
  .settings(documentationSettings: _*)
//  .settings(libraryDependencies ++= Seq(AkkaActor, Protobuf, Jpinyin, CommonLang3))
  .enablePlugins(HeaderPlugin, AutomateHeaderPlugin)
  .disablePlugins(SbtScalariform)


lazy val core = (project in file("raft4s-core"))
  .settings(name := "raft4s-core")
  .settings(commonSettings: _*)
  //    .settings(protocSettings: _*)
  .settings(integrationTestSettings: _*)
  .settings(libraryDependencies ++= Seq(AkkaCluster, AkkaRemote, CommonsIo, Java8Compat, Scalaz, Protobuf))
  .settings(libraryDependencies ++= Seq(AkkaTestkit % "test,it", AkkaTestkitMultiNode % "test", Javaslang % "test", JunitInterface % "test", Scalatest % "test,it"))
  .settings(integrationTestPublishSettings: _*)
  .configs(IntegrationTest, MultiJvm)
  .enablePlugins(HeaderPlugin, AutomateHeaderPlugin)
    