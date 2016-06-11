package com.all4journey.webapp.pages

import akka.actor.ActorSystem
import akka.http.scaladsl.model.StatusCodes
import akka.stream.Materializer
import com.all4journey.domain.{AddressDao, StateDaoImpl, UserDao}
import com.all4journey.shared.domain.{AddressTypePickler, Address, State, PersonalFormData}
import com.all4journey.webapp.Page
import com.all4journey.webapp.exceptions.{MultipleHomeAddressException, HomeAddressException, InvalidAddressException}
import com.all4journey.webapp.util.{UserContext, DomainSupport}
import com.typesafe.scalalogging.LazyLogging
import prickle.{Pickle, Unpickle}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import akka.http.scaladsl.server.Route

import scala.concurrent.Await
import scala.language.postfixOps
import scala.util.Success

/**
  * Created by aabreu on 1/10/16.
  */
trait PersonalInfoFormPage extends Page with LazyLogging with SecurityDirectives with AddressTypePickler {
  def apply()(implicit actorSystem: ActorSystem, mat: Materializer) =
    pathEnd {
      get {
        extractRequestContext { implicit ctx => {
//          authenticate { user =>
            val personalFormData = buildFormData

            val personalInfoFormView = new PersonalInfoFormView("token", personalFormData)
            complete(personalInfoFormView.apply())
//          }
        }
        }
      } ~
        post {
          extractRequestContext { implicit ctx =>
//            authenticate { user =>
              entity(as[String]) { profileJsonPayload =>
                Unpickle[PersonalFormData].fromString(profileJsonPayload) match {
                  case Success(pfd: PersonalFormData) =>
                    handleProfileUpdates(pfd)
                    val postSuccessFormData = buildFormData
                    val pickledPfp = Pickle.intoString(postSuccessFormData)
                    complete(pickledPfp)
                  case _ => complete(StatusCodes.BadRequest)
//                }
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

  }

  private def buildFormData: PersonalFormData = {
    val user = UserContext.getCurrentUser
    PersonalFormData(user)

  }
}

object PersonalInfoFormPage extends PersonalInfoFormPage
