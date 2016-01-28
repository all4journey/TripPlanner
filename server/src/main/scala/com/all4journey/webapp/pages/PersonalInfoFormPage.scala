package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.stream.Materializer
import com.all4journey.domain.{AddressDao, StateDaoImpl}
import com.all4journey.shared.domain.PersonalFormData
import com.all4journey.webapp.Page
import com.all4journey.webapp.util.{UserContext, DomainSupport}
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
