package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.tripPlanner.domain.{VehicleDaoImpl, UserDaoImpl, AddressDaoImpl, StateDaoImpl}
import com.tripPlanner.shared.domain.{Profile, State}
import com.tripPlanner.webapp.Page
import com.tripPlanner.webapp.pages.AjaxProfileView
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
    val userDao = UserDaoImpl(DomainSupport.db)
    val userIdFuture = userDao.create(profileInfo.user)

    val addressDao = AddressDaoImpl(DomainSupport.db)

    val vehicleDao = VehicleDaoImpl(DomainSupport.db)

    userIdFuture.onSuccess {
      case id =>
//        for {
//        address <- profileInfo.addresses
//        addressId <- addressDao.create(address.copy(userId = id))
//      } yield address.copy(id = addressId)
        profileInfo.addresses foreach(address => addressDao.create(address.copy(userId = id)))
        profileInfo.vehicles foreach(vehicle => vehicleDao.create(vehicle.copy(userId = id)))
    }
//    for {
//      address <- profileInfo.addresses
//      addressId <- addressDao.create(address.copy(userId = userId))
//    } addressId

    //    val addressDao = AddressDaoImpl(DomainSupport.db)
    //    val addresses = profileInfo.addresses

    // TODO: need to leverage the address count in the DB as well
    //    val MaxAddresses = 4
    //    if (addresses.size > MaxAddresses) {
    //      throw new IllegalArgumentException(s"The list of addresses passed exceeds $MaxAddresses")
    //    }
    //
    //    for (address <- addresses) {
    //      val addressWithUserId = address.copy(userId = profileInfo.user.id)
    //      addressDao.create(addressWithUserId)
    //    }
    //

    //    val vehicleDao = VehicleDaoImpl(DomainSupport.db)
    //    val vehicles = profileInfo.vehicles

    // TODO: need to leverage the vehicle count in the DB as well
    //    val MaxVehicles = 4
    //    if (vehicles.size > MaxVehicles) {
    //      throw new IllegalArgumentException(s"The list of vehicles passed exceeds $MaxVehicles")
    //    }
    //
    //    for (vehicle <- vehicles) {
    //      val vehicleWithUserId = vehicle.copy(userId = profileInfo.user.id)
    //      vehicleDao.create(vehicleWithUserId)
    //    }
    //
    //    vehicles.foreach {
    //      vehicleDao.save
    //    }
  }

  def getStates: Future[Seq[State]] = {
    val stateDao = StateDaoImpl(DomainSupport.db)
    stateDao.getStates
  }
}
