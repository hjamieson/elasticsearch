package org.oclc.hadoop.elastic.utils

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class DateHelperSpec extends FlatSpec {

  val date1 = "2020-03-12 00:00:15,377"
  /*
  epoch = 1583985615
  Timestamp in milliseconds: 1583985615000
   */

  "toEpochSecond" should "return 1583985615" in {
    assert(DateHelper.toEpochSecond(date1) == 1583985615L)
  }

  "toEpochMillis()" should "return 1583985615000" in {
    assert(DateHelper.toEpochMillis(date1) == 1583985615377L)
  }

}
