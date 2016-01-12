package com.tripPlanner.domain

import java.util.UUID

import slick.driver.MySQLDriver.api._
import com.tripPlanner.shared.domain.{State, Address}
import scala.concurrent.{Await, ExecutionContext, Future}
import com.tripPlanner.domain.Tables.{Address => Addresses, AddressRow}
import scala.concurrent.duration._

/**
  * Created by aabreu on 12/29/15.
  */
trait AddressDao {
  def update(address:Address): Future[Long]
  def create(address: Address): Future[Option[String]]
}

case class AddressDaoImpl(db:Database)(implicit ec:ExecutionContext) extends AddressDao {
  def update(address:Address) = {
    val query = for {
      a <- Addresses if a.id === address.id
    } yield (a.street, a.stateId, a.zipcode)

    val update = query.update(address.street, address.state.id, address.zipCode)
    db.run(update) map {
      result => result
    }
  }

  def create(address: Address) = {
    val id = UUID.randomUUID().toString

    val insertAddress = Addresses += AddressRow(id, address.userId, address.street, address.state.id, address.zipCode)

    db.run(insertAddress) map {
      result =>
        if(result == 1)
          Some(id)
        else
          None
    }
  }

}
