import LiftModuleKeys.{liftEdition, liftVersion}

moduleName := "pimping-lift-advanced-bs4"

moduleName := { name.value + "_" + liftEdition.value }

version := "2.1.3-SNAPSHOT"

libraryDependencies ++= {
  "ch.qos.logback" % "logback-classic" % "1.0.6" % "compile" ::
    "com.h2database" % "h2" % "1.3.167" % "compile" ::
    "com.andersen-gott" %% "scravatar" % "1.0.3" % "compile" ::
    "org.webjars" % "bootstrap" % "4.2.1" ::
    "org.webjars" % "jquery" % "3.3.1" ::
    "org.webjars" % "popper.js" % "1.14.6" ::
    "org.webjars" % "font-awesome" % "4.6.3" ::
    Nil
}
