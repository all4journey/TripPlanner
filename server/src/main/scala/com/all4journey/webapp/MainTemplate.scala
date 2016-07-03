package com.all4journey.webapp

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
        titleTag(s"All4Journey - $titleText"),
        scriptPath("https://cdn.withcopper.com/1/copper.js"),
        scriptPath("/client-fastopt.js"),
        scriptPath("/client-jsdeps.js"),
        scriptPath("/assets/bootstrap/bootstrap.min.js"),
        css("/assets/bootstrap/bootstrap.min.css"),
        header
      ),
      body()(
        div(cls := "navbar navbar-inverse navbar-fixed-top", role := "navigation")(
            ul(cls := "nav navbar-nav")(
              menuItem("Main", "/")
            ),
            ul(cls := "nav navbar-nav navbar-right")(
              // menuItem("Profile", "/multiformProfile/personal"), // TODO this only displays if there is a user session established
              menuItem("Sign in", "/login", "glyphicon-log-in"), // TODO this only displays if there is no user session
              menuItem("Sign up", "javascript:Copper.login({ application_id: '57786660483D692158904B8B8E3B61E6518E2BBC', scope: 'name, phone' })", "glyphicon-pencil") // TODO this only displays if there is no user session
            /*
              TODO once there is a user session established, this needs to display throughout the session
              menuItem("Sign out", "/logout", "glyphicon-log-out")
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

  def menuItem(name: String, hrefPath: String)(implicit ctx: RequestContext) =
    li(
      if (ctx.request.uri.path.toString == hrefPath) cls := "menu active"
      else cls := "menu",
      a(href := hrefPath, name)
    )

  def menuItem(name: String, hrefPath: String, glyphIcon: String)(implicit ctx: RequestContext) =
    li(
      if (ctx.request.uri.path.toString == hrefPath) cls := "menu active"
      else cls := "menu",
      a(href := hrefPath)(
        span(cls := s"glyphicon $glyphIcon", paddingRight := "10px")(),
        strong()(name)
      )
    )
}
