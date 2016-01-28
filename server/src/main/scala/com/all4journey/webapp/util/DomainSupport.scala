package com.all4journey.webapp.util

import slick.driver.MySQLDriver.api._

/**
  * Created by rjkj on 12/28/15.
  */
object DomainSupport {
  lazy val databaseConfig = sys.props.get("db_config")
  lazy val db:Database = Database.forConfig(databaseConfig.getOrElse("db"))
}
