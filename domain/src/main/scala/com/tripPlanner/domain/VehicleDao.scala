package com.tripPlanner.domain

import com.tripPlanner.shared.domain.Vehicle
import com.tripPlanner.domain.Tables.{Vehicle=>Vehicles, VehicleRow}
import slick.driver.MySQLDriver.api._
import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by aabreu on 12/29/15.
  */
trait VehicleDao {
  def save(vehicle: Vehicle): Future[Long]
}

case class VehicleDaoImpl(db:Database)(implicit ec:ExecutionContext) extends VehicleDao {
  def save(vehicle: Vehicle) = {
    val primaryKey = vehicle.id match {
      case Some(pk) => pk
      case None => java.util.UUID.randomUUID.toString
    }

    val insertVehicle = Vehicles += VehicleRow(primaryKey, vehicle.userId, vehicle.year, vehicle.make, vehicle.model)

    db.run(insertVehicle) map {
      result => result
    }
  }
}
