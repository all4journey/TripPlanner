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
//           (implicit ctx: RequestContext)
  =
    html(
      head(
        meta(charset := StandardCharsets.UTF_8.name.toLowerCase),
        titleTag(s"Trip Planner - $titleText"),
        scriptPath("client-fastopt.js"),
        scriptPath("client-jsdeps.js"),
        header
      ),
      body(
        content
      )
    )
}
