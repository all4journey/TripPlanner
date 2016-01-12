package com.tripPlanner.domain

import org.scalatest.{BeforeAndAfter, Matchers, FlatSpec}
import slick.driver.MySQLDriver.api._

/**
  * Created by rjkj on 1/11/16.
  */
trait DomainTestSpec extends FlatSpec with Matchers with BeforeAndAfter{
  var db:Database = _

  before {
    val databaseConfig = sys.props.get("db_config")
    db = Database.forConfig(databaseConfig.getOrElse("db"))
  }

  after {
    db.close()
  }
}
