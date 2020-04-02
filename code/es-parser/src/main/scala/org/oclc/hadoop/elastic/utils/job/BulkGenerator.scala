package org.oclc.hadoop.elastic.utils.job

import org.oclc.hadoop.elastic.utils.{EsFormatter, JsonMapper, TextLogParser}

import scala.io.Source

object BulkGenerator extends App {

  var lineCount = 0
  val HOST = "localhost"
  val ROLE = "rs"
  val INDEX = "foobar"

  Source.stdin
    .getLines()
    .foreach {
      line =>
        TextLogParser.parse(line)(HOST, ROLE, lineCount) match {
          case Some(e) => println(EsFormatter.toBulkApi(e, INDEX))
          case None => Unit
        }
        lineCount += 1
    }


}
