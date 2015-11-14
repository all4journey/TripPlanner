package tripPlanner.webapp

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import org.scalajs.jquery.jQuery

object App extends JSApp{
  def main():Unit = {
    jQuery("body").append("<p>Hello World</p>")
  }

}
