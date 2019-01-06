import LiftModuleKeys.{liftEdition, liftVersion}

moduleName := "fobo-angular-lift-roundtrips"

moduleName := { name.value + "_" + liftEdition.value }

version := "2.1.1-SNAPSHOT"

libraryDependencies ++= {
  "ch.qos.logback" % "logback-classic" % "1.0.6" % "compile" ::
    "com.typesafe.slick" %% "slick" % "2.1.0" % "compile" ::
    "com.h2database" % "h2" % "1.3.170" % "compile" ::
    "com.andersen-gott" %% "scravatar" % "1.0.3" % "compile" ::
    Nil
}
