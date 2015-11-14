package tripPlanner.webapp

import com.travelPlanner.domain.User

import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import org.scalajs.jquery.jQuery

object App extends JSApp{
  def main():Unit = {
    val user = User(
      "Robert"
    )
    jQuery("body").append("<p>Hello World</p>")
    jQuery("body").append(user.name)
  }

}
