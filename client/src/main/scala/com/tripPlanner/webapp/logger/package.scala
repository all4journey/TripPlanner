package com.tripPlanner.webapp

package object logger {
  private val defaultLogger = LoggerFactory.getLogger("Log")
  defaultLogger.enableServerLogging("/log")

  def log = defaultLogger
}
