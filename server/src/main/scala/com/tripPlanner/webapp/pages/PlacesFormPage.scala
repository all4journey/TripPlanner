package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.domain.{StateDaoImpl, AddressDao}
import com.tripPlanner.shared.domain.{PlacesFormData, State, Address}
import com.tripPlanner.webapp.Page
import com.tripPlanner.webapp.util.{DomainSupport, UserContext}
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by aabreu on 1/30/16.
  */
trait PlacesFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {

        val placesFormData = buildFormData(loadStates = true)

        val placesFormView = new PlacesFormView(placesFormData)
        complete(placesFormView.apply())
      }
      }
    }
  }

  private def buildFormData(loadStates: Boolean): PlacesFormData = {

    val user = UserContext.getCurrentUser

    var placesFormData = PlacesFormData(None, Seq[Address](), Seq[State]())

    val addressDao = AddressDao(DomainSupport.db)
    val addressesFuture = addressDao.getAddressesByUserId(user.id, None)
    val addressList = Await.result(addressesFuture, 10 seconds)

    placesFormData = placesFormData.copy(addresses = addressList)

    if (loadStates) {
      val stateDao = StateDaoImpl(DomainSupport.db)
      val statesFuture = stateDao.getStates
      val stateList = Await.result(statesFuture, 10 seconds)
      placesFormData = placesFormData.copy(states = stateList)
    }

    placesFormData
  }

}

object PlacesFormPage extends PlacesFormPage
