package com.tripPlanner.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.tripPlanner.domain.{AddressDao, StateDaoImpl}
import com.tripPlanner.shared.domain.PersonalFormData
import com.tripPlanner.webapp.Page
import com.tripPlanner.webapp.util.{UserContext, DomainSupport}
import com.typesafe.scalalogging.LazyLogging
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Await

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

        val personalInfoFormView = new PersonalInfoFormView(PersonalFormData(Some(user), addresses, states))
        complete(personalInfoFormView.apply())
      }
      }
    }
  }
}

object PersonalInfoFormPage extends PersonalInfoFormPage
