package org.oclc.hadoop.elastic.utils

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ParserSpec extends FlatSpec {

  val parser = TextLogParser
  val logText = "2020-03-12 00:00:15,423 INFO org.apache.hadoop.hbase.master.RegionStates: Transition {c62c97939102d7149a2c08c3c0b1b188 state=OPEN, ts=1583899224308, server=hddev1db011dxc1.dev.oclc.org,60020,1582741896405} to {c62c97939102d7149a2c08c3c0b1b188 state=PENDING_CLOSE, ts=1583985615423, server=hddev1db011dxc1.dev.oclc.org,60020,1582741896405}"
  val MESSAGE = "org.apache.hadoop.hbase.master.RegionStates: Transition {c62c97939102d7149a2c08c3c0b1b188 state=OPEN, ts=1583899224308, server=hddev1db011dxc1.dev.oclc.org,60020,1582741896405} to {c62c97939102d7149a2c08c3c0b1b188 state=PENDING_CLOSE, ts=1583985615423, server=hddev1db011dxc1.dev.oclc.org,60020,1582741896405}"
  val TIMESTAMP = "2020-03-12 00:00:15,423"
  val LEVEL = "INFO"
  val badText = "this is crap"
  val HOST = "localhost"
  val ROLE = "mstr"

  "a parser" should "accept a line of text to parse" in {
    val entry = parser.parse(logText)(HOST, ROLE)
    assert(entry.isDefined)
  }
  it should "extract a TIMESTAMP field" in {
    val entry = parser.parse(logText)(HOST, ROLE)
    assert(entry.get.timeStamp == TIMESTAMP)
  }
  it should "extract a message field" in {
    val entry = parser.parse(logText)(HOST, ROLE)
    assert(entry.get.message == MESSAGE)
  }

  it should "extract a level field" in {
    val entry = parser.parse(logText)(HOST, ROLE)
    assert(entry.get.level == LEVEL)
  }
  it should "handle gibberish" in {
    val entry = parser.parse(badText)(HOST, ROLE)
    assert(!entry.isDefined)
  }
  it should "provide host metadata" in {
    assert(parser.parse(logText)(HOST, ROLE).get.hostname == HOST)
  }
  it should "provide service field" in {
    assert(parser.parse(logText)(HOST, ROLE).get.role == ROLE)
  }
//  it should "provide the raw data" in {
//    assert(parser.parse(logText)(HOST, ROLE).get.rawText == logText)
//  }

  it should "reject bad data" in {
    val badText = "where is this from?"
    assert(parser.parseText.isDefinedAt(badText)== false)
  }

  it should "supply a default offset" in {
    assert(parser.parse(logText)(HOST, ROLE).get.offset == 0)
  }
  it should "return an explicit offset" in {
    assert(parser.parse(logText)(HOST, ROLE, 25).get.offset == 25)
  }
}
