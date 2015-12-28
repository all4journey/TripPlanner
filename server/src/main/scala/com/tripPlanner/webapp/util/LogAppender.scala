package com.tripPlanner.webapp.util

import akka.actor.ActorSystem
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import akka.stream.Materializer
import com.tripPlanner.webapp.Page
import com.typesafe.scalalogging.Logger
import org.slf4j.LoggerFactory
import spray.json.DefaultJsonProtocol


trait LogAppenderJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val logMessageFormat = jsonFormat5(LogMessage)
}
/**
  * Created by rjkj on 12/19/15.
  */
trait LogAppender extends Page with LogAppenderJsonSupport {
  def logger:Logger = Logger(LoggerFactory.getLogger("CLIENT"))

  def apply()(implicit sys: ActorSystem, mat: Materializer) = {
    post {
      entity(as[List[LogMessage]]){log  =>
        for {
          logValue <- log
        } yield logValue.level match {
          case "TRACE" => logger.trace(logValue.message)
          case "DEBUG" => logger.debug(logValue.message)
          case "INFO" => logger.info(logValue.message)
          case "WARN" => logger.warn(logValue.message)
          case "ERROR" => logger.error(logValue.message)
          case "FATAL" => logger.error(logValue.message)
          case s:String => logger.info(s"received invalid log type: $s")
        }
        complete("logged")
      }
    }
  }
}

object LogAppender extends LogAppender

case class LogMessage(logger:String, timestamp:Double, level:String, url:String, message:String)
