import sbt._
import sbt.Keys._

object FoBoDemoBuild extends Build {

  val liftVersion = SettingKey[String]("liftVersion", "Version number of the Lift Web Framework")
  
  val liftEdition = SettingKey[String]("liftEdition", "Lift Edition (short version number to append to artifact name)")

  val liftLatestVersion = SettingKey[String]("liftLatestVersion", "Latest version number of the Lift Web Framework")
  
  val liftLatestEdition = SettingKey[String]("liftLatestEdition", "Latest Lift Edition (short version number to append to artifact name)")
  

  lazy val root = Project(id   = "FoBo-Demo", 
                  base = file(".")) aggregate(FoBoLiftTemplateDemo,Bs3StarterTemplates,FoBoAngularSandbox)
  
  lazy val FoBoLiftTemplateDemo = Project(id = "FoBo-Lift-Template-Demo",
                             base = file("fobo-lift-template-demo"))                 
   
    
  lazy val FoBoAngularSandbox = Project(id = "FoBo-Angular-Sandbox",
                             base = file("fobo-angular-sandbox")) 
                             
  lazy val FoBoBS3Sandbox = Project(id = "FoBo-Bootstrap3-Sandbox",
                             base = file("fobo-bs3-sandbox")) 
                                                      
                             
  lazy val Bs3StarterTemplates = Project(id = "Pimping-Lift-Advanced-Bs3",
                             base = file("pimping-lift_advanced_bs3"))                              
                                                      
  
}
