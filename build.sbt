name := "Weather API Practice by Alan Chin"

// *****************************************************************************
// Projects
// *****************************************************************************

lazy val pure =
  project
    .in(file("."))
    .enablePlugins(AutomateHeaderPlugin)
    .configs(IntegrationTest)
    .settings(settings)
    .settings(
      Defaults.itSettings,
      headerSettings(IntegrationTest),
      inConfig(IntegrationTest)(scalafmtSettings),
      IntegrationTest / console / scalacOptions --= Seq("-Xfatal-warnings", "-Ywarn-unused-import"),
      IntegrationTest / parallelExecution := false,
      IntegrationTest / unmanagedSourceDirectories := Seq((IntegrationTest / scalaSource).value)
    )
    .settings(
      libraryDependencies ++= Seq(
        library.catsCore,
        library.circeCore,
        library.circeGeneric,
        library.circeRefined,
        library.circeParser,
        library.http4sBlazeClient,
        library.http4sBlazeServer,
        library.http4sCirce,
        library.http4sDsl,
        library.kittens,
        library.logback,
        library.pureConfig,
        library.refinedCats,
        library.refinedCore,
        library.refinedPureConfig,
        library.refinedScalaCheck % IntegrationTest,
        library.scalaCheck        % IntegrationTest,
        library.refinedScalaCheck % Test,
        library.scalaCheck        % Test,
      )
    )

// *****************************************************************************
// Library dependencies
// *****************************************************************************

lazy val library =
  new {
    object Version {
      val cats         = "2.1.1"
      val circe        = "0.11.1"
      val http4s       = "0.20.10"
      val kittens      = "1.2.1"
      val logback      = "1.2.3"
      val pureConfig   = "0.11.1"
      val refined      = "0.9.9"
      val scalaCheck   = "1.14.0"
    }
    val catsCore          = "org.typelevel"          %% "cats-core"            % Version.cats
    val circeCore         = "io.circe"               %% "circe-core"           % Version.circe
    val circeGeneric      = "io.circe"               %% "circe-generic"        % Version.circe
    val circeRefined      = "io.circe"               %% "circe-refined"        % Version.circe
    val circeParser       = "io.circe"               %% "circe-parser"         % Version.circe
    val http4sBlazeServer = "org.http4s"             %% "http4s-blaze-server"  % Version.http4s
    val http4sBlazeClient = "org.http4s"             %% "http4s-blaze-client"  % Version.http4s
    val http4sCirce       = "org.http4s"             %% "http4s-circe"         % Version.http4s
    val http4sDsl         = "org.http4s"             %% "http4s-dsl"           % Version.http4s
    val kittens           = "org.typelevel"          %% "kittens"              % Version.kittens
    val logback           = "ch.qos.logback"         %  "logback-classic"      % Version.logback
    val pureConfig        = "com.github.pureconfig"  %% "pureconfig"           % Version.pureConfig
    val refinedCore       = "eu.timepit"             %% "refined"              % Version.refined
    val refinedCats       = "eu.timepit"             %% "refined-cats"         % Version.refined
    val refinedPureConfig = "eu.timepit"             %% "refined-pureconfig"   % Version.refined
    val refinedScalaCheck = "eu.timepit"             %% "refined-scalacheck"   % Version.refined
    val scalaCheck        = "org.scalacheck"         %% "scalacheck"           % Version.scalaCheck
  }

// *****************************************************************************
// Settings
// *****************************************************************************

lazy val settings =
  commonSettings ++
    scalafmtSettings

val licenseText = s"""Personal Practice""".stripMargin

def compilerSettings(sv: String) =
  CrossVersion.partialVersion(sv) match {
    case Some((2, 13)) =>
      Seq(
        "-deprecation",
        "-explaintypes",
        "-feature",
        "-language:higherKinds",
        "-unchecked",
        "-Xcheckinit",
        "-Xfatal-warnings",
        "-Xlint:adapted-args",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Ywarn-dead-code",
        "-Ywarn-extra-implicit",
        "-Ywarn-numeric-widen",
        //"-Ywarn-unused:implicits",
        "-Ywarn-unused:imports",
        //"-Ywarn-unused:locals",
        //"-Ywarn-unused:params",
        "-Ywarn-unused:patvars",
        "-Ywarn-unused:privates",
        "-Ywarn-value-discard",
        "-Ycache-plugin-class-loader:last-modified",
        "-Ycache-macro-class-loader:last-modified"
      )
    case _ =>
      Seq(
        "-deprecation",
        "-encoding", "UTF-8",
        "-explaintypes",
        "-feature",
        "-language:higherKinds",
        "-target:jvm-1.8",
        "-unchecked",
        "-Xcheckinit",
        "-Xfatal-warnings",
        "-Xfuture",
        "-Xlint:adapted-args",
        "-Xlint:by-name-right-associative",
        "-Xlint:constant",
        "-Xlint:delayedinit-select",
        "-Xlint:doc-detached",
        "-Xlint:inaccessible",
        "-Xlint:infer-any",
        "-Xlint:missing-interpolator",
        "-Xlint:nullary-override",
        "-Xlint:nullary-unit",
        "-Xlint:option-implicit",
        "-Xlint:package-object-classes",
        "-Xlint:poly-implicit-overload",
        "-Xlint:private-shadow",
        "-Xlint:stars-align",
        "-Xlint:type-parameter-shadow",
        "-Xlint:unsound-match",
        "-Ydelambdafy:method",
        "-Yno-adapted-args",
        "-Ypartial-unification",
        "-Ywarn-numeric-widen",
        "-Ywarn-unused-import",
        "-Ywarn-value-discard"
      )
  }

lazy val commonSettings =
  Seq(
    scalaVersion := "2.12.10",
    crossScalaVersions := Seq(scalaVersion.value, "2.13.10"),
    organization := "kuo.lung.chin",
    organizationName := "Kuo Lung (Alan) Chin",
    startYear := Some(2019),
    headerLicense := Some(HeaderLicense.Custom(licenseText)),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.10.3"),
    scalacOptions ++= compilerSettings(scalaVersion.value),
    Compile / console / scalacOptions --= Seq("-Xfatal-warnings", "-Ywarn-unused-import"),
    Compile / unmanagedSourceDirectories := Seq((Compile / scalaSource).value),
    Test / console / scalacOptions --= Seq("-Xfatal-warnings", "-Ywarn-unused-import"),
    Test / unmanagedSourceDirectories := Seq((Test / scalaSource).value),
  )

lazy val scalafmtSettings =
  Seq(
    scalafmtOnCompile := true
  )
