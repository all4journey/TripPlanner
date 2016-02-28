package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.all4journey.domain.{StateDaoImpl, AddressDao}
import com.all4journey.shared.domain.{PlacesFormData, Address, State}
import com.all4journey.webapp.Page
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
trait NewPlaceFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    post {
      extractRequestContext { implicit ctx =>
        entity(as[String]) { addressJsonPayload =>
          Unpickle[PlacesFormData].fromString(addressJsonPayload) match {
            case Success(someFormData: PlacesFormData) =>
              val newAddressId = someFormData.address match {
                case Some(someAddress) => addNewPlace(someAddress)
                case None => throw new IllegalArgumentException("there must be an input address in order to process this request")
              }
              val postSuccessFormData = buildFormData(newAddressId, loadStates = false)
              val pickledPfp = Pickle.intoString(postSuccessFormData)
              complete(pickledPfp)
            case _ => complete(StatusCodes.BadRequest)
          }
        }
      }
    }
  }

  private def addNewPlace(address: Address): String = {
    if (address.id.equals("0")) {
      val user = UserContext.getCurrentUser
      val addressDao = AddressDao(DomainSupport.db)
      addressDao.create(address.copy(userId = user.id))
    }
    else
      throw new IllegalArgumentException("This place can't be added... address type is not zero")
  }

  private def buildFormData(newAddressId:String, loadStates: Boolean): PlacesFormData = {

    val user = UserContext.getCurrentUser

    var placesFormData = PlacesFormData(None, Seq[Address](), Seq[State]())

    val addressDao = AddressDao(DomainSupport.db)
    val addressesFuture = addressDao.getAddressesByUserId(user.id, None)
    val addressList = Await.result(addressesFuture, 10 seconds)

    placesFormData = placesFormData.copy(addresses = addressList)

    addressList foreach(addressItem =>
      if (addressItem.id.equals(newAddressId))
        placesFormData = placesFormData.copy(address = Some(addressItem))
    )

    if (loadStates) {
      val stateDao = StateDaoImpl(DomainSupport.db)
      val statesFuture = stateDao.getStates
      val stateList = Await.result(statesFuture, 10 seconds)
      placesFormData = placesFormData.copy(states = stateList)
    }

    placesFormData
  }
}

object NewPlaceFormPage extends NewPlaceFormPage
