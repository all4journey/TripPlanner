package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.tripPlanner.domain.{VehicleDaoImpl, UserDaoImpl, AddressDaoImpl, StateDaoImpl}
import com.tripPlanner.shared.domain.{Profile, State}
import com.tripPlanner.webapp.Page
import com.tripPlanner.webapp.util.DomainSupport
import com.typesafe.scalalogging.LazyLogging
import prickle.{Unpickle, Pickle}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success



/**
  * Created by aabreu on 12/6/15.
  */
trait ProfilePage extends Page with LazyLogging{
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {
        val states = ProfileLogic.getStates()
        val profileView = new ProfileView(states)
        complete(profileView.apply())
      }
      }
    } ~
      post {
        extractRequestContext { implicit ctx =>
          entity(as[String]) { profileJsonPayload =>
            Unpickle[Profile].fromString(profileJsonPayload) match {
              case Success(profileInfo: Profile) => {
                ProfileLogic.save(profileInfo)
                complete(StatusCodes.OK)
              }
              case _ => complete(StatusCodes.BadRequest)
            }

          }
        }
      }
  }
}

object ProfilePage extends ProfilePage

object ProfileLogic {
  def save(profileInfo: Profile): Unit = {
    val userDao = UserDaoImpl(DomainSupport.db)
    userDao.save(profileInfo.user)

    val addressDao = AddressDaoImpl(DomainSupport.db)
    val addresses = profileInfo.addresses
    addresses.foreach {
      addressDao.save
    }

    val vehicleDao = VehicleDaoImpl(DomainSupport.db)
    val vehicles = profileInfo.vehicles

    vehicles.foreach {
      vehicleDao.save
    }
  }

  def getStates(): Seq[State] = {
    val stateDao = StateDaoImpl(DomainSupport.db)
    val result = stateDao.getStates()
    return result
  }
}