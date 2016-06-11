package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server
import akka.stream.Materializer
import com.all4journey.domain.{StateDaoImpl, AddressDao}
import com.all4journey.shared.domain._
import com.all4journey.webapp.Page
import com.all4journey.webapp.exceptions.{NoAddressException, InvalidAddressException}
import com.all4journey.webapp.util.{DomainSupport, UserContext}
import com.typesafe.scalalogging.LazyLogging
import prickle.{Pickle, Unpickle}
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Success

/**
  * Created by aabreu on 1/30/16.
  */
trait PlacesFormPage extends Page with LazyLogging with AddressTypePickler {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {

        val placesFormData = buildFormData(loadStates = true)

        val placesFormView = new PlacesFormView(placesFormData)
        complete(placesFormView.apply())
      }
      }
    } ~
    post {
      extractRequestContext { implicit ctx =>
        entity(as[String]) { addressJsonPayload =>
          Unpickle[PlacesFormData].fromString(addressJsonPayload) match {
            case Success(someFormData) =>
              val (homeAddress, place) = (
                handleInsertOrUpdate(someFormData.homeAddress), handleInsertOrUpdate(someFormData.place)
                )

              val postSuccessFormData = buildFormData(Option(homeAddress), Option(place), loadStates = false)
              val pickledPfp = Pickle.intoString(postSuccessFormData)
              complete(pickledPfp)
            case _ => complete(StatusCodes.BadRequest)
          }
        }
      }
    }
  }

  def getPlaceById()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {
        val addressId = ctx.request.getUri().query().getOrElse("id", "")

        val addressDao = AddressDao(DomainSupport.db)
        val addressesFuture = addressDao.getAddressById(addressId)
        val addressList = Await.result(addressesFuture, 10 seconds)

        if (addressList.size > 1)
          throw new IllegalStateException("There is more than 1 address with ID " + addressId)

        if (addressList.nonEmpty)
          complete(Pickle.intoString(addressList.head))

        else
          complete(StatusCodes.BadRequest)

      }
      }
    }
  }

  private def handleInsertOrUpdate(addressToInsertOrUpdate: Option[Address]): Address = {
    addressToInsertOrUpdate match {
      case Some(someAddress) if Address.doValidation(someAddress).isEmpty && someAddress.id != "0" =>
        updatePlace(someAddress)

      case Some(someAddress) if Address.doValidation(someAddress).isEmpty && someAddress.id == "0" =>
        addNewPlace(someAddress)

      case Some(someAddress) if Address.doValidation(someAddress).nonEmpty =>
        throw InvalidAddressException

      case None => throw NoAddressException
    }
  }


  private def addNewPlace(address: Address): Address = {

    val user = UserContext.getCurrentUser
    val addressDao = AddressDao(DomainSupport.db)

    val addressToInsert = address.copy(userId = user.id)
    val newAddressId = addressDao.create(address.copy(userId = user.id))
    addressToInsert.copy(id = newAddressId)
  }

  private def updatePlace(address: Address) = {

    val user = UserContext.getCurrentUser
    val addressDao = AddressDao(DomainSupport.db)

    val addressToUpdate = address.copy(userId = user.id)
    addressDao.update(addressToUpdate)
    addressToUpdate
  }

  private def buildFormData(homeAddress: Option[Address] = None, place: Option[Address] = None, loadStates: Boolean): PlacesFormData = {

    val user = UserContext.getCurrentUser

    val addressDao = AddressDao(DomainSupport.db)
    val addressesFuture = addressDao.getAddressesByUserId(user.id, None)
    val addressList = Await.result(addressesFuture, 10 seconds)

    val states = if (loadStates) {
      val stateDao = StateDaoImpl(DomainSupport.db)
      val statesFuture = stateDao.getStates
      Await.result(statesFuture, 10 seconds)
    } else
      Seq.empty[State]

    PlacesFormData(homeAddress, place, addressList, states)
  }

}

object PlacesFormPage extends PlacesFormPage
