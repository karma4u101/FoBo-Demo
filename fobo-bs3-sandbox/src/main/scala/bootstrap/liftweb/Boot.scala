package bootstrap.liftweb

import net.liftweb._
import util._
import Helpers._

import common._
import http._
import js.jquery.JQueryArtifacts
import sitemap._
import Loc._
//import mapper._
import mapper.{DB,StandardDBVendor,Schemifier}

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
    FoBo.InitParam.JQuery=FoBo.JQuery1110 //FoBo.JQuery191  
    FoBo.InitParam.ToolKit=FoBo.Bootstrap320 
    FoBo.InitParam.ToolKit=FoBo.FontAwesome410
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
    //val static     = Menu(Loc("Static", Link(List("static"), true, "/static/index"), S.loc("StaticContent" , scala.xml.Text("Static Content")),LocGroup("lg2","topRight")))
    val twbs       = Menu(Loc("Bootstrap3", Link(List("bootstrap3"), true, "/bootstrap3/index"), S.loc("Bootstrap3" , scala.xml.Text("Bootstrap3")),LocGroup("lg2")))

    val FLTDemo       = Menu(Loc("FLTDemo", 
        ExtLink("http://www.media4u101.se/fobo-lift-template-demo/"), 
        S.loc("FLTDemo" , scala.xml.Text("FoBo Lift Template Demo")),
        LocGroup("lg2","topRight")/*,
        FoBo.TBLocInfo.LinkTargetBlank */ ))      
    
    //list-group menu example with collapsible sub menus
    val sbL1Dashbord      = Menu(Loc("sbL1Dashbord"     ,Link(List("sbL1Dashbord")     ,true, "#sbL1Dashbord")     , S.loc("sbL1Dashbord"     , Text("Dashboard"))       ))
    val sbL1Users         = Menu(Loc("sbL1Users"        ,Link(List("sbL1Users")        ,true, "#users")            , S.loc("sbL1Users"        , Text("Users"))           ))
    val sbL2Users         = Menu(Loc("sbL2Users"        ,Link(List("sbL2Users")        ,true, "#sbL2Users")        , S.loc("sbL2Users"        , Text("Users"))           ))
    val sbL2CreateUsr     = Menu(Loc("sbL2CreateUsr"    ,Link(List("sbL2CreateUsr")    ,true, "#sbL2CreateUsr")    , S.loc("sbL2CreateUsr"    , Text("Create User"))     ))
    val sbL2CreateGrp     = Menu(Loc("sbL2CreateGrp"    ,Link(List("sbL2CreateGrp")    ,true, "#sbL2CreateGrp")    , S.loc("sbL2CreateGrp"    , Text("Create Group"))    ))
    val sbL1Articles      = Menu(Loc("sbL1Articles"     ,Link(List("sbL1Articles")     ,true, "#articles")         , S.loc("sbL1Articles"     , Text("Articles"))           ))
    val sbL2Articles      = Menu(Loc("sbL2Articles"     ,Link(List("sbL2Articles")     ,true, "#sbL2Articles")     , S.loc("sbL2Articles"     , Text("Articles"))        ))
    val sbL2CreateArticle = Menu(Loc("sbL2CreateArticle",Link(List("sbL2CreateArticle"),true, "#sbL2CreateArticle"), S.loc("sbL2CreateArticle", Text("Create Article"))  ))
   
    //index affix sidbar links
    val dtop  = Menu(Loc("dtop"      ,Link(List("dtop")      ,true, "#dtop")         , null   ))
    val sbg1  = Menu(Loc("sbg1"      ,Link(List("sbg1")      ,true, "#section-1")         , null   ))

    val g1ex1 = Menu(Loc("g1ex1"     ,Link(List("g1ex1")     ,true, "#section-1-1")     , S.loc("g1ex1"     , Text("Example 1.1: Collapsible menu")),LocGroup("sbg1")       ))
    val g1ex2 = Menu(Loc("g1ex2"     ,Link(List("g1ex2")     ,true, "#section-1-2")     , S.loc("g1ex2"     , Text("Example 1.2:")),LocGroup("sbg1")       ))
    val g1ex3 = Menu(Loc("g1ex3"     ,Link(List("g1ex3")     ,true, "#section-1-3")     , S.loc("g1ex3"     , Text("Example 1.3:")),LocGroup("sbg1")       ))
    val g1ex4 = Menu(Loc("g1ex4"     ,Link(List("g1ex4")     ,true, "#section-1-4")     , S.loc("g1ex4"     , Text("Example 1.4:")),LocGroup("sbg1")       ))

    val sbg2  = Menu(Loc("sbg2"      ,Link(List("sbg2")      ,true, "#section-2")       , S.loc("sbg2"      , Text("Section 2"))   ))
    val g2ex1 = Menu(Loc("g2ex1"     ,Link(List("g2ex1")     ,true, "#section-2-1")     , S.loc("g2ex1"     , Text("Example 2.1:")),LocGroup("sbg2")       ))
    val g2ex2 = Menu(Loc("g2ex2"     ,Link(List("g2ex2")     ,true, "#section-2-2")     , S.loc("g2ex2"     , Text("Example 2.2:")),LocGroup("sbg2")       ))
    val g2ex3 = Menu(Loc("g2ex3"     ,Link(List("g2ex3")     ,true, "#section-2-3")     , S.loc("g2ex3"     , Text("Example 2.3:")),LocGroup("sbg2")       ))
    val g2ex4 = Menu(Loc("g2ex4"     ,Link(List("g2ex4")     ,true, "#section-2-4")     , S.loc("g2ex4"     , Text("Example 2.4:")),LocGroup("sbg2")       ))
    val g2ex5 = Menu(Loc("g2ex5"     ,Link(List("g2ex5")     ,true, "#section-2-5")     , S.loc("g2ex5"     , Text("Example 2.5:")),LocGroup("sbg2")       ))

    val sbg3  = Menu(Loc("sbg3"      ,Link(List("sbg3")      ,true, "#section-3")       , S.loc("sbg3"      , Text("Section 3"))   ))
    val g3ex1 = Menu(Loc("g3ex1"     ,Link(List("g3ex1")     ,true, "#section-3-1")     , S.loc("g3ex1"     , Text("Example 3.1:")),LocGroup("sbg3")       ))
    val g3ex2 = Menu(Loc("g3ex2"     ,Link(List("g3ex2")     ,true, "#section-3-2")     , S.loc("g3ex2"     , Text("Example 3.2:")),LocGroup("sbg3")       ))
    val g3ex3 = Menu(Loc("g3ex3"     ,Link(List("g3ex3")     ,true, "#section-3-3")     , S.loc("g3ex3"     , Text("Example 3.3:")),LocGroup("sbg3")       ))
    val g3ex4 = Menu(Loc("g3ex4"     ,Link(List("g3ex4")     ,true, "#section-3-4")     , S.loc("g3ex4"     , Text("Example 3.4:")),LocGroup("sbg3")       ))
    val g3ex5 = Menu(Loc("g3ex5"     ,Link(List("g3ex5")     ,true, "#section-3-5")     , S.loc("g3ex5"     , Text("Example 3.5:")),LocGroup("sbg3")       ))
    
    val sbg4  = Menu(Loc("sbg4"      ,Link(List("sbg4")      ,true, "#section-4")       , S.loc("sbg4"      , Text("Section 4"))   ))
    val g4ex1 = Menu(Loc("g4ex1"     ,Link(List("g4ex1")     ,true, "#section-4-1")     , S.loc("g4ex1"     , Text("Example 4.1:")),LocGroup("sbg4")       ))
    val g4ex2 = Menu(Loc("g4ex2"     ,Link(List("g4ex2")     ,true, "#section-4-2")     , S.loc("g4ex2"     , Text("Example 4.2:")),LocGroup("sbg4")       ))
    val g4ex3 = Menu(Loc("g4ex3"     ,Link(List("g4ex3")     ,true, "#section-4-3")     , S.loc("g4ex3"     , Text("Example 4.3:")),LocGroup("sbg4")       ))
    val g4ex4 = Menu(Loc("g4ex4"     ,Link(List("g4ex4")     ,true, "#section-4-4")     , S.loc("g4ex4"     , Text("Example 4.4:")),LocGroup("sbg4")       ))
    val g4ex5 = Menu(Loc("g4ex5"     ,Link(List("g4ex5")     ,true, "#section-4-5")     , S.loc("g4ex5"     , Text("Example 4.5:")),LocGroup("sbg4")       ))
    
    
    def sitemap = SiteMap(
        home          >> LocGroup("lg1"),
        FLTDemo,
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
            ),
        dtop,    
        sbg1,
        g1ex1,
        g1ex2,
        g1ex3,
        g1ex4,
        sbg2,
        g2ex1,
        g2ex2,
        g2ex3,
        g2ex4,        
        g2ex5,
        sbg3,
        g3ex1,
        g3ex2,
        g3ex3,
        g3ex4,        
        g3ex5,
        sbg4,
        g4ex1,
        g4ex2,
        g4ex3,
        g4ex4,        
        g4ex5        
         )
  }
  
}
