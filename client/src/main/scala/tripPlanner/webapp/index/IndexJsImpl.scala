package tripPlanner.webapp.index

import com.tripPlanner.webapp.index.IndexJs
import scala.scalajs.js.Dynamic.global

/**
  * Created by rjkj on 12/5/15.
  */
object IndexJsImpl extends IndexJs {
  def run(): Unit = global.document.getElementById("content").textContent = "HelloWorld"
}
