package org.oclc.hadoop.elastic.utils

import org.scalatest.FlatSpec

class CsvSpec extends FlatSpec {
  "Csv split" should "split a line into tokens" in {
    val tokens = CsvUtils.split("alpha,beta,gamma, delta,epsilon")
    assert(tokens.size == 5, "should get 5 tokens");
  }
  it should "remove white-space in tokens" in {
    val tokens = CsvUtils.split("alpha ,beta, gamma , delta, epsilon")
    assert(tokens.filter(_.contains(" ")).size == 0, "no tokens contain blanks")
  }

  it should "handle quoted tokens" in {
    val text =
      """
        |1, 2, "hello", true, 5, "two strings"
        |""".stripMargin
    val tokens = CsvUtils.split(text)
    assert(tokens.size == 6, "expecting 6 tokens")
    assert(tokens(5) == "two strings")
  }


}
