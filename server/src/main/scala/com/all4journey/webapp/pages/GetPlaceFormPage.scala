package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.{StatusCode, StatusCodes}
import akka.stream.Materializer
import com.all4journey.domain.AddressDao
import com.all4journey.shared.domain.{Address, AddressTypePickler, PlacesFormData, State}
import com.all4journey.webapp.Page
import com.all4journey.webapp.util.DomainSupport
import com.typesafe.scalalogging.LazyLogging
import prickle.Pickle

import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

/**
  * Created by aabreu on 1/30/16.
  */
trait GetPlaceFormPage extends Page with LazyLogging with AddressTypePickler {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
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
}

object GetPlaceFormPage extends GetPlaceFormPage


