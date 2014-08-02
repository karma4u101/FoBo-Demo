package code.model

// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._
import net.liftweb._
import common._


object SlickHelper extends PersonComponent  {
  
  def initSchema() = db.withSession {
    implicit session =>
    // Create the tables, including primary and foreign keys
    (persons.ddl).create

    persons ++= Seq(
      (1,"Tiancum",43),
      (2,"Jacob",27),
      (3,"Nephi",29),
      (4,"Enos",34),
      (5,"Peter",46),
      (6,"UllaBella",42)
    )
    
  }

  def demoRun() = db.withSession {
    implicit session =>

    println("Persons:")
      persons foreach { case (id,name,age) =>
      println(" " + id + "\t" + name + "\t" + age )  
    }
       
  }
  
}