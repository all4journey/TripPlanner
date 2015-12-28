package com.tripPlanner.webapp.util

import akka.http.scaladsl.marshalling._
import akka.http.scaladsl.model.{MediaTypes, ContentType}

import scalacss._
import scalacss.mutable.StyleSheet
import scalatags.Text.TypedTag

trait ScalaTagsSupport {
  val htmlContentType = MediaTypes.`text/html`

  implicit def scalaTagsMarshaller: ToEntityMarshaller[TypedTag[String]] =
    Marshaller.StringMarshaller.wrap(htmlContentType)(_.toString())
}

trait ScalaCssSupport {
  val cssContentType = MediaTypes.`text/css`

  // See https://github.com/japgolly/scalacss/issues/47
  implicit val inOrderRenderer = new Renderer[String] {
    override def apply(css: Css): String = {
      val sb = new StringBuilder
      val fmt = StringRenderer.formatPretty()(sb)
      css.foreach { case CssEntry(mq, sel, content) =>
        mq.foreach(fmt.mqStart)
        fmt.selStart(mq, sel)
        fmt.kv1(mq, content.head)
        content.tail.foreach(fmt.kvn(mq, _))
        fmt.selEnd(mq, sel)
        mq.foreach(fmt.mqEnd)
      }
      fmt.done()
      sb.toString()
    }
  }
  implicit val env = Env.empty

  implicit def scalaCssMarshaller: ToEntityMarshaller[StyleSheet.Base] =
    Marshaller.StringMarshaller.wrap(cssContentType)(_.render)
}

object ScalaTagsSupport extends ScalaTagsSupport
object ScalaCssSupport extends ScalaCssSupport
