package code.model

// Use H2Driver to connect to an H2 database
import scala.slick.driver.H2Driver.simple._
import net.liftweb._
import common._


object SlickHelper extends PersonComponent with SupplierComponent with CoffeeComponent {
  
  def initSchema() = db.withSession {
    implicit session =>
    // Create the tables, including primary and foreign keys
    (suppliers.ddl ++ coffees.ddl ++ persons.ddl).create

    // Insert some suppliers
    suppliers += (101, "Acme, Inc.", "99 Market Street", "Groundsville", "CA", "95199")
    suppliers += ( 49, "Superior Coffee", "1 Party Place", "Mendocino", "CA", "95460")
    suppliers += (150, "The High Ground", "100 Coffee Lane", "Meadows", "CA", "93966")

    // Insert some coffees (using JDBC's batch insert feature, if supported by the DB)
    coffees ++= Seq(
      ("Colombian", 101, 7.99, 0, 0),
      ("French_Roast", 49, 8.99, 0, 0),
      ("Espresso", 150, 9.99, 0, 0),
      ("Colombian_Decaf", 101, 8.99, 0, 0),
      ("French_Roast_Decaf", 49, 9.99, 0, 0)
    )
    
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
    // Iterate through all coffees and output them
    println("Coffees:")
    coffees foreach { case (name, supID, price, sales, total) =>
      println(" " + name + "\t" + supID + "\t" + price + "\t" + sales + "\t" + total)
    }

    println("Persons:")
      persons foreach { case (id,name,age) =>
      println(" " + id + "\t" + name + "\t" + age )  
    }
    
    // Why not let the database do the string conversion and concatenation?
    println("Coffees (concatenated by DB):")
    val q1 = for(c <- coffees)
      yield LiteralColumn(" ") ++ c.name ++ "\t" ++ c.supID.asColumnOf[String] ++
        "\t" ++ c.price.asColumnOf[String] ++ "\t" ++ c.sales.asColumnOf[String] ++
        "\t" ++ c.total.asColumnOf[String]
    // The first string constant needs to be lifted manually to a LiteralColumn
    // so that the proper ++ operator is found
    q1 foreach println

    // Perform a join to retrieve coffee names and supplier names for
    // all coffees costing less than $9.00
    println("Manual join:")
    val q2 = for {
      c <- coffees if c.price < 9.0
      s <- suppliers if s.id === c.supID
    } yield (c.name, s.name)
    for(t <- q2) println(" " + t._1 + " supplied by " + t._2)

    // Do the same thing using the navigable foreign key
    println("Join by foreign key:")
    val q3 = for {
      c <- coffees if c.price < 9.0
      s <- c.supplier
    } yield (c.name, s.name)
    // This time we read the result set into a List
    val l3: List[(String, String)] = q3.list
    for((s1, s2) <- l3) println(" " + s1 + " supplied by " + s2)

    // Check the SELECT statement for that query
    println(q3.selectStatement)

    // Compute the number of coffees by each supplier
    println("Coffees per supplier:")
    val q4 = (for {
      c <- coffees
      s <- c.supplier
    } yield (c, s)).groupBy(_._2.id).map {
      case (_, q) => (q.map(_._2.name).min.get, q.length)
    }
    // .get is needed because Slick cannot enforce statically that
    // the supplier is always available (being a non-nullable foreign key),
    // thus wrapping it in an Option
    q4 foreach { case (name, count) =>
      println(" " + name + ": " + count)
    }      
  }
  
}