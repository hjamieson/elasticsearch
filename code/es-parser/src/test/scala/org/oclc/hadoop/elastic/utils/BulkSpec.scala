package org.oclc.hadoop.elastic.utils

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class BulkSpec extends FlatSpec {
  val LOGTEXT = "2020-03-12 00:00:15,423 INFO org.apache.hadoop.hbase.master.RegionStates: Transition {c62c97939102d7149a2c08c3c0b1b188 state=OPEN, ts=1583899224308, server=hddev1db011dxc1.dev.oclc.org,60020,1582741896405} to {c62c97939102d7149a2c08c3c0b1b188 state=PENDING_CLOSE, ts=1583985615423, server=hddev1db011dxc1.dev.oclc.org,60020,1582741896405}"
  val MESSAGE = "org.apache.hadoop.hbase.master.RegionStates: Transition {c62c97939102d7149a2c08c3c0b1b188 state=OPEN, ts=1583899224308, server=hddev1db011dxc1.dev.oclc.org,60020,1582741896405} to {c62c97939102d7149a2c08c3c0b1b188 state=PENDING_CLOSE, ts=1583985615423, server=hddev1db011dxc1.dev.oclc.org,60020,1582741896405}"
  val TIMESTAMP = "2020-03-12 00:00:15,423"
  val LEVEL = "INFO"
  val badText = "this is crap"
  val HOST = "localhost"
  val ROLE = "mstr"
  val INDEX = "foo"

  val ENTRY = TextLogParser.parse(LOGTEXT)(HOST, ROLE, 999)
  val bulk = EsFormatter.toBulkApi(ENTRY.get, INDEX)
  val line1 = bulk.split("\n")(0)
  val line2 = bulk.split("\n")(1)

  "BulkApi" should "return 2 lines for each entry" in {
    assert(bulk.split("\n").size == 2)
  }

  "line1" should "have index field" in {
    assert(line1.startsWith("{\"index\""))
    assert(line1.contains("\"_index\":\"foo\""))
  }

  "line2" should "have the doc field" in {
    assert(line2.startsWith("""{"timeStamp":"""))
    println(line2)
  }

}
