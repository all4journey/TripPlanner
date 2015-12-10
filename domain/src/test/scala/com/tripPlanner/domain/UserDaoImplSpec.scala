package com.tripPlanner.domain

import java.time.ZonedDateTime

import org.scalatest._
import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

//import com.tripPlanner.domain.User

/**
  * Created by rjkj on 12/6/15.
  */
class UserDaoImplSpec extends FlatSpec with Matchers with BeforeAndAfter{

  var db:Database = _



  before {
    db = Database.forConfig("db")
  }

  after {
    db.close()
  }

  "UserDaoImpl" should "insert a User" in {
    val dao = UserDaoImpl(db)
    val user = User("12345", "Rob", "Kernick", Option(ZonedDateTime.now()))
    val future = dao.save(user)

    val longResult = Await.result(future, Duration.Inf)
    longResult should be (1)
  }

}
