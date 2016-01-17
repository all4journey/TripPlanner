package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.tripPlanner.domain._
import com.tripPlanner.shared.domain.{Profile, State}
import com.tripPlanner.webapp.Page
import com.tripPlanner.webapp.util.DomainSupport
import com.typesafe.scalalogging.LazyLogging
import prickle.Unpickle

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Await, Future}
import scala.util.Success
import scala.concurrent.duration._


/**
  * Created by aabreu on 12/6/15.
  */

trait AjaxProfilePage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {


        complete(AjaxProfileView())
      }
      }
    }
  }
}

object AjaxProfilePage extends AjaxProfilePage

trait ProfilePage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {

        val states = ProfileLogic.getStates

        val myList = Await.result(states, 10 seconds) //TODO: look into removing Await

        val profileView = new ProfileView(myList)
        complete(profileView.apply())
      }
      }
    } ~
      post {
        extractRequestContext { implicit ctx =>
          entity(as[String]) { profileJsonPayload =>
            Unpickle[Profile].fromString(profileJsonPayload) match {
              case Success(profileInfo: Profile) =>
                ProfileLogic.save(profileInfo)
                complete(StatusCodes.OK)

              case _ => complete(StatusCodes.BadRequest)
            }

          }
        }
      }
  }
}

object ProfilePage extends ProfilePage

object ProfileLogic extends LazyLogging {
  def save(profileInfo: Profile): Unit = {
    val userDao = UserDao(DomainSupport.db)
    val userIdFuture = userDao.create(profileInfo.user)

    val addressDao = AddressDao(DomainSupport.db)

    val vehicleDao = VehicleDao(DomainSupport.db)

    userIdFuture.onSuccess {
      case result =>
        result match {
          case Some(id) =>
            //TODO - Set limit on vehicles and Addresses
            profileInfo.addresses foreach (address => addressDao.create(address.copy(userId = id)))
            profileInfo.vehicles foreach (vehicle => vehicleDao.create(vehicle.copy(userId = id)))
          case None =>
          //Throw Error
        }

    }
  }

  def getStates: Future[Seq[State]] = {
    val stateDao = StateDaoImpl(DomainSupport.db)
    stateDao.getStates
  }
}
