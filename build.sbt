moduleName := "FoBo-Demo"

organization := "net.liftweb"

version in ThisBuild := "0.5-SNAPSHOT"

liftVersion in ThisBuild <<= liftVersion ?? "3.0-SNAPSHOT" //2.6-RC1

liftLatestVersion in ThisBuild <<= liftLatestVersion ?? "3.0-SNAPSHOT"

liftEdition in ThisBuild <<= liftVersion apply { _.substring(0,3) }

liftLatestEdition in ThisBuild <<= liftLatestVersion apply { _.substring(0,3) }

//name <<= (name, liftEdition) { (n, e) =>  n + "_" + e }

crossScalaVersions := Seq("2.11.1", "2.10.4", "2.9.3", "2.9.2", "2.9.1-1", "2.9.1")

scalaVersion in ThisBuild := "2.11.1"

EclipseKeys.withSource := true

EclipseKeys.skipParents in ThisBuild := false

resolvers ++= Seq("snapshots"     at "https://oss.sonatype.org/content/repositories/snapshots",
                  "staging"       at "https://oss.sonatype.org/content/repositories/staging",
                  "releases"      at "https://oss.sonatype.org/content/repositories/releases"
                 )

seq(webSettings :_*)

scalacOptions ++= Seq("-deprecation", "-unchecked")

libraryDependencies <++= (liftVersion,liftEdition,version) { (v,e,mv) =>
    "net.liftweb"     %% "lift-webkit"            % v    % "compile" ::
    "net.liftweb"     %% "lift-mapper"            % v    % "compile" ::
    "net.liftmodules" %% ("fobo"+"_"+e)          % "1.4-SNAPSHOT" % "compile" ::
    Nil
}

//to be able to debug (inside eclipse) we need to move stuff to the topp level src folders and currently 
//we are debuging the bs3 sandbox stuff that the only reason we need this extra stuff here right now.
libraryDependencies ++= Seq(
    "org.eclipse.jetty"       % "jetty-webapp"            % "8.1.7.v20120910"     % "container,test",
    "org.eclipse.jetty.orbit" % "javax.servlet"           % "3.0.0.v201112011016" % "container,test" artifacts Artifact("javax.servlet", "jar", "jar"),
    "ch.qos.logback"          % "logback-classic"         % "1.0.6",
    "com.typesafe.slick"      %% "slick"                  % "2.1.0",
    "com.h2database"          % "h2"                      % "1.3.167",
    "com.andersen-gott"       %% "scravatar"              % "1.0.3"
  )
  
  libraryDependencies <++= scalaVersion { sv =>
  (sv match {
      case "2.11.1"  => "org.specs2" %% "specs2" % "2.3.12" % "test"
      case "2.10.4" | "2.9.2" | "2.9.1" | "2.9.1-1" => "org.specs2" %% "specs2" % "1.12.3" % "test"
      case _ => "org.specs2" %% "specs2" % "1.12.3" % "test"
      }) ::
   (sv match {
      case "2.11.1"  => "org.scalacheck" %% "scalacheck" % "1.11.4" % "test"
      case "2.10.4" | "2.9.2" => "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
      case _ => "org.scalacheck" %% "scalacheck" % "1.10.0" % "test"
      }) ::
  Nil
}