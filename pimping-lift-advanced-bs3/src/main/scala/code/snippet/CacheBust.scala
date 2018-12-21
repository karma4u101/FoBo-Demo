package code.snippet

import net.liftweb.util._
import java.util.Date
import java.text.SimpleDateFormat
import code.lib.BuildInfo
import Helpers._

class CacheBust {
  lazy val buildTime = BuildInfo.buildTime
  lazy val date: Date = new Date(buildTime)
  lazy val formatter = new SimpleDateFormat("yyMMddHHmmss")
  lazy val strDate: String = formatter.format(date)

  def usingBuildTime:CssSel = "link [href+]" #> s"?bt=$strDate" & "script [src+]" #> s"?bt=$strDate"

}
