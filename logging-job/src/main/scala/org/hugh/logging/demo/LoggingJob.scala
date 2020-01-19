package org.hugh.logging.demo

import com.typesafe.scalalogging.Logger

import scala.util.Random

object LoggingJob extends App {

  val random = new Random()

  val logger = Logger("LoggingJob")
  val sleepTimeMS = 2 * 1000
  var iterations: Long = 1
  logger.debug("simple logging job is initiating")
  while (true){
    if (iterations % 5 == 0) logger.debug(s"iteration: $iterations")
    iterations = iterations + 1
    logger.info(s"I am stuck on bandaids [${random.nextInt(1000)}]")
    Thread.sleep(sleepTimeMS)
  }

}
