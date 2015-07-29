package code.snippet

//import code.snippet.SimpleRoundTrips
import scala.xml._
import net.liftweb.http._
import net.liftweb.http.js.JsCmds._
import net.liftweb.http.js.JE.JsRaw

/*This snippet is injecting the roundtrip binding scripts into the tail of the html page body.*/
class RoundTripBindingInjector extends PersonRT with SimpleRT {
  
  /*
   * This is a quick fix to move the scripts out of the page and into “page JS” file instead
   * There are most likely a more elegant solution. 
   */
  def render() : NodeSeq = {
    val functions = ((for {
      session <- S.session
    } yield 
       <lift:tail>{
       S.appendGlobalJs(JsRaw(s"var myRTFunctions = ${session.buildRoundtrip(getRoundTrips).toJsCmd}").cmd)
       }</lift:tail>
    ) openOr NodeSeq.Empty)
    functions  
  }
  
  /*
  def render() : NodeSeq = {
    val functions = ((for {
      session <- S.session
    } yield <lift:tail>{Script(
        JsRaw(s"var myRTFunctions = ${session.buildRoundtrip(getRoundTrips).toJsCmd}").cmd
        )}</lift:tail>) openOr NodeSeq.Empty)
    functions
  }  
  */
}