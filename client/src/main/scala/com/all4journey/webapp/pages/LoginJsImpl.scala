package com.all4journey.webapp.pages

import com.all4journey.shared.domain.security.LoginCredentials
import org.scalajs.dom
import org.scalajs.jquery.{jQuery => $, JQueryXHR, JQueryAjaxSettings}
import prickle.Pickle
import scala.scalajs.js
import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

// $COVERAGE-OFF$
object LoginJsImpl extends LoginJs{
  override def run(): Unit = {
    val content = dom.document.getElementById("content")
    content.appendChild(loginForm.render)
  }

  def loginForm = div(cls:="container")(
    form(cls:="form-horizontal", role := "form")(
      div(cls:="form-group")(
        label(cls:="col-lg-3 control-label")("Email address:"),
        div(cls := "col-lg-8")(
          input(id := "userName", name := "userName", cls:="form-control", `type` := "email")
        )
      ),
      div(cls:="form-group", role:="form")(
        label(cls := "col-lg-3 control-label")("Password:"),
        div(cls:="col-lg-8")(
          input(id := "password", name := "password", cls:="form-control", `type` := "password")
        )
      ),
      div(cls:="form-group")(
        label()(),
        div(cls:="col-md-8")(
          input(id :="loginButton", `type`:="button", cls:="btn btn-primary", value := "Login", onclick := {() =>
            val userName:String = $("#userName").value().toString.trim
            val password = $("#password").value().toString.trim
            val loginCredentials = LoginCredentials(userName, password)
            val pickledLogin = Pickle.intoString(loginCredentials)
            $.ajax(js.Dynamic.literal(
              url = "login",
              `type` = "post",
              data = pickledLogin,
              contentType = "application/json; charset=utf-8",
              traditional = true,
              success = { (data:js.Any, jqXHR: JQueryXHR) =>
                val content = dom.document.getElementById("content")
                content.appendChild(p(s"$data").render)
              },
              failure = {
                (data:js.Any, jqXHR: JQueryXHR) =>
                  val content = dom.document.getElementById("content")
                  content.appendChild(p("Username or Password incorrect").render)
              },
              error = {(qXHR: JQueryXHR, textStatus: String, errorThrow: String) =>
                textStatus match {
                  case _ =>
                    val content = dom.document.getElementById("content")
                    content.appendChild(p(s"$textStatus").render)
                }

              }
            ).asInstanceOf[JQueryAjaxSettings])
          })
        )
      )
    )
  )
}
// $COVERAGE-ON$
