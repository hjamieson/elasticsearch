package elkutils

import org.junit.runner.RunWith
import org.oclc.hadoop.elastic.utils.CsvUtils
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class MiscSpec extends FlatSpec {
  "a mutable.Map" should "convert to immutable.Map" in {
    val m1 = Map[String, Int]("one" -> 1, "two" -> 2, "three" -> 3)
    val m2 = m1.map(e => e._1 -> e._2 * 5)
    println(m2)
  }

  "a immutable.Map" should "convert to immutable.Map" in {
    val m1 = Map[String, String]("one" -> "1", "pi" -> "3.1415", "valid" -> "true", "long" -> "123456", "string" -> "this is text")

    val m2 = m1.map(e => e._1 -> CsvUtils.toJsonValue(e._2))
    println(m2)
  }

  "a map" should "be inferred" in {
    val m1 = Map[String, String]("one" -> "1", "pi" -> "3.1415", "valid" -> "true", "long" -> "123456", "string" -> "this is text")
    println(m1.map{ e =>
      s""""${e._1}" : ${CsvUtils.toJsonValue(e._2)} """
    }
    .mkString(","))
  }

}
