
name := "cats-effect-r-and-d"

version := "0.1"

scalaVersion := "2.13.3"

scalacOptions += "-Ymacro-annotations"
scalafmtOnCompile := true

val versions = new {
  val cats          = "2.1.1"
  val catsEffect    = "2.1.3"
  val shapeless     = "2.3.3"
  val catsMeowMtl   = "0.4.0"
  val circe         = "0.13.0"
  val fs2           = "2.4.2"
  val http4s        = "0.21.3"
  val log4cats      = "1.1.1"
  val refined       = "0.9.14"
  val redis4cats    = "0.10.0"
  val skunk         = "0.0.11"
  val monocle       = "2.0.5"
  val betterMonadicFor = "0.3.1"
  val logback          = "1.2.3"

  val scalaCheck    = "1.14.3"
  val scalaTest     = "3.2.0"
  val scalaTestPlus = "3.2.0.0"
}

libraryDependencies ++= Seq(
  compilerPlugin("org.typelevel"    % "kind-projector" % "0.11.0" cross CrossVersion.full),
//  compilerPlugin("org.scalamacros" %% "paradise"       % "2.1.1"  cross CrossVersion.full),
  "org.typelevel"                     %% "cats-core"                      % versions.cats,
  "org.typelevel"                     %% "cats-effect"                    % versions.catsEffect,
  "com.olegpy"                        %% "meow-mtl-core"                  % versions.catsMeowMtl,
  "com.olegpy"                        %% "better-monadic-for"             % versions.betterMonadicFor,
  "co.fs2"                            %% "fs2-core"                       % versions.fs2,
  "com.chuusai"                       %% "shapeless"                      % versions.shapeless,
  "org.scalacheck"                    %% "scalacheck"                     % versions.scalaCheck,
  "io.chrisdavenport"                 %% "log4cats-slf4j"                 % versions.log4cats,
  "ch.qos.logback"                    %  "logback-classic"                % versions.logback,
  "dev.profunktor"                    %% "redis4cats-log4cats"            % versions.redis4cats,
  "org.tpolecat"                      %% "skunk-core"                     % versions.skunk,
  "org.tpolecat"                      %% "skunk-circe"                    % versions.skunk,
  "dev.profunktor"                    %% "redis4cats-effects"             % versions.redis4cats,
  "dev.profunktor"                    %% "redis4cats-log4cats"            % versions.redis4cats,
  "org.http4s"                        %% "http4s-dsl"                     % versions.http4s,
  "org.http4s"                        %% "http4s-blaze-server"            % versions.http4s,
  "org.http4s"                        %% "http4s-blaze-client"            % versions.http4s,
  "org.http4s"                        %% "http4s-circe"                   % versions.http4s,
  "io.circe"                          %% "circe-core"                     % versions.circe,
  "io.circe"                          %% "circe-generic"                  % versions.circe,
  "io.circe"                          %% "circe-parser"                   % versions.circe,
  "io.circe"                          %% "circe-refined"                  % versions.circe,
  "eu.timepit"                        %% "refined"                        % versions.refined,
  "eu.timepit"                        %% "refined-cats"                   % versions.refined,
  "org.scalatestplus"                 %% "scalacheck-1-14"                % versions.scalaTestPlus,
  "com.github.julien-truffaut"        %% "monocle-core"                   % versions.monocle,
  "com.github.julien-truffaut"        %% "monocle-macro"                  % versions.monocle,
  "com.github.julien-truffaut"        %% "monocle-law"                    % versions.monocle % "test",
  "org.scalatest"                     %% "scalatest"                      % versions.scalaTest % "test"
)


lazy val tests = (project in file("modules/tests"))
  .configs(IntegrationTest)
  .settings(
    name := "cats-effect-r-and-d-test-suite",
    scalacOptions += "-Ymacro-annotations",
    scalafmtOnCompile := true,
    Defaults.itSettings
  )
  .dependsOn(core)

lazy val core = (project in file("modules/core"))
  .enablePlugins(DockerPlugin)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "cats-effect-r-and-d",
    packageName in Docker := "cats-effect-r-and-d",
    scalacOptions += "-Ymacro-annotations",
    scalafmtOnCompile := true,
    resolvers += Resolver.sonatypeRepo("snapshots"),
    Defaults.itSettings,
    dockerBaseImage := "openjdk:8u201-jre-alpine3.9",
    dockerExposedPorts ++= Seq(8080),
    makeBatScripts := Seq(),
    dockerUpdateLatest := true
  )