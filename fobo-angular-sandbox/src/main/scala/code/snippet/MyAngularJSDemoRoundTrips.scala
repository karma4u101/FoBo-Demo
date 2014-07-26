package code.snippet

import net.liftweb.json.JsonAST.{JString, JArray, JValue}
import net.liftweb.http.{RoundTripInfo, RoundTripHandlerFunc}
import code.model.{PersonComponent,Person}
import code.lib.EmptyRoundTrip
import net.liftweb._
import common._

// trait SimpleRoundTrips extends EmptyRoundTrip ==>> MyAJSDemoRoundTrips extends EmptyRoundTrip .... extends PageRoundTrips
trait MyAngularJSDemoRoundTrips extends EmptyRoundTrip with PersonComponent {
  
  private val logger = Logger(classOf[MyAngularJSDemoRoundTrips]) 
  
  protected def doSimpleRT(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    //send the json data to the client as a JString using the RoundTripHandlerFunc
    logger.info("doSimpleRT awsome")  
    func.send(JString("There and back again!"))
  }
  
  protected def getPersonGridData(value : JValue, func : RoundTripHandlerFunc) : Unit = {
     import net.liftweb.json.JsonDSL._
     //map the query result to a list containing JsonAST.JObject's
     val jsondata = selectAllPersons().map { p => (("id" -> p.id) ~ ("name" -> p.name) ~ ("age" -> p.age)) }
     //send the json data to the client as a JArray using the RoundTripHandlerFunc 
     func.send( JArray(jsondata) )
  } 
  
  protected def addPersonGridData(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    import net.liftweb.json.JsonParser._
    import net.liftweb.json.DefaultFormats
    implicit val formats = DefaultFormats   
    //we need to extract the value as a sting and parse it to get a 
    //JValue that will be accepted for maping to Person
    val pval = parse(value.extract[String])
    //the Person case class will get the values 
    val person = pval.extract[Person]
    //insert it into the persons database table 
    insertPerson(person)
    //call to update the grid
    getPersonGridData(value, func)
  }
  
  protected def updatePersonGridData(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    import net.liftweb.json.JsonParser._
    import net.liftweb.json.DefaultFormats
    implicit val formats = DefaultFormats   
    //we need to extract the value as a sting and parse it to get a 
    //JValue that will be accepted for maping to Person
    val pval = parse(value.extract[String])
    //the Person case class will get the values 
    val person = pval.extract[Person]
    //insert it into the persons database table 
    logger.info("updatePersonGridData person to update is person="+person.toString())  
    updatePerson(person)
    //call to update the grid
    getPersonGridData(value, func)
  }  
  
  private val roundtrips : List[RoundTripInfo] = List("doSimpleRT" -> doSimpleRT _, 
                                                      "getPersonGridData" -> getPersonGridData _,
                                                      "addPersonGridData" -> addPersonGridData _,
                                                      "updatePersonGridData" -> updatePersonGridData _)
  abstract override def getRoundTrips = super.getRoundTrips ++ roundtrips  
}