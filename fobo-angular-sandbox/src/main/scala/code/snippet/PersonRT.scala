package code.snippet

import net.liftweb.json.JsonAST.{JString, JArray, JValue}
import net.liftweb.http.{RoundTripInfo, RoundTripHandlerFunc}
import code.model.{PersonComponent,Person}
import code.lib.EmptyRoundTrip
import net.liftweb._
import common._

trait PersonRT extends EmptyRoundTrip with PersonComponent {
  
  private val logger = Logger(classOf[PersonRT]) 
  
  protected def personsQuery(value : JValue, func : RoundTripHandlerFunc) : Unit = {
     import net.liftweb.json.JsonDSL._
     //map the query result to a list containing JsonAST.JObject's 
     val jsondata = selectAllPersons().map { p => ( ("id" -> p.id) ~ ("name" -> p.name) ~ ("age" -> p.age)  ) }
     //send the json data to the client as a JArray using the RoundTripHandlerFunc 
     logger.info("personsQuery about to send jsondata.size="+jsondata.size)  
     func.send( JArray(jsondata) )
  }    
  
  protected def addPersonCmd(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    import net.liftweb.json.JsonParser._
    import net.liftweb.json.DefaultFormats
    import net.liftweb.json.JsonDSL._
    implicit val formats = DefaultFormats   
    //we need to extract the value as a sting and parse it to get a 
    //JValue that will be accepted for maping to Person
    val pval = parse(value.extract[String])
    //the Person case class will get the values 
    val person = pval.extract[Person]
    //insert it into the persons database table 
    insertPerson(person)
    //send back a status
    val retStaus = ("inserted" -> true)
    func.send(retStaus)
  }
  
  protected def updatePersonCmd(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    import net.liftweb.json.JsonParser._
    import net.liftweb.json.DefaultFormats
    import net.liftweb.json.JsonDSL._
    implicit val formats = DefaultFormats   
    //we need to extract the value as a sting and parse it to get a 
    //JValue that will be accepted for maping to Person
    val pval = parse(value.extract[String])
    //the Person case class will get the values 
    val person = pval.extract[Person]
    //insert it into the persons database table 
    logger.info("updatePersonCmd about to update person="+person.toString())  
    updatePerson(person)
    //send back a status
    val retStaus = ("updated" -> true)
    func.send(retStaus)    
  } 
  
  protected def deletePersonCmd(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    import net.liftweb.json.JsonParser._
    import net.liftweb.json.DefaultFormats
    import net.liftweb.json.JsonDSL._
    implicit val formats = DefaultFormats   
    //we need to extract the value as a sting and parse it to get a 
    //JValue that will be accepted for maping to Person
    val pval = parse(value.extract[String])
    //the Person case class will get the values 
    val person = pval.extract[Person]
    //insert it into the persons database table 
    logger.info("deletePersonCmd about to delete person="+person.toString())  
    deletePerson(person)
    //send back a status
    val retStaus = ("deleted" -> true)
    func.send(retStaus)    
  }   
  
  private val roundtrips : List[RoundTripInfo] = List("personsQuery" -> personsQuery _,                                                
                                                      "addPersonCmd" -> addPersonCmd _,
                                                      "updatePersonCmd" -> updatePersonCmd _,
                                                      "deletePersonCmd" -> deletePersonCmd _)
  abstract override def getRoundTrips = super.getRoundTrips ++ roundtrips  
}