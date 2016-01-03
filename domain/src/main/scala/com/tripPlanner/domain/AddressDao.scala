package com.tripPlanner.domain

import slick.driver.MySQLDriver.api._
import com.tripPlanner.shared.domain.Address
import scala.concurrent.{ExecutionContext, Future}
import com.tripPlanner.domain.Tables.{Address => Addresses, AddressRow}

/**
  * Created by aabreu on 12/29/15.
  */
trait AddressDao {
  def save(address:Address): Future[Long]
}

case class AddressDaoImpl(db:Database)(implicit ec:ExecutionContext) extends AddressDao {
  def save(address:Address) = {
    val primaryKey = address.id match {
      case Some(pk) => pk
      case None => java.util.UUID.randomUUID.toString
    }
    val insertAddress = Addresses += AddressRow(primaryKey, address.userId, address.street, address.state.id, address.zipCode)

    db.run(insertAddress) map {
      result => result
    }
  }
}
