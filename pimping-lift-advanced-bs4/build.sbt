import LiftModuleKeys.{liftEdition, liftVersion}

moduleName := "pimping-lift-advanced-bs4"

moduleName := { name.value + "_" + liftEdition.value }

version := "2.1.1-SNAPSHOT"

libraryDependencies ++= {
  "ch.qos.logback" % "logback-classic" % "1.0.6" % "compile" ::
    "com.h2database" % "h2" % "1.3.167" % "compile" ::
    "com.andersen-gott" %% "scravatar" % "1.0.3" % "compile" ::
    Nil
}
