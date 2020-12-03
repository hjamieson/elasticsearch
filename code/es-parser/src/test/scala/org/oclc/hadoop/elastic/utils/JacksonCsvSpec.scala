package org.oclc.hadoop.elastic.utils

import org.junit.runner.RunWith
import org.scalatest.FlatSpec
import org.scalatest.junit.JUnitRunner
import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.{CsvMapper, CsvSchema}
import elkutils.Models.Test1Line

import java.io.File

@RunWith(classOf[JUnitRunner])
class JacksonCsvSpec extends FlatSpec {
  //  val testFile = "src/test/resources/test1.csv"
  val testFile = "src/test/resources/movies.csv"
  "data as map" should "return each row as a map" in {
    val csvFile = new File(testFile)
    val mapper = new CsvMapper
    val schema = CsvSchema.emptySchema.withHeader // use first row as header; otherwise defaults are fine
    val it: MappingIterator[java.util.Map[String, String]] = mapper.readerFor(classOf[java.util.HashMap[String, String]])
      .`with`(schema)
      .readValues(csvFile)
    //    while (it.hasNext) {
    //      val rowAsMap = it.next
    //      println(rowAsMap)
    //    }
    val lines = it.readAll()
    assert(lines.size == 89, "expect 89 rows")
    println(lines.get(0))
    lines
  }

  /* I can't make jackson read with a pojo as a schema in Scala.
  *
  "using a schema" should "add type info" in {
    val csvFile = new File("src/test/resources/test1.csv")
    val mapper = new CsvMapper
    val schema = mapper.schemaFor(classOf[Test1Line])
    val iter: MappingIterator[Test1Line] = mapper.readerFor(classOf[Test1Line])
      .`with`(schema)
      .readValue(csvFile)
    val list = iter.readAll()
    assert(list.get(0).tag == "funny");
  }

* */

}
