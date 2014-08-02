package code.model

import scala.slick.driver.H2Driver.simple._
import scala.slick.ast._
import scala.slick.ast.Util._
import net.liftweb._
import common._

//see https://github.com/slick/slick-examples/blob/master/src/main/scala/com/typesafe/slick/examples/lifted/MultiDBCakeExample.scala
//for how to setup different drivers on for example test and production.

trait SlickComponent {
  val db = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver="org.h2.Driver")
}

//Moved outside of trait to make json serialization simpler
case class Person(id: Long, name: String, age: Int )

trait PersonComponent extends SlickComponent {
  
  private val logger = Logger(classOf[PersonComponent])
  
  class Persons(tag: Tag) extends Table[(Long, String, Int )](tag,"PERSONS") {
    def id = column[Long]("PER_ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def age = column[Int]("AGE")
  
    def * = (id, name, age) 
  }
  val persons = TableQuery[Persons]  
  
  private val personsAutoInc = persons.map(p => (p.name, p.age)) returning persons.map(_.id) into {
    case (_, id) => id
  }  
  
  private def findGById(id: Long) = for {
    p <- persons; if p.id === id
  } yield p  
  
  
  /*
  def insertPerson(person: Person)(implicit session: Session): Person = {
    val id = personsAutoInc.insert(person.name, person.age)
    person.copy(name = person.name, age = person.age)
  } 
  */
  def updatePerson(person: Person): Person = db.withSession {
    implicit session =>
    logger.debug("updatePerson person="+person.toString)  
    val p = findGById(person.id)
    p.update(person.id,person.name,person.age)
    person.copy(name = person.name, age = person.age)
  }
  
  def deletePerson(person: Person): Unit = db.withSession {
    implicit session =>
    logger.debug("deletePerson person="+person.toString)  
    val p = findGById(person.id)
    val affectedRowsCount = p.delete
    logger.info("deletePerson affectedRowsCount="+affectedRowsCount)
    val invoker = p.deleteInvoker
    val statement = p.deleteStatement    
  }  
    
  def insertPerson(person: Person): Person = db.withSession {
    implicit session =>
    logger.debug("insertPerson person="+person.toString)  
    val id = personsAutoInc.insert(person.name, person.age)
    person.copy(id=id,name = person.name, age = person.age)
  }   
  
  private val queryAll = for (p <- persons) yield (p.id,p.name,p.age)
  def selectAllPersons(): List[Person] = db.withSession {
    implicit session =>  
    queryAll.list.map(x => Person(x._1,x._2,x._3))
  }
  
}




