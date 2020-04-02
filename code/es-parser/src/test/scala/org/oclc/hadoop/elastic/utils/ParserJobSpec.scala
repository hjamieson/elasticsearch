package org.oclc.hadoop.elastic.utils

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

import scala.io.Source

@RunWith(classOf[JUnitRunner])
class ParserJobSpec extends FlatSpec {

  val GOODFILE = "src/test/resources/hbase-sample.log"
  val BADFILE = "src/test/resources/hbase-sample-bad.log"
  val HOST = "localhost"
  val ROLE = "role"
  val parser = TextLogParser
  var lines = 0

  "parsing 10 good lines" should "return 10" in {
    val sin = Source.fromFile(GOODFILE).getLines()
      .foreach {
        line =>
          parser.parse(line)(HOST, ROLE) match {
            case Some(e) => println(JsonMapper.toJson(e))
            case None => Unit
          }
          lines += 1
      }
    assert(lines == 9)
  }

  "parsing a file with bad lines" should "return 13" in {
    lines = 0
    val sin = Source.fromFile(BADFILE).getLines()
      .foreach {
        line =>
          parser.parse(line)(HOST, ROLE) match {
            case Some(e) => println(JsonMapper.toJson(e))
            case None => Unit
          }
          lines += 1
      }
    assert(lines == 12)
  }

}
