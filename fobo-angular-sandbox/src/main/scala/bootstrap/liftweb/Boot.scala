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

    //Init slick tables 
    SlickHelper.initSchema 
    //MySlickHelper.demoRun
    
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
    FoBo.InitParam.JQuery=FoBo.JQuery1102  
    FoBo.InitParam.ToolKit=FoBo.Bootstrap301
    FoBo.InitParam.ToolKit=FoBo.FontAwesome401
    FoBo.InitParam.ToolKit=FoBo.AngularJS122
    FoBo.InitParam.ToolKit=FoBo.AJSNGGrid207
    FoBo.InitParam.ToolKit=FoBo.AJSUIBootstrap070
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
    
//    val divider1   = Menu("divider1") / "divider1"
//    val ddLabel1   = Menu.i("UserDDLabel") / "ddlabel1"
    val home       = Menu.i("Home") / "index" 
//    val userMenu   = User.AddUserMenusHere
    
//    val static     = Menu(Loc("Static", 
//        Link(List("static"), true, "/static/index"), 
//        S.loc("StaticContent" , scala.xml.Text("Static Content")),
//        LocGroup("lg2","topRight") ))
//
    val twbs       = Menu(Loc("Bootstrap3", 
        Link(List("bootstrap301"), true, "/bootstrap301/index"), 
        S.loc("Bootstrap3" , scala.xml.Text("Bootstrap3")),
        LocGroup("lg2"),
        FoBo.TBLocInfo.LinkTargetBlank ))
        
    val AngularJS       = Menu(Loc("AngularJS", 
        ExtLink("http://angularjs.org/"), 
        S.loc("AngularJS" , scala.xml.Text("AngularJS")),
        LocGroup("lg2"),
        FoBo.TBLocInfo.LinkTargetBlank  )) 
        
   val Slick       = Menu(Loc("Slick", 
        ExtLink("http://slick.typesafe.com/"), 
        S.loc("Slick" , scala.xml.Text("Slcik")),
        LocGroup("lg2"),
        FoBo.TBLocInfo.LinkTargetBlank  )) 
        
   val FontAwesome       = Menu(Loc("FontAwesome", 
        ExtLink("http://fontawesome.io/"), 
        S.loc("FontAwesome" , scala.xml.Text("Font Awesome")),
        LocGroup("lg2"),
        FoBo.TBLocInfo.LinkTargetBlank  ))         
        
    val FLTDemo       = Menu(Loc("FLTDemo", 
        ExtLink("http://www.media4u101.se/fobo-lift-template-demo/"), 
        S.loc("FLTDemo" , scala.xml.Text("FoBo Lift Template Demo")),
        LocGroup("lg2")/*,
        FoBo.TBLocInfo.LinkTargetBlank */ ))        
     
        //
        
    def sitemap = SiteMap(
        home          >> LocGroup("lg1"),
      /*  static, */
       
        FLTDemo ,
        Slick,
        AngularJS,
        twbs, 
        FontAwesome
        /*,
        ddLabel1      >> LocGroup("topRight") >> PlaceHolder submenus (
            divider1  >> FoBo.TBLocInfo.Divider >> userMenu
            ) */
         ) 
  }
  
}
