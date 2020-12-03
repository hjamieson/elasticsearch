package org.oclc.hadoop.elastic.utils

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.{CsvMapper, CsvSchema}

import java.io.File
import scala.collection.mutable
import scala.collection.immutable

/**
 * reads CSV records and transforms them into various forms for use.
 */
object CsvUtils {
  /**
   * return a mapping of column-name to value in a map.  This function needs
   * access to the header to know what columns are named, so we will pass it
   * as an implicit for now.
   *
   * @param csvLine
   * @return
   */
  def toMap(csvLine: String)(implicit header: Array[String]): mutable.Map[String, String] = {
    val token = split(csvLine)
    val map: mutable.Map[String, String] = mutable.Map()
    for (i <- 0 to header.length - 1) {
      map += (header(i) -> token(i))
    }
    map
  }

  /**
   * split a csv line of text into manageble tokens.  should take care
   * of quoted values, spacies, etc.
   *
   * @param line
   * @return
   */
  def split(line: String): Array[String] = {
    val token = line.split(",")
    token.map(t => {
      val noSpc = t.trim
      if (noSpc(0) == "\"") noSpc.replaceAll("\"", "") else noSpc
    })
  }

  /**
   * returns true if the token should be quoted as a string in JSON.
   */
  val longs = raw"([0-9]+)".r
  val doubles = raw"([0-9.]+)".r
  val words = raw"(\w+)".r
  val bools = raw"(true|True|false|False)".r

  def isString(s: String): Boolean = s match {
    case longs(_) => false
    case doubles(_) => false
    case bools(_) => false
    case _ => true
  }

  def toJsonValue(n: String): String = {
    if (isString(n)) s""""${n}"""" else n
  }

  def csvToBulkFormat(csvFile: File, index: String): Unit = {
    import scala.collection.JavaConverters._
    val mapper = new CsvMapper
    val schema = CsvSchema.emptySchema.withHeader // use first row as header; otherwise defaults are fine
    val it: MappingIterator[java.util.Map[String, String]] = mapper.readerFor(classOf[java.util.HashMap[String, String]])
      .`with`(schema)
      .readValues(csvFile)
        while (it.hasNext) {
          val rowAsMap = it.next
          println(ElkUtils.EsFormatter.toBulkApiWithInference(immutable.Map() ++ rowAsMap.asScala, index))
        }
  }

}
