package tripPlanner.webapp

import org.scalajs.dom.raw.HTMLScriptElement

import scala.scalajs.js
import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document
import org.scalajs.jquery.jQuery

import scala.scalajs.js.annotation.JSExport

/**
  * Created by rjkj on 11/21/15.
  */
object GoogleMapsOld {
  def loadScript = {
    val script = document.createElement("script").asInstanceOf[HTMLScriptElement]
    script.`type` = "text/javascript"
    script.src = "https://maps.googleapis.com/maps/api/js?key=AIzaSyCgGhSLguWAvTEzvLkud08lU31bXtaWBuM&callback=initializeSample"
    document.head.appendChild(script)

  }

  def loadMap(lat:Double, long:Double) = {
    val script = document.createElement("script").asInstanceOf[HTMLScriptElement]
    script.`type` = "text/javascript"
    script.text = s"function initialize() {\n  " +
      "var mapProp = {\n" +
      s"center:new google.maps.LatLng($lat,$long),\n    " +
      "zoom:8,\n    " +
      "mapTypeId:google.maps.MapTypeId.ROADMAP\n  " +
      "};\n  " +
      "var map=new google.maps.Map(document.getElementById(\"googleMap\"),mapProp);\n" +
      "}\n" +
      "google.maps.event.addDomListener(window, 'load', initialize);"

    document.body.appendChild(script)
  }

}

@JSExport("initializeSample")
object Initializer {
  println("map loaded successfully")
}
