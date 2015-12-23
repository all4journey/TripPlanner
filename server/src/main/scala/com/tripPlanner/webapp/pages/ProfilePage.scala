package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.domain.Profile
import com.tripPlanner.webapp.Page
import prickle.Unpickle
import akka.http.scaladsl.model.StatusCodes
import scala.util.Success
import scala.util.Try

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
        entity(as[String]) { profileJsonPayload =>
          Unpickle[Profile].fromString(profileJsonPayload) match {
            case Success(userInfo) => complete(s"This following info was obtained from the form input\n" +
              "****Personal Info**** \n" +
              s"Last Name := '${userInfo.lastName}', First Name := '${userInfo.firstName}'\n" +
              s"Company := '${userInfo.company}'\n" +
              s"Time Zone := '${userInfo.userTimezone}'\n" +
              s"Street Address := '${userInfo.streetAddress}'\n" +
              s"State := '${userInfo.userState}', Zip code := '${userInfo.zipCode}'\n" +
              "****Vehicle Info****\n" +
              s"Year := '${userInfo.userVehicleYear}', Make := '${userInfo.make}', Model := '${userInfo.model}'")
            case _ => complete(StatusCodes.BadRequest)
          }

        }
      }
    }
  }
}

object ProfilePage extends ProfilePage