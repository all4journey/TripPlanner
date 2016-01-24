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
        scriptPath("/client-fastopt.js"),
        scriptPath("/client-jsdeps.js"),
        scriptPath("/assets/bootstrap/bootstrap.min.js"),
        css("/assets/bootstrap/bootstrap.min.css"),
        header
      ),
      body()(
        div(cls := "navbar navbar-inverse navbar-fixed-top", role := "navigation")(
            ul(cls := "nav navbar-nav")(
              menuItem("Main", "/"),
              menuItem("Profile", "/multiformProfile/personal")
            )
        ),
        div(cls := "container-fluid")(
          div(cls := "main", paddingTop:="50px")(
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
