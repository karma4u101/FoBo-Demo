import LiftModuleKeys.{liftVersion, liftEdition}

//##################################################################
//##
//##  Build settings
//##
//##############

lazy val commonSettings = Seq(
  organization := "net.liftweb",
  version := "2.1.0-SNAPSHOT",
  scalacOptions ++= Seq("-unchecked", "-deprecation"),
  autoAPIMappings := true
)

lazy val fobometa = (project in file("."))
  .settings(commonSettings: _*)
  // .enablePlugins(ScalaUnidocPlugin)
  .settings(name := "FoBo-Demo")
  .settings(scalaVersion in ThisBuild := "2.12.7")
  .settings(
    liftVersion in ThisBuild := { liftVersion ?? "3.3.0" }.value)
  .settings(liftEdition in ThisBuild := {
    liftVersion apply { _.substring(0, 3) }
  }.value)
  .aggregate(
    foboLiftTemplateDemo,
    foboLiftRoundTrips,
    bs3StarterTemplates,
    bs4StarterTemplates,
    mdStarterTemplates
  )

lazy val foboLiftTemplateDemo = (project in file("fobo-lift-template-demo"))
  .settings(commonSettings: _*)
  .settings(name := "fobo-lift-template-demo")

lazy val foboLiftRoundTrips = (project in file("fobo-angular-lift-roundtrips"))
  .settings(commonSettings: _*)
  .settings(name := "fobo-angular-lift-roundtrips")

lazy val bs3StarterTemplates = (project in file("pimping-lift-advanced-bs3"))
  .settings(commonSettings: _*)
  .settings(name := "pimping-lift-advanced-bs3")

lazy val bs4StarterTemplates = (project in file("pimping-lift-advanced-bs4"))
  .settings(commonSettings: _*)
  .settings(name := "pimping-lift-advanced-bs4")

lazy val mdStarterTemplates = (project in file("pimping-lift-advanced-md"))
  .settings(commonSettings: _*)
  .settings(name := "pimping-lift-advanced-md")

//##
//##
//##################################################################

//##################################################################
//##
//##  Common resolvers
//##
//##############

resolvers in ThisBuild ++= Seq(
  "snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "releases" at "https://oss.sonatype.org/service/local/staging/deploy/maven2"
)

//##
//##
//##################################################################

//##################################################################
//##
//##  Common plugins
//##
//##############

enablePlugins(JettyPlugin)

//##
//##
//##################################################################


//##################################################################
//##
//##  Common dependencies
//##
//##############

libraryDependencies in ThisBuild ++= {
  "net.liftweb"       %% "lift-webkit"                      % liftVersion.value % "compile" ::
    "net.liftweb"     %% "lift-mapper"                      % liftVersion.value % "compile" ::
    "net.liftmodules" %% ("fobo" + "_" + liftEdition.value) % "2.1.0"    % "compile" ::
    "net.liftweb"     %% "lift-testkit"                     % liftVersion.value % "test" ::
    Nil
}

//to be able to debug (inside eclipse) we need to move stuff to the top level src folders and currently
//we are debugging the bs3 sand-box stuff that the only reason we need this extra stuff here right now.
libraryDependencies in ThisBuild ++= Seq(
  "org.eclipse.jetty"       % "jetty-webapp"  % "8.1.7.v20120910"     % "test",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "test" artifacts Artifact(
    "javax.servlet",
    "jar",
    "jar")
)

libraryDependencies ++= {
  ((scalaVersion.value, liftVersion.value) match {
    case ("2.10.4", _) | ("2.9.2", _) | ("2.9.1", _) | ("2.9.1-1", _) =>
      "org.specs2" %% "specs2" % "1.12.3" % "test"
    case ("2.11.7", "2.6.2") | ("2.11.7", "2.6.3") =>
      "org.specs2" %% "specs2" % "2.3.11" % "test"
    case (_, "3.0.0") => "org.specs2" %% "specs2"      % "3.7"   % "test"
    case (_, "3.0.1") | (_, "3.0.2") => "org.specs2" %% "specs2-core" % "3.8.6" % "test"
    case (_, _) =>
      "org.specs2" %% "specs2-core" % "3.8.6" % "test" //lift 3.1.x
  }) ::
    ((scalaVersion.value, liftVersion.value) match {
      case ("2.10.4", _) | ("2.9.2", _) | ("2.9.1", _) | ("2.9.1-1", _) =>
        "org.specs2" %% "specs2" % "1.12.3" % "test"
      case ("2.11.7", "2.6.2") | ("2.11.7", "2.6.3") =>
        "org.specs2" %% "specs2" % "2.3.11" % "test"
      case (_, "3.0.0") =>
        "org.specs2" %% "specs2" % "3.7" % "test" //no mather extras for 3.7
      case (_, "3.0.1") | (_, "3.0.2") =>
        "org.specs2" %% "specs2-matcher-extra" % "3.8.6" % "test"
      case (_, _) =>
        "org.specs2" %% "specs2-matcher-extra" % "3.8.6" % "test" //lift 3.1.x
    }) ::
    ((scalaVersion.value, liftVersion.value) match {
      case (_, "2.6.2") | (_, "2.6.3") =>
        "org.scalacheck" %% "scalacheck" % "1.10.1" % "test"
      case (_, "3.0.0") =>
        "org.specs2" %% "specs2-scalacheck" % "3.7" % "test"
      case (_, "3.0.1") | (_, "3.0.2") =>
        "org.specs2" %% "specs2-scalacheck" % "3.8.6" % "test"
      case (_, _) =>
        "org.specs2" %% "specs2-scalacheck" % "3.8.6" % "test" //lift 3.1.x
    }) ::
    Nil
}

//##
//##
//##################################################################

