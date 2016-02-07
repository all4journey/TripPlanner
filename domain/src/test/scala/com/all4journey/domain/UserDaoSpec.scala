package com.all4journey.domain

import com.all4journey.shared.domain.User

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by rjkj on 12/6/15.
  */
class UserDaoSpec extends DomainTestSpec{

  "UserDaoImpl" should "insert a User" in {
    val dao = UserDao(db)
    val user:User = User(fName = "Rob", lName = "Kernick", email = "a.a@somesite.com", registrationDate = Some("12/31/2015"))
    val future = dao.create(user)

    val id = Await.result(future, Duration.Inf).getOrElse("")
    id should not be ""
  }

  it should "insert a User with no date set" in {
    val dao = UserDao(db)
    val user:User = User(fName =  "Rob", lName = "Kernick", email = "a.a@somesite.com", registrationDate = None)
    val future = dao.create(user)

    val id = Await.result(future, Duration.Inf).getOrElse("")
    id should not be ""

  }

  it should "update a user" in {
    val dao = UserDao(db)
    val user:User = User(fName =  "Rob", lName = "Kernick", email = "a.a@somesite.com", registrationDate = None)
    val future = dao.create(user)

    val id = Await.result(future, Duration.Inf).getOrElse("")
    id should not be empty

    val updatedUser = user.copy(fName = "Changed", id = id)

    val result = dao.update(updatedUser)
    val returned = Await.result(result, Duration.Inf)
    returned shouldEqual 1
  }

  it should "delete a User" in {
    val dao = UserDao(db)
    val user:User = User(fName =  "Rob", lName = "Kernick", email = "a.a@somesite.com", registrationDate = None)
    val future = dao.create(user)

    val id = Await.result(future, Duration.Inf).getOrElse("")
    id should not be ""

    val updated = user.copy(id = id)
    val deletedFuture = dao.delete(updated)

    val deleted = Await.result(deletedFuture, Duration.Inf)
    deleted shouldEqual true

  }

}
