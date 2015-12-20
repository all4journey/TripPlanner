package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.webapp.Page

/**
  * Created by aabreu on 12/6/15.
  */
trait ProfilePage extends Page {
  def apply()(implicit sys:ActorSystem, mat:Materializer) = pathEnd{
    get{
      extractRequestContext { implicit ctx =>
        complete(ProfileView())
      }
    }~
    post {
      extractRequestContext { implicit ctx =>
        formFields('firstName, 'lastName, 'company, 'userTimezone, 'streetAddress, 'userState, 'zipCode, 'userVehicleYear, 'make, 'model) { (firstName, lastName,
        company, userTimezone, streetAddress, userState, zipCode, year, make, model) =>
          complete(s"This following info was obtained from the form input\n" +
            "****Personal Info**** \n" +
            s"Last Name := '$lastName', First Name := '$firstName'\n" +
            s"Company := '$company'\n" +
            s"Time Zone := '$userTimezone'\n" +
            s"Street Address := '$streetAddress'\n" +
            s"State := '$userState', Zip code := '$zipCode'\n" +
            "****Vehicle Info****\n" +
            s"Year := '$year', Make := '$make', Model := '$model'")
        }
      }
    }
  }
}

object ProfilePage extends ProfilePage