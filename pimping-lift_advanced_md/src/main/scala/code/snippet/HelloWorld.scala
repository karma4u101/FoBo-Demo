package code 
package snippet 

import scala.xml.{NodeSeq, Text}
import net.liftweb.util._
import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._

import code.model._
import scravatar.{Gravatar,DefaultImage}

class HelloWorld {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  // replace the contents of the element with id "time" with the date
  def howdy = "#time *" #> date.map(_.toString)

  def usergrav = "#gravatar *" #>  userDDLabel    
  
    def userDDLabel:NodeSeq = { 
      def gravatar:NodeSeq = {
        val gurl = Gravatar(User.currentUser.map(u => u.email.get).openOrThrowException("Something wicked happened #1")).size(50).avatarUrl
        <img class="gravatar" src={gurl}/> 
      }      
      lazy val username = User.currentUser.map(u => u.firstName + " "+ u.lastName)
      User.loggedIn_? match {
        case true =>  <xml:group>{gravatar}  <md-tooltip>{username.openOrThrowException("Something wicked happened")}</md-tooltip></xml:group> 
        case _ => <xml:group>{""}</xml:group>   
      }
    }
    
  /*
   lazy val date: Date = DependencyFactory.time.vend // create the date via factory

   def howdy = "#time *" #> date.toString
   */
}

