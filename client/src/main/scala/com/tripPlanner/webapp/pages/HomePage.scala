package com.tripPlanner.webapp.pages

import org.scalajs.dom

import scalatags.JsDom._
import scalatags.JsDom.all._
import scalatags.JsDom.short.*

/**
  * Created by rjkj on 12/1/15.
  */
object HomePage {
  def renderMap: TypedTag[dom.raw.HTMLElement] = div(*.`class`:="content" ){
    "HelloWorld"
  }
}
