package com.tripPlanner.domain

import org.scalatest._
import slick.driver.MySQLDriver.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

import com.tripPlanner.shared.domain.User

/**
  * Created by rjkj on 12/6/15.
  */
class UserDaoImplSpec extends FlatSpec with Matchers with BeforeAndAfter{

  var db:Database = _



  before {
    val databaseConfig = sys.props.get("db_config")
    db = Database.forConfig(databaseConfig.getOrElse("db"))
  }

  after {
    db.close()
  }

  "UserDaoImpl" should "insert a User" in {
    val dao = UserDaoImpl(db)
    val user:User = User(fName = "Rob", lName = "Kernick", registrationDate = Some("12/31/2015"))
    val future = dao.create(user)

    val id = Await.result(future, Duration.Inf)
    id should not be ""
  }

  it should "update a User with no date set" in {
    val dao = UserDaoImpl(db)
    val user:User = User(fName =  "Rob", lName = "Kernick", registrationDate = None)
    val future = dao.create(user)

    val id = Await.result(future, Duration.Inf)
    id should not be ""
  }

}
