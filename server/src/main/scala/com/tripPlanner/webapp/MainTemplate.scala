package com.tripPlanner.webapp

import akka.http.scaladsl.server.RequestContext

import scala.scalajs.niocharset.StandardCharsets
import scalatags.Text.all._

object MainTemplate extends View {
  def apply(
             titleText: String,
             header: Seq[Modifier] = Seq.empty,
             content: Seq[Modifier] = Seq.empty,
             footer: Seq[Modifier] = Seq.empty
           )
           (implicit ctx: RequestContext)
  =
    html(
      head(
        meta(charset := StandardCharsets.UTF_8.name.toLowerCase),
        titleTag(s"Trip Planner - $titleText"),
        scriptPath("client-fastopt.js"),
        scriptPath("client-jsdeps.js"),
        scriptPath("/assets/bootstrap/bootstrap.min.js"),
        css("/assets/bootstrap/bootstrap.min.css"),
        header
      ),
      body(
        div(cls := "navbar navbar-inverse navbar-fixed-top", role := "navigation")(
        ),
        div(cls := "container-fluid")(
          div(cls := "col-sm-3 col-md-2 sidebar")(
            ul(cls := "nav nav-sidebar")(
              menuItem("Main", "/"),
              menuItem("Page 2", "/page2")
            )
          ),
          div(cls := "col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main")(
            h1(cls := "page-header", titleText),
            div(id := "content"),
            div(content)
          )
        ),
        footer
      )
    )

  def menuItem(name: String, hrefPath: String)(implicit ctx: RequestContext) =
    li(
      if (ctx.request.uri.path.toString == hrefPath) cls := "menu active"
      else cls := "menu",
      a(href := hrefPath, name)
    )
}
