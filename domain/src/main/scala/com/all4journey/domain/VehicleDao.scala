package com.all4journey.domain

import java.util.UUID

import com.all4journey.domain.Tables.{Vehicle => Vehicles, VehicleRow}
import com.all4journey.shared.domain.Vehicle
import slick.driver.MySQLDriver.api._

import scala.concurrent.{ExecutionContext, Future}

case class VehicleDao(db: Database)(implicit ec: ExecutionContext) {
  def update(vehicle: Vehicle): Future[Long] = {
    val query = for {
      v <- Vehicles if v.id === vehicle.id
    } yield (v.make, v.model, v.year)

    val update = query.update(vehicle.make, vehicle.model, vehicle.year)
    db.run(update) map { result => result }
  }

  def create(vehicle: Vehicle): Future[Option[String]] = {
    val id = UUID.randomUUID().toString

    val insertVehicle = Vehicles += VehicleRow(id, vehicle.userId, vehicle.year, vehicle.make, vehicle.model)
    db.run(insertVehicle) map {
      result =>
        if (result == 1)
          Some(id)
        else
          None
    }
  }

}
