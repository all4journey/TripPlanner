package com.all4journey.domain

import java.util.UUID

import slick.driver.MySQLDriver.api._
import com.all4journey.shared.domain.{AddressTypeFactory, State, Address}
import scala.concurrent.{Await, ExecutionContext, Future}
import com.all4journey.domain.Tables.{Address => Addresses, AddressRow}
import scala.concurrent.duration._

case class AddressDao(db:Database)(implicit ec:ExecutionContext) {
  def update(address:Address) = {
    val query = for {
      a <- Addresses if a.id === address.id
    } yield (a.street, a.stateId, a.zipcode, a.placeName)

    val update = query.update(address.street, address.state.id, address.zipCode, address.placeName)
    db.run(update) map {
      result => result
    }
  }

  def create(address: Address): String = {
    val id = UUID.randomUUID().toString

    val insertAddress = Addresses += AddressRow(id, address.userId, address.street, address.state.id, address.zipCode, address.addressType.id, address.placeName)

    db.run(insertAddress) map {
      result =>
        if(result == 1)
          Some(id)
        else
          None
    }

    id
  }

  def getAddressById(addressId: String): Future[Seq[Address]] = {
    val query = Addresses.filter(_.id === addressId)

    db.run(query.result) map {
      addressList => for {
        a <- addressList
      } yield {
        val stateDao = StateDaoImpl(db)
        val statesFuture = stateDao.getStateById(a.stateId)
        val stateResult = Await.result(statesFuture, 10 seconds)
        Address(a.id, a.userId, a.street, stateResult(0), a.zipcode, AddressTypeFactory.buildAddressTypeFromString(a.addressType), a.placeName)
      }
    }
  }

  def getHomeAddressByUserId(userId: String): Future[Seq[Address]] = {
    getAddressesByUserId(userId, Some("HOME"))
  }

  def getAddressesByUserId(userId: String, addressType: Option[String]): Future[Seq[Address]] = {

    val query = addressType match {
      case Some(someAddressType) => Addresses.filter(a => a.userId === userId && a.addressType === someAddressType)
      case None => Addresses.filter(a => a.userId === userId)
    }

    db.run(query.result) map {
      addressList => for {
        a <- addressList
      } yield {
        val stateDao = StateDaoImpl(db)
        val statesFuture = stateDao.getStateById(a.stateId)
        val stateResult = Await.result(statesFuture, 10 seconds)
        Address(a.id, a.userId, a.street, stateResult.head, a.zipcode, AddressTypeFactory.buildAddressTypeFromString(a.addressType), a.placeName)
      }
    }
  }

  def deleteByAddressId(addressId: String) = {
    val query = Addresses.filter(_.id === addressId)

    val delete = query.delete
    db.run(delete) map {
      result => result
    }
  }

}
