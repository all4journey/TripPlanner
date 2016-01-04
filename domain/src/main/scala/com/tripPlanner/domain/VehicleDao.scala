package com.tripPlanner.domain

import com.tripPlanner.shared.domain.Vehicle
import com.tripPlanner.domain.Tables.{Vehicle=>Vehicles, VehicleRow}
import slick.driver.MySQLDriver.api._
import scala.concurrent.{Await, ExecutionContext, Future}
import scala.concurrent.duration._

/**
  * Created by aabreu on 12/29/15.
  */
trait VehicleDao {
  def save(vehicle: Vehicle): Future[Long]
  def create(vehicle: Vehicle): Future[Long]
}

case class VehicleDaoImpl(db:Database)(implicit ec:ExecutionContext) extends VehicleDao {
  def save(vehicle: Vehicle) = {
    storeInDb(vehicle, createVehicle = false)
  }

  def create(vehicle: Vehicle) = {
    storeInDb(vehicle, createVehicle = true)
  }

  private def storeInDb(vehicle: Vehicle, createVehicle: Boolean): Future[Long] = {
    val primaryKey = if (createVehicle) java.util.UUID.randomUUID.toString else {
      ensureValidId(vehicle.id)
      vehicle.id
    }

    val insertVehicle = Vehicles += VehicleRow(primaryKey, vehicle.userId, vehicle.year, vehicle.make, vehicle.model)

    db.run(insertVehicle) map {
      result => result
    }
  }

  private def ensureValidId(vehicleId: String): Unit = {
//    val vehicleListQuery = TableQuery[Vehicles].filter(_.id === vehicleId)
//    val vehicleListFuture = db.run(vehicleListQuery.result) map {
//      vehicleListQuery => for {
//        v <- vehicleListQuery
//      } yield Vehicle(v.id, v.userId, None, None, None);
//    }

    val vehicleCountQuery = TableQuery[Vehicles].filter(_.id === vehicleId).length
    val vehicleCountFuture = db.run(vehicleCountQuery.result)
    val myVehicleCount = Await.result(vehicleCountFuture, 10 seconds)

    if (myVehicleCount != 1) {
      // need to make sure akka http doesn't return this exception to the browser
      throw new IllegalStateException("The vehicle ID  is not valid")
    }
  }
}
