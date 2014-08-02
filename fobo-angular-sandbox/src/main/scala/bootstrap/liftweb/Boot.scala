package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._
import mapper.{DB,StandardDBVendor,Schemifier}


import code.model._
import net.liftmodules.FoBo


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
    SlickHelper.demoRun
    
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
    //FoBo.InitParam.JQuery=FoBo.JQuery1111 //FoBo.JQuery1102  
    FoBo.InitParam.JQuery=FoBo.JQuery1111 //FoBo.JQuery191  
    //FoBo.InitParam.JQuery=FoBo.JQueryMigrate121    
    FoBo.InitParam.ToolKit=FoBo.Bootstrap320
    FoBo.InitParam.ToolKit=FoBo.FontAwesome410
    FoBo.InitParam.ToolKit=FoBo.AngularJS1219
    FoBo.InitParam.ToolKit=FoBo.AJSNGGrid207
    FoBo.InitParam.ToolKit=FoBo.AJSUIBootstrap0100
    FoBo.InitParam.ToolKit=FoBo.PrettifyJun2011
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
     
    //index affix sidbar links
    val dtop  = Menu(Loc("dtop"      ,Link(List("dtop")      ,true, "#dtop")         , null   ))
    val sbg1  = Menu(Loc("sbg1"      ,Link(List("sbg1")      ,true, "#section-1")         , S.loc("section-1"     , Text("FoBo Lift/Angular setup"))   ))

    val g1ex1 = Menu(Loc("g1ex1"     ,Link(List("g1ex1")     ,true, "#section-1-1")     , S.loc("g1ex1"     , Text("Dependency settings")),LocGroup("sbg1")       ))
    val g1ex2 = Menu(Loc("g1ex2"     ,Link(List("g1ex2")     ,true, "#section-1-2")     , S.loc("g1ex2"     , Text("Lift Boot")),LocGroup("sbg1")       ))
    val g1ex3 = Menu(Loc("g1ex3"     ,Link(List("g1ex3")     ,true, "#section-1-3")     , S.loc("g1ex3"     , Text("Lift default template")),LocGroup("sbg1")       ))
//    val g1ex4 = Menu(Loc("g1ex4"     ,Link(List("g1ex4")     ,true, "#section-1-4")     , S.loc("g1ex4"     , Text("Example 1.4:")),LocGroup("sbg1")       ))

    val sbg2  = Menu(Loc("sbg2"      ,Link(List("sbg2")      ,true, "#section-2")       , S.loc("sbg2"      , Text("Section 2"))   ))
    val g2ex1 = Menu(Loc("g2ex1"     ,Link(List("g2ex1")     ,true, "#section-2-1")     , S.loc("g2ex1"     , Text("Example 2.1:")),LocGroup("sbg2")       ))
    val g2ex2 = Menu(Loc("g2ex2"     ,Link(List("g2ex2")     ,true, "#section-2-2")     , S.loc("g2ex2"     , Text("Example 2.2:")),LocGroup("sbg2")       ))
    val g2ex3 = Menu(Loc("g2ex3"     ,Link(List("g2ex3")     ,true, "#section-2-3")     , S.loc("g2ex3"     , Text("Example 2.3:")),LocGroup("sbg2")       ))
    val g2ex4 = Menu(Loc("g2ex4"     ,Link(List("g2ex4")     ,true, "#section-2-4")     , S.loc("g2ex4"     , Text("Example 2.4:")),LocGroup("sbg2")       ))
    val g2ex5 = Menu(Loc("g2ex5"     ,Link(List("g2ex5")     ,true, "#section-2-5")     , S.loc("g2ex5"     , Text("Example 2.5:")),LocGroup("sbg2")       ))
    
        
    def sitemap = SiteMap(
        home          >> LocGroup("lg1"),
      /*  static, */
       
        FLTDemo,
        Slick,
        AngularJS,

        FontAwesome,
        dtop,    
        sbg1,
        g1ex1,
        g1ex2,
        g1ex3,
        sbg2,
        g2ex1,
        g2ex2,
        g2ex3,
        g2ex4,        
        g2ex5        
        /*,
        ddLabel1      >> LocGroup("topRight") >> PlaceHolder submenus (
            divider1  >> FoBo.TBLocInfo.Divider >> userMenu
            ) */
        
         ) 
  }
  
}
