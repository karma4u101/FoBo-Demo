package code.snippet

import java.text.SimpleDateFormat
import java.util.Date

import net.liftweb.util.Helpers._
import net.liftweb.util._
import code.lib.BuildInfo

class CacheBust {
  lazy val buildTime = BuildInfo.buildTime
  lazy val date: Date = new Date(buildTime)
  lazy val formatter = new SimpleDateFormat("yyMMddHHmmss")
  lazy val strDate: String = formatter.format(date)

  def usingBuildTime:CssSel = "link [href+]" #> s"?bt=$strDate" & "script [src+]" #> s"?bt=$strDate"

}
