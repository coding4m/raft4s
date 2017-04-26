/*
 * Copyright 2015 - 2016 Red Bull Media House GmbH <http://www.redbullmediahouse.com> - all rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import sbt._

object ProjectDependencyVersions {
  val CommonLang3Version = "3.5"
  val JpinyinVersion = "1.0"
  val AkkaVersion = "2.5.0"
  val ProtobufVersion = "2.5.0"
  val Log4jVersion = "2.5"
}

object ProjectDependencies {
  import ProjectDependencyVersions._

  val CommonLang3 =          "org.apache.commons"         % "commons-lang3"             % CommonLang3Version
  val Jpinyin =              "com.github.stuxuhai"       % "jpinyin"                     % JpinyinVersion
  val CassandraDriver =      "com.datastax.cassandra"     % "cassandra-driver-core"     % "3.0.2"
  val CassandraConnector =   "com.datastax.spark"        %% "spark-cassandra-connector" % "1.6.0-M2"
  val Javaslang =            "io.javaslang"              % "javaslang"                 % "2.0.5"
  val JunitInterface =       "com.novocode"               % "junit-interface"           % "0.11"

  val AkkaActor =            "com.typesafe.akka"         %% "akka-actor"                % AkkaVersion
  val AkkaRemote =           "com.typesafe.akka"         %% "akka-remote"               % AkkaVersion
  val AkkaCluster =          "com.typesafe.akka"         %% "akka-cluster"              % AkkaVersion
  val AkkaStream =           "com.typesafe.akka"         %% "akka-stream"               % AkkaVersion
  val AkkaStreamTestkit =    "com.typesafe.akka"         %% "akka-stream-testkit"       % AkkaVersion
  val AkkaTestkit =          "com.typesafe.akka"         %% "akka-testkit"              % AkkaVersion
  val AkkaTestkitMultiNode = "com.typesafe.akka"         %% "akka-multi-node-testkit"   % AkkaVersion

  val Protobuf =             "com.google.protobuf"        % "protobuf-java"             % ProtobufVersion
  val CommonsIo =            "commons-io"                 % "commons-io"                % "2.4"
  val Log4jApi =             "org.apache.logging.log4j"   % "log4j-api"                 % Log4jVersion
  val Log4jCore =            "org.apache.logging.log4j"   % "log4j-core"                % Log4jVersion
  val Log4jSlf4j =           "org.apache.logging.log4j"   % "log4j-slf4j-impl"          % Log4jVersion
  val Java8Compat =          "org.scala-lang.modules"    %% "scala-java8-compat"   % "0.8.0"
  val Scalatest =            "org.scalatest"             %% "scalatest"                 % "3.0.0"
  val Scalaz =               "org.scalaz"                %% "scalaz-core"               % "7.2.7"
}

