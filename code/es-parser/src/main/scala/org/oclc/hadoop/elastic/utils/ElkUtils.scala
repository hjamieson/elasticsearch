package org.oclc.hadoop.elastic.utils

/**
 * utilities for ELK tools
 */
object ElkUtils {

  /**
   * ES bulk API expression of an create
   * POST _bulk
   * {"index" : {"_index":"test", "_id": "1"}}
   * {"doc" : {"field1":"value1", "field2":"value2"}}
   */
  object EsFormatter {
    def toBulkApi(entry: Map[String, String], index: String): String = {
      s"""{"index":{"_index":"${index}"}}""" + "\n" + JsonMapper.toJson(entry)
    }

    def toBulkApiWithInference(entry: Map[String, String], index: String): String = {
      val inner = entry.map(e => s""""${e._1}" : ${CsvUtils.toJsonValue(e._2)} """).mkString(",")
      s"""{"index":{"_index":"${index}"}}""" + "\n" + s"""{"doc" : {$inner}}"""
    }
  }

}
