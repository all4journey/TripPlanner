package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.tripPlanner.domain.UserDaoImpl
import com.tripPlanner.shared.domain.Profile
import com.tripPlanner.webapp.Page
import com.tripPlanner.webapp.util.DomainSupport
import com.typesafe.scalalogging.LazyLogging
import prickle.Unpickle

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success



/**
  * Created by aabreu on 12/6/15.
  */
trait ProfilePage extends Page with LazyLogging{
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
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
                val userDao = UserDaoImpl(DomainSupport.db)
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