package code.snippet

import net.liftweb.json.JsonAST.{JString, JArray, JValue, JField, JInt}
import net.liftweb.json.JsonDSL._
import net.liftweb.json.JsonParser._
import net.liftweb.json.DefaultFormats
import net.liftweb.http.{RoundTripInfo, RoundTripHandlerFunc}
import code.model.{PersonComponent,Person}
import code.lib.EmptyRoundTrip
import net.liftweb._
import common._

trait PersonRT extends EmptyRoundTrip with PersonComponent {
  
  private val logger = Logger(classOf[PersonRT]) 
  
  protected def personsQuery(value : JValue, func : RoundTripHandlerFunc) : Unit = {
     //map the query result to a list containing JsonAST.JObject's 
     val jsondata = selectAllPersons().map { p => ( ("id" -> p.id) ~ ("name" -> p.name) ~ ("age" -> p.age)  ) }
     //send the json data to the client as a JArray using the RoundTripHandlerFunc 
     logger.debug("personsQuery about to send jsondata.size="+jsondata.size)  
     func.send( JArray(jsondata) )
  }    
  
  protected def addPersonCmd(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    val vtp = jsonToPerson(value)
    logger.info("addPersonCmd about to add person="+vtp.toString()) 
    //insert it into the persons database table 
    insertPerson(vtp)
    //send back a status
    val retStaus = ("inserted" -> true)
    func.send(retStaus)
  }
  
  protected def updatePersonCmd(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    val vtp = jsonToPerson(value)
    logger.debug("updatePersonCmd about to update person="+vtp.toString())  
    updatePerson(vtp)
    //send back a status
    val retStaus = ("updated" -> true)
    func.send(retStaus)    
  } 
  
  protected def deletePersonCmd(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    val vtp = jsonToPerson(value)
    logger.debug("deletePersonCmd about to delete person="+vtp.toString())  
    deletePerson(vtp)
    //send back a status
    val retStaus = ("deleted" -> true)
    func.send(retStaus)    
  }   
  
  private def jsonToPerson(value:JValue) : Person = {
    implicit val formats = DefaultFormats 
    val pval = parse(value.extract[String]) transformField {
      case JField("age", JString(s))  => JField("age", JInt(s.toInt))
      case JField("id", JString(s))  => JField("id", JInt(s.toInt))
    }
    //Return the case class
    Person((pval \ "id").extract[Int],(pval \ "name").extract[String],(pval \ "age").extract[Int])
  }    
  
  private val roundtrips : List[RoundTripInfo] = List("personsQuery" -> personsQuery _,                                                
                                                      "addPersonCmd" -> addPersonCmd _,
                                                      "updatePersonCmd" -> updatePersonCmd _,
                                                      "deletePersonCmd" -> deletePersonCmd _)
  abstract override def getRoundTrips = super.getRoundTrips ++ roundtrips  
}