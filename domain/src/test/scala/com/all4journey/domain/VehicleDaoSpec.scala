package com.all4journey.domain

import com.all4journey.shared.domain.{User, Vehicle}

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration

/**
  * Created by rjkj on 1/16/16.
  *
  * NOT NEEDED RIGHT NOW
  */
/*
class VehicleDaoSpec extends DomainTestSpec{
  "VehicleDao" should "insert a vehicle" in {
    val userDao = UserDao(db)
    val user:User = User(fName = "Rob", lName = "Kernick", registrationDate = Some("12/31/2015"))
    val userFuture = userDao.create(user)

    val userId = Await.result(userFuture, Duration.Inf).getOrElse("")
    userId should not be empty

    val dao = VehicleDao(db)
    val vehicle = Vehicle(userId = userId, year = Option("1999"), make = Some("Toyota"), model = Some("Camry"))
    val future = dao.create(vehicle)

    val id = Await.result(future, Duration.Inf).getOrElse("")
    id should not be empty
  }

  it should "update a vehicle" in {

  }
}
*/
