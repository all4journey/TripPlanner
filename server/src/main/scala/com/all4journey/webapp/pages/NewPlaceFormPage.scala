package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.all4journey.domain.{StateDaoImpl, AddressDao}
import com.all4journey.shared.domain.{AddressTypePickler, PlacesFormData, Address, State}
import com.all4journey.webapp.Page
import com.all4journey.webapp.exceptions.{NoAddressException, InvalidAddressException}
import com.all4journey.webapp.util.{UserContext, DomainSupport}
import com.typesafe.scalalogging.LazyLogging
import prickle.{Pickle, Unpickle}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success

/**
  * Created by aabreu on 1/31/16.
  */
@deprecated
trait NewPlaceFormPage extends Page with LazyLogging with AddressTypePickler {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    post {
      extractRequestContext { implicit ctx =>
        entity(as[String]) { addressJsonPayload =>
          Unpickle[PlacesFormData].fromString(addressJsonPayload) match {
            case Success(someFormData: PlacesFormData) =>
              val newAddress = someFormData.place match {
                case Some(someAddress) => addNewPlace(someAddress)
                case None => throw NoAddressException
              }
              val postSuccessFormData = buildFormData(newAddress, loadStates = false)
              val pickledPfp = Pickle.intoString(postSuccessFormData)
              complete(pickledPfp)
            case _ => complete(StatusCodes.BadRequest)
          }
        }
      }
    }
  }

  private def addNewPlace(address: Address): Address = {

    val violations = Address.doValidation(address)

    if (address.id == "0" && violations.isEmpty) {
      val user = UserContext.getCurrentUser
      val addressDao = AddressDao(DomainSupport.db)

      val addressToInsert = address.copy(userId = user.id)
      val newAddressId = addressDao.create(address.copy(userId = user.id))
      addressToInsert.copy(id = newAddressId)
    }
    else
      throw InvalidAddressException
  }

  private def buildFormData(newAddress: Address, loadStates: Boolean): PlacesFormData = {

    val user = UserContext.getCurrentUser

    val addressDao = AddressDao(DomainSupport.db)
    val addressesFuture = addressDao.getAddressesByUserId(user.id, None)
    val addressList = Await.result(addressesFuture, 10 seconds)

//    var placesFormData = PlacesFormData(None, Seq.empty[Address], Seq.empty[State]
//    placesFormData = placesFormData.copy(addresses = addressList)

//    addressList foreach(addressItem =>
//      if (addressItem.id.equals(newAddressId))
//        placesFormData = placesFormData.copy(address = Some(addressItem))
//    )

    val states = if (loadStates) {
      val stateDao = StateDaoImpl(DomainSupport.db)
      val statesFuture = stateDao.getStates
      val stateList = Await.result(statesFuture, 10 seconds)
      stateList
    }
    else
      Seq.empty[State]

    PlacesFormData(None, Option(newAddress), addressList, states)
  }
}

@deprecated
object NewPlaceFormPage extends NewPlaceFormPage

