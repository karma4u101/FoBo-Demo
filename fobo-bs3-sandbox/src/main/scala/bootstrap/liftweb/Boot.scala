package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._
import mapper._

import code.model._
import net.liftmodules.{FoBo}


/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor = 
	new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url") openOr 
			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(DefaultConnectionIdentifier, vendor)
    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User)

    // where to search snippet
    LiftRules.addToPackages("code")


    def sitemapMutators = User.sitemapMutator
    //The SiteMap is built in the Site object bellow 
    LiftRules.setSiteMapFunc(() => sitemapMutators(Site.sitemap))

    //Init the FoBo - Front-End Toolkit module, 
    //see http://liftweb.net/lift_modules for more info
    FoBo.InitParam.JQuery=FoBo.JQuery191  
    FoBo.InitParam.ToolKit=FoBo.Bootstrap300 
    FoBo.InitParam.ToolKit=FoBo.FontAwesome401
    FoBo.init() 
    
    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)
    
    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))    
      
    LiftRules.noticesAutoFadeOut.default.set( (notices: NoticeType.Value) => {
        notices match {
          case NoticeType.Notice => Full((8 seconds, 4 seconds))
          case _ => Empty
        }
     }
    ) 
    
    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
  }
  
  object Site {
    import scala.xml._
    val divider1   = Menu("divider1") / "divider1"
    val ddLabel1   = Menu.i("UserDDLabel") / "ddlabel1"
    val home       = Menu.i("Home") / "index" 
    val userMenu   = User.AddUserMenusHere
    val static     = Menu(Loc("Static", Link(List("static"), true, "/static/index"), S.loc("StaticContent" , scala.xml.Text("Static Content")),LocGroup("lg2","topRight")))
    val twbs       = Menu(Loc("Bootstrap3", Link(List("bootstrap3"), true, "/bootstrap3/index"), S.loc("Bootstrap3" , scala.xml.Text("Bootstrap3")),LocGroup("lg2")))

    //list-group menu example with collapsible sub menus
    val sbL1Dashbord      = Menu(Loc("sbL1Dashbord"     ,Link(List("sbL1Dashbord")     ,true, "#sbL1Dashbord")     , S.loc("sbL1Dashbord"     , Text("Dashboard"))       ))
    val sbL1Users         = Menu(Loc("sbL1Users"        ,Link(List("sbL1Users")        ,true, "#users")            , S.loc("sbL1Users"        , Text("Users"))           ))
    val sbL2Users         = Menu(Loc("sbL2Users"        ,Link(List("sbL2Users")        ,true, "#sbL2Users")        , S.loc("sbL2Users"        , Text("Users"))           ))
    val sbL2CreateUsr     = Menu(Loc("sbL2CreateUsr"    ,Link(List("sbL2CreateUsr")    ,true, "#sbL2CreateUsr")    , S.loc("sbL2CreateUsr"    , Text("Create User"))     ))
    val sbL2CreateGrp     = Menu(Loc("sbL2CreateGrp"    ,Link(List("sbL2CreateGrp")    ,true, "#sbL2CreateGrp")    , S.loc("sbL2CreateGrp"    , Text("Create Group"))    ))
    val sbL1Articles      = Menu(Loc("sbL1Articles"     ,Link(List("sbL1Articles")     ,true, "#articles")         , S.loc("sbL1Articles"     , Text("Articles"))           ))
    val sbL2Articles      = Menu(Loc("sbL2Articles"     ,Link(List("sbL2Articles")     ,true, "#sbL2Articles")     , S.loc("sbL2Articles"     , Text("Articles"))        ))
    val sbL2CreateArticle = Menu(Loc("sbL2CreateArticle",Link(List("sbL2CreateArticle"),true, "#sbL2CreateArticle"), S.loc("sbL2CreateArticle", Text("Create Article"))  ))
   
    def sitemap = SiteMap(
        home          >> LocGroup("lg1"),
        static,
        twbs,
        sbL1Dashbord,
        sbL1Users,
        sbL2Users,
        sbL2CreateUsr,
        sbL2CreateGrp,
        sbL1Articles,
        sbL2Articles,
        sbL2CreateArticle,
        ddLabel1      >> LocGroup("topRight") >> PlaceHolder submenus (
            divider1  >> FoBo.TBLocInfo.Divider >> userMenu
            )
         )
  }
  
}
