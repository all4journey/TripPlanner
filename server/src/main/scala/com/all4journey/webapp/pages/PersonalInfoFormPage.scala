package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.all4journey.domain.{AddressDao, StateDaoImpl, UserDao}
import com.all4journey.shared.domain.{Address, State}
import com.all4journey.shared.domain.PersonalFormData
import com.all4journey.webapp.Page
import com.all4journey.webapp.util.{UserContext, DomainSupport}
import com.typesafe.scalalogging.LazyLogging
import prickle.{Pickle, Unpickle}
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

        val personalFormData = buildFormData(loadStates = true)

        val personalInfoFormView = new PersonalInfoFormView(personalFormData)
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
              val postSuccessFormData = buildFormData(loadStates = false)
              val pickledPfp = Pickle.intoString(postSuccessFormData)
              complete(pickledPfp)
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

    personalFormData.address match {
      case Some(someAddress) => createOrUpdateAddress(user.id, someAddress.copy(userId = user.id))
      case _ => return
    }


  }

  private def createOrUpdateAddress(userId: String, addressToInsertOrUpdate: Address): Unit = {
    val addressDao = AddressDao(DomainSupport.db)

    if (addressToInsertOrUpdate.id.equals("0")) {
      val homeAddressFuture = addressDao.getHomeAddressByUserId(userId)
      val homeAddressResult = Await.result(homeAddressFuture, 10 seconds)

      if (homeAddressResult.size == 1)
        throw new IllegalStateException("There is a home address for this user already")
      else
        addressDao.create(addressToInsertOrUpdate)
    }

    else
      addressDao.update(addressToInsertOrUpdate)
  }

  private def buildFormData(loadStates: Boolean): PersonalFormData = {

    val user = UserContext.getCurrentUser

    var personalFormData = PersonalFormData(user, None, Seq[State]())

    val addressDao = AddressDao(DomainSupport.db)
    val homeAddressFuture = addressDao.getHomeAddressByUserId(user.id)
    val homeAddressResult = Await.result(homeAddressFuture, 10 seconds)

    if (homeAddressResult.size > 1)
      throw new IllegalStateException("There is more than one home address")

    else if (!homeAddressResult.isEmpty) {
      personalFormData = personalFormData.copy(address = Some(homeAddressResult(0)))
    }

    if (loadStates) {
      val stateDao = StateDaoImpl(DomainSupport.db)
      val statesFuture = stateDao.getStates
      val stateList = Await.result(statesFuture, 10 seconds)
      personalFormData = personalFormData.copy(states = stateList)
    }

    personalFormData
  }
}

object PersonalInfoFormPage extends PersonalInfoFormPage
