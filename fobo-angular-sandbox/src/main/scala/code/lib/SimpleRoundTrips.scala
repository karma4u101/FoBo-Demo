package code.lib

//import code.lib.EmptyRoundTrip
import net.liftweb.json.JsonAST.{JDouble, JString, JArray, JValue}
import net.liftweb.http.{RoundTripInfo, RoundTripHandlerFunc}

trait SimpleRoundTrips extends EmptyRoundTrip {
  
  protected def doSimpleRT(value : JValue, func : RoundTripHandlerFunc) : Unit = {
    func.send(JString("There and back again!"))
  }
  
  protected def getNameAgeGridDataFromRT(value : JValue, func : RoundTripHandlerFunc) : Unit = {
     import net.liftweb.json.JsonDSL._
     case class Person(name:String,age:Int)
     val persons = List(Person("Tiancum",43),Person("Jacob",27),Person("Nephi",29),Person("Enos",34),Person("Peter",46))
     val jsondata = persons.map { p => (("name" -> p.name) ~ ("age" -> p.age))}
     func.send( JArray(jsondata) )
  } 
  
  
  private val roundtrips : List[RoundTripInfo] = List("doSimpleRT" -> doSimpleRT _, 
                                                      "getNameAgeGridDataFromRT" -> getNameAgeGridDataFromRT _)
  abstract override def getRoundTrips = super.getRoundTrips ++ roundtrips  
}