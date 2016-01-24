package com.tripPlanner.webapp.util

import com.tripPlanner.domain.UserDao
import com.tripPlanner.shared.domain.User
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
    val userDao = UserDao(DomainSupport.db)
    userDao.getUserById("3770b302-84b1-4099-9957-cf6ca52b50cf")
  }
}

object UserContext extends UserContext
