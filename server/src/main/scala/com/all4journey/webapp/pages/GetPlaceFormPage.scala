package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl.model.{StatusCodes, StatusCode}
import akka.stream.Materializer
import com.all4journey.domain.AddressDao
import com.all4journey.shared.domain.{State, Address, PlacesFormData}
import com.all4journey.webapp.Page
import com.all4journey.webapp.util.DomainSupport
import com.typesafe.scalalogging.LazyLogging
import prickle.Pickle
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by aabreu on 1/30/16.
  */
trait GetPlaceFormPage extends Page with LazyLogging {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) = pathEnd {
    get {
      extractRequestContext { implicit ctx => {
        val addressId = ctx.request.getUri().query().get("id").getOrElse("")

        val addressDao = AddressDao(DomainSupport.db)
        val addressesFuture = addressDao.getAddressById(addressId)
        val addressList = Await.result(addressesFuture, 10 seconds)

        if (addressList.size > 1)
          throw new IllegalStateException("There is more than 1 address with ID " + addressId)

        if (!addressList.isEmpty)
          complete(Pickle.intoString(addressList(0)))

        else
          complete(StatusCodes.BadRequest)

      }
      }
    }
  }
}

object GetPlaceFormPage extends GetPlaceFormPage


