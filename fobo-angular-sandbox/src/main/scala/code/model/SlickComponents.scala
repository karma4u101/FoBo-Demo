package code.model

import scala.slick.driver.H2Driver.simple._
import scala.slick.ast._
import scala.slick.ast.Util._
import net.liftweb._
import common._

trait SlickComponent {
  val db = Database.forURL("jdbc:h2:mem:test1;DB_CLOSE_DELAY=-1", driver="org.h2.Driver")
}

//Moved outside of trait to make json serialization simpler
case class Person(id: Int, name: String, age: Int )

trait PersonComponent extends SlickComponent with Loggable /*Logger*/ {
  
  class Persons(tag: Tag) extends Table[(Int, String, Int )](tag,"PERSONS") {
    def id = column[Int]("PER_ID", O.PrimaryKey, O.AutoInc)
    def name = column[String]("NAME")
    def age = column[Int]("AGE")
  
    def * = (id, name, age) 
  }
  val persons = TableQuery[Persons]  
  
  private val personsAutoInc = persons.map(p => (p.name, p.age)) returning persons.map(_.id) into {
    case (_, id) => id
  }  
  /*
  def insertPerson(person: Person)(implicit session: Session): Person = {
    val id = personsAutoInc.insert(person.name, person.age)
    person.copy(name = person.name, age = person.age)
  } 
  */
  def insertPerson(person: Person): Person = db.withSession {
    implicit session =>
    logger.info("insertPerson person="+person.toString)  
    val id = personsAutoInc.insert(person.name, person.age)
    person.copy(name = person.name, age = person.age)
  }   
  
  private val queryAll = for (p <- persons) yield (p.id,p.name,p.age)
  def selectAllPersons(): List[Person] = db.withSession {
    implicit session =>  
    queryAll.list.map(x => Person(x._1,x._2,x._3))
  }
  
}

trait CoffeeComponent extends SlickComponent with SupplierComponent {
 // Definition of the COFFEES table
  class Coffees(tag: Tag) extends Table[(String, Int, Double, Int, Int)](tag, "COFFEES") {
    def name = column[String]("COF_NAME", O.PrimaryKey)
    def supID = column[Int]("SUP_ID")
    def price = column[Double]("PRICE")
    def sales = column[Int]("SALES")
    def total = column[Int]("TOTAL")
    def * = (name, supID, price, sales, total)
    // A reified foreign key relation that can be navigated to create a join
    def supplier = foreignKey("SUP_FK", supID, suppliers)(_.id)
  }
  val coffees = TableQuery[Coffees]  
}

trait SupplierComponent extends SlickComponent {
  // Definition of the SUPPLIERS table
  class Suppliers(tag: Tag) extends Table[(Int, String, String, String, String, String)](tag, "SUPPLIERS") {
    def id = column[Int]("SUP_ID", O.PrimaryKey) // This is the primary key column
    def name = column[String]("SUP_NAME")
    def street = column[String]("STREET")
    def city = column[String]("CITY")
    def state = column[String]("STATE")
    def zip = column[String]("ZIP")
    // Every table needs a * projection with the same type as the table's type parameter
    def * = (id, name, street, city, state, zip)
  }
  val suppliers = TableQuery[Suppliers]    
}


