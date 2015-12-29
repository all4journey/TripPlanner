package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.shared.domain.{Profile, Address, State, Vehicle}
import com.tripPlanner.webapp.{logger, Page}

import com.tripPlanner.domain.UserDao

import prickle.Unpickle
import akka.http.scaladsl.model.StatusCodes
import scala.util.Success
import scala.util.Try
import com.typesafe.scalalogging.LazyLogging


/**
  * Created by aabreu on 12/6/15.
  */
trait ProfilePage extends Page with LazyLogging {
  def apply()(implicit sys: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {
        logger.info("Hello Logging")
        complete(ProfileView())
      }
      }
    } ~
      post {
        extractRequestContext { implicit ctx =>
          entity(as[String]) { profileJsonPayload =>
            Unpickle[Profile].fromString(profileJsonPayload) match {
              case Success(userInfo: Profile) => {
                val userDao = new UserDao()
                userDao.save(userInfo.user)
                complete(s"This following info was obtained from the form input\n" +
                  "****Personal Info**** \n" +
                  s"Last Name := '${userInfo.user.lName}', First Name := '${userInfo.user.fName}'\n" +
                  s"Time Zone := '${userInfo.addresses(0).state.timezone.id}'\n" +
                  s"Street Address := '${userInfo.addresses(0).street}'\n" +
                  s"State := '${userInfo.addresses(0).state.id}', Zip code := '${userInfo.addresses(0).zipCode}'\n" +
                  "****Vehicle Info****\n" +
                  s"Year := '${userInfo.vehicles(0).year}', Make := '${userInfo.vehicles(0).make}', Model := '${userInfo.vehicles(0).model}'")
              }
              case _ => complete(StatusCodes.BadRequest)
            }

          }
        }
      }
  }
}

object ProfilePage extends ProfilePage