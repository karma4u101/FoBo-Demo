package code.snippet

//import code.snippet.SimpleRoundTrips
import scala.xml._
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE.JsRaw

/*This snippet is injecting the roundtrip binding scripts into the tail of the html page body.*/
class RoundTripBindingInjector extends MyAngularJSDemoRoundTrips {
  
  def render() : NodeSeq = {
    val functions = ((for {
      session <- S.session
    } yield <lift:tail>{Script(
        JsRaw(s"var myRTFunctions = ${session.buildRoundtrip(getRoundTrips).toJsCmd}").cmd
        )}</lift:tail>) openOr NodeSeq.Empty)
    functions
  }  

}