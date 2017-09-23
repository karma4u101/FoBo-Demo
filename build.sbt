moduleName := "FoBo-Demo"

organization := "net.liftweb"

version in ThisBuild := "0.10-SNAPSHOT"

//makes it possible to provide the lift version from sbt script / parameter
liftVersion in ThisBuild := { liftVersion ?? "3.1.1" }.value //"3.0.1" "2.6.2"

liftEdition in ThisBuild := { liftVersion apply { _.substring(0, 3) } }.value

crossScalaVersions := Seq("2.12.2",
                          "2.11.11",
                          "2.11.7",
                          "2.10.4",
                          "2.9.3",
                          "2.9.2",
                          "2.9.1-1",
                          "2.9.1")

scalaVersion in ThisBuild := "2.11.11"

EclipseKeys.withSource := true

EclipseKeys.skipParents in ThisBuild := false

resolvers ++= Seq(
  "snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
  "staging" at "https://oss.sonatype.org/content/repositories/staging",
  "releases" at "https://oss.sonatype.org/content/repositories/releases"
)

enablePlugins(JettyPlugin)

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies ++= {
  "net.liftweb"       %% "lift-webkit"                      % liftVersion.value % "compile" ::
    "net.liftweb"     %% "lift-mapper"                      % liftVersion.value % "compile" ::
    "net.liftmodules" %% ("fobo" + "_" + liftEdition.value) % "2.0-SNAPSHOT"    % "compile" ::
    "net.liftweb"     %% "lift-testkit"                     % liftVersion.value % "test" ::
    Nil
}

//to be able to debug (inside eclipse) we need to move stuff to the top level src folders and currently
//we are debugging the bs3 sand-box stuff that the only reason we need this extra stuff here right now.
libraryDependencies ++= Seq(
  "org.eclipse.jetty"       % "jetty-webapp"  % "8.1.7.v20120910"     % "container,test",
  "org.eclipse.jetty.orbit" % "javax.servlet" % "3.0.0.v201112011016" % "container,test" artifacts Artifact(
    "javax.servlet",
    "jar",
    "jar"),
  "com.andersen-gott" %% "scravatar" % "1.0.3"
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
