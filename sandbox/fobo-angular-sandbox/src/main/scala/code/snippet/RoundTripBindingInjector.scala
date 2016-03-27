package code.snippet

import scala.xml._
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE.JsRaw

/*This snippet is injecting the roundtrip binding scripts into the tail of the html page body.*/
class RoundTripBindingInjector extends PersonRT with SimpleRT {
  
  /*
   * Appends the roundtrip binding scripts to Lift's page script file.
   */
  def render() : NodeSeq = {
    val functions = ((for {
      session <- S.session
    } yield {
       S.appendGlobalJs(JsRaw(s"var myRTFunctions = ${session.buildRoundtrip(getRoundTrips).toJsCmd}").cmd)
       NodeSeq.Empty
       }
    ) openOr NodeSeq.Empty)
    functions  
  }
  
}