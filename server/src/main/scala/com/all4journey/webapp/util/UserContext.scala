package com.all4journey.webapp.util

import com.all4journey.domain.UserDao
import com.all4journey.shared.domain.User
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration._

/**
  * Created by aabreu on 1/18/16.
  *
  * Just using this class for now as a usercontext... assuming
  * that each request will ideally have an instance this, each page or logic can just
  * say useContext.user and it'll get the user associated with
  * a request...
  */
trait UserContext {

  def getCurrentUser: User = {
//    val userDao = UserDao(DomainSupport.db)
//    val userFuture = userDao.getUserById("1234-1234-1234-1234")
//    val result = Await.result(userFuture, 10 seconds)
//
//    if (result.size > 1)
//      throw new IllegalStateException("there is more than one user with the same ID")
//
//    result(0)
    User("1234-1234-1234-1234", "andy", "andy123", "andy@tripplanner.travel", "", None)
  }
}

object UserContext extends UserContext
