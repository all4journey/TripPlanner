package com.all4journey.webapp

import akka.http.scaladsl.server.RequestContext

import scala.scalajs.niocharset.StandardCharsets
import scalatags.Text.all._
import scalatags.generic.PixelStyle

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
        titleTag(s"All4Journey - $titleText"),
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
            ),
            ul(cls := "nav navbar-nav navbar-right")(
              menuItem("Sign in", "/login"), // TODO this only displays if there is no user session
              menuItem("Sign up", "/signUp", Some("padding-right: 10px")) // TODO this only displays if there is no user session
            /*
              TODO once there is a user session established, this needs to display throughout the session
              menuItem("Sign out", "/signOut", Some("padding-right: 10px"))
             */
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

  def menuItem(name: String, hrefPath: String, customStyle: Option[String] = None)(implicit ctx: RequestContext) = {
    li(
      if (ctx.request.uri.path.toString == hrefPath) cls := "menu active"
      else cls := "menu",
      a(href := hrefPath, name),
      style := customStyle.getOrElse("")
    )
  }
}
