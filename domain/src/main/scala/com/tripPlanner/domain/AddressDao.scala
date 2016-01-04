package com.tripPlanner.domain

import slick.driver.MySQLDriver.api._
import com.tripPlanner.shared.domain.{State, Address}
import scala.concurrent.{Await, ExecutionContext, Future}
import com.tripPlanner.domain.Tables.{Address => Addresses, AddressRow}
import scala.concurrent.duration._

/**
  * Created by aabreu on 12/29/15.
  */
trait AddressDao {
  def save(address:Address): Future[Long]
  def create(address: Address): Future[Long]
}

case class AddressDaoImpl(db:Database)(implicit ec:ExecutionContext) extends AddressDao {
  def save(address:Address) = {
    storeInDb(address, createAddress = false)
  }

  def create(address: Address) = {
    storeInDb(address, createAddress = true)
  }

  private def storeInDb(address: Address, createAddress: Boolean): Future[Long] = {
    val primaryKey = if (createAddress) java.util.UUID.randomUUID.toString else {
      ensureValidId(address.id)
      address.id
    }

    val insertAddress = Addresses += AddressRow(primaryKey, address.userId, address.street, address.state.id, address.zipCode)

    db.run(insertAddress) map {
      result => result
    }
  }

  private def ensureValidId(addressId: String): Unit = {
//    val addressListQuery = TableQuery[Addresses].filter(_.id === addressId)
//    val addressListFuture = db.run(addressListQuery.result) map {
//      addressListQuery => for {
//        a <- addressListQuery
//      } yield Address(a.id, a.userId, None, State(a.stateId, ""), a.zipcode);
//    }

    val addressCountQuery = TableQuery[Addresses].filter(_.id === addressId).length
    val addressCountFuture = db.run(addressCountQuery.result)
    val myAddressCount = Await.result(addressCountFuture, 10 seconds)

    if (myAddressCount != 1) {
      // need to make sure akka http doesn't return this exception to the browser
      throw new IllegalStateException("The vehicle ID  is not valid")
    }
  }
}
