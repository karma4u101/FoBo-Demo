import LiftModuleKeys.{liftVersion, liftEdition}

moduleName := "fobo-lift-template-demo"

moduleName := { name.value + "_" + liftEdition.value }

version := "2.1.4-SNAPSHOT"

libraryDependencies ++= {
  "ch.qos.logback" % "logback-classic" % "1.0.6" % "compile" ::
    Nil
}
