package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.tripPlanner.domain.{UserDao, AddressDao, StateDaoImpl}
import com.tripPlanner.shared.domain.{Address, PersonalFormData}
import com.tripPlanner.webapp.Page
import com.tripPlanner.webapp.util.{UserContext, DomainSupport}
import com.typesafe.scalalogging.LazyLogging
import prickle.Unpickle
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await
import scala.util.Success

/**
  * Created by aabreu on 1/10/16.
  */
trait PersonalInfoFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {

        val stateDao = StateDaoImpl(DomainSupport.db)
        val statesFuture = stateDao.getStates
        val states = Await.result(statesFuture, 10 seconds)

        val user = UserContext.getCurrentUser

        val addressDao = AddressDao(DomainSupport.db)
        val addressesFuture = addressDao.getAddressesByUserId(user.id)
        val addresses = Await.result(addressesFuture, 10 seconds)

        val personalInfoFormView = new PersonalInfoFormView(PersonalFormData(user, addresses, Seq[Address](), states))
        complete(personalInfoFormView.apply())
      }
      }
    } ~
    post {
      extractRequestContext { implicit ctx =>
        entity(as[String]) { profileJsonPayload =>
          Unpickle[PersonalFormData].fromString(profileJsonPayload) match {
            case Success(pfd: PersonalFormData) =>
              handleProfileUpdates(pfd)
              complete(StatusCodes.OK)
            case _ => complete(StatusCodes.BadRequest)
          }
        }
      }
      }
  }

  private def handleProfileUpdates(personalFormData: PersonalFormData): Unit = {
    val user = UserContext.getCurrentUser
    val updatedUser = user.copy(fName = personalFormData.user.fName, lName = personalFormData.user.lName)

    val userDao = UserDao(DomainSupport.db)
    userDao.update(updatedUser)

    val addressDao = AddressDao(DomainSupport.db)
    val addressesFuture = addressDao.getAddressesByUserId(user.id)
    val savedAddresses = Await.result(addressesFuture, 10 seconds)

    if (savedAddresses.isEmpty) {
      personalFormData.addressListToAdd foreach (address => addressDao.create(address.copy(userId = user.id)))
    } else {

      personalFormData.addressListToRemove foreach (address =>
        addressDao.deleteByAddressId(address.id)
      )

      // handle when an address (or addresses) was (were) updated
      personalFormData.addressListToAdd foreach (address =>
        if (address.id.equals("0")) addressDao.create(address.copy(userId = user.id))
        else  addressDao.update(address)

      )
    }

  }
}

object PersonalInfoFormPage extends PersonalInfoFormPage
