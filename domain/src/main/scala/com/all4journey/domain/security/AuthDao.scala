package com.all4journey.domain.security

import java.util.UUID

import com.all4journey.domain.UserDao
import com.all4journey.shared.domain.User
import com.all4journey.shared.domain.security.Token
import com.all4journey.domain.Tables.{Token => tokens, User => users, UserRow, TokenRow}
import com.github.t3hnar.bcrypt._
import slick.driver.MySQLDriver.api._

import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.ExecutionContext.Implicits.global

class AuthDao(db:Database){
  def signIn(login: String, password:String) = {
    val query = users.filter(u => u.emailAddress === login).result

      db.run(query) flatMap { users =>
      users.find(user => password.bcrypt == user.password) match {
        case Some(user) => db.run(tokens.filter(_.userId === user.id).result.headOption).flatMap {
          case Some(token) => Future.successful(Some(Token(token.id, token.userId, token.token)))
          case None => createToken(user.id).map(token => Some(token))
        }
        case None => Future.successful(None)
      }
    }
  }

  def signUp(newUser:User): Future[Token] = {
    val userDao = UserDao(db)
    userDao.create(newUser) flatMap{
      case Some(userId) => createToken(userId)
      case None => Future.successful[Token](Token.empty)
    }
  }

  def authenticate(token:String)(implicit ec:ExecutionContext):Future[Option[User]] = {
    val query = for {
      token <- tokens.filter(_.token === token)
      user <- users.filter(_.id === token.userId)
    } yield user

    val action = query.result.headOption

    db.run(action ) map {
      case Some(user) => Some(User(user.id, user.firstName, user.lastName, user.emailAddress, "", Option(user.registrationDate.toString)))
      case None => None
    }
  }

  def createToken(userId: String):Future[Token] = {
    val token = Token(id = UUID.randomUUID().toString, userId = userId)

    db.run(tokens returning tokens += TokenRow(token.id, token.userId, token.token)) map {
      result => Token(result.id, result.userId, result.token)
    }
  }
}
