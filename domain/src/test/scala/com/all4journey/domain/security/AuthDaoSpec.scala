package com.all4journey.domain.security

import java.util.UUID

import com.all4journey.domain.DomainTestSpec
import com.all4journey.shared.domain.User
import com.all4journey.shared.domain.security.Token
import com.github.t3hnar.bcrypt._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * Created by rjkj on 1/31/16.
  */
class AuthDaoSpec extends DomainTestSpec{
  it should "signUp a User" in {
    val user = User(fName="Test", lName = "User", emailAddress = UUID.randomUUID().toString+ "test@testing.com", password = "password123", registrationDate= None)
    val dao = AuthDao(db)
    val token = dao.signUp(user)

    val result = Await.result[Token](token, Duration.Inf)
    result.id should not be empty
    result.userId should not be empty
    result.token should not be empty
  }

  it should "signIn a user with a valid token" in {
    val user = User(fName="Test", lName = "User", emailAddress = UUID.randomUUID().toString+ "test@testing.com", password = "password123", registrationDate= None)
    val dao = AuthDao(db)
    val tokenFuture = dao.signUp(user)

    val token = Await.result[Token](tokenFuture, Duration.Inf)
    token.id should not be empty
    token.userId should not be empty
    token.token should not be empty

    val result = dao.signIn(user.emailAddress, user.password)

    val tokenFromDb = Await.result(result, Duration.Inf)
    tokenFromDb should not be None
    tokenFromDb.get.id shouldEqual token.id
    tokenFromDb.get.userId shouldEqual token.userId
    tokenFromDb.get.token shouldEqual token.token
  }

  it should "authenticate a user" in {
    val user = User(fName="Test", lName = "User", emailAddress = UUID.randomUUID().toString+ "test@testing.com", password = "password123", registrationDate= None)
    val dao = AuthDao(db)
    val token = dao.signUp(user)

    val result = Await.result[Token](token, Duration.Inf)
    result.id should not be empty
    result.userId should not be empty
    result.token should not be empty

    val userFuture = dao.authenticate(result.token)
    val validatedUser = Await.result(userFuture, Duration.Inf)

    validatedUser should not be None
    validatedUser.get.fName shouldEqual user.fName
    validatedUser.get.lName shouldEqual user.lName
    validatedUser.get.emailAddress shouldEqual user.emailAddress
    validatedUser.get.password shouldBe empty
    validatedUser.get.registrationDate should not be empty
  }
}
