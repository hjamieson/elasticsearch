package org.oclc.hadoop.elastic.utils

import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.time.{LocalDateTime, ZoneId}
import java.util.Date

import com.fasterxml.jackson.databind.{DeserializationFeature, ObjectMapper}
import com.fasterxml.jackson.module.scala.DefaultScalaModule
import com.fasterxml.jackson.module.scala.experimental.ScalaObjectMapper

object JsonMapper {
  val om = new ObjectMapper() with ScalaObjectMapper
  om.registerModule(DefaultScalaModule)
  om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

  def toJson(value: Any): String = om.writeValueAsString(value)

  def fromJson[T: Manifest](json: String): T = om.readValue[T](json)

}

object DateHelper {
  val DATEFORMAT1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS")
  val DTF = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss,SSS")

  def toDate(str: String): Date = DATEFORMAT1.parse(str)

  def toLocalDateTime(str: String): LocalDateTime = LocalDateTime.parse(str, DTF)

  def toEpochSecond(str: String): Long = LocalDateTime.parse(str, DTF).atZone(ZoneId.systemDefault()).toEpochSecond

  def toEpochMillis(str: String): Long = LocalDateTime.parse(str, DTF).atZone(ZoneId.systemDefault()).toInstant.toEpochMilli
}

/**
 * ES bulk API expression of an create
 * POST _bulk
 * {"index" : {"_index":"test", "_id": "1"}}
 * {"doc" : {"field1":"value1", "field2":"value2"}}
 */
object EsFormatter {
  def toBulkApi(entry: LogEntry,index: String): String = {
     s"""{"index":{"_index":"${index}"}}""" + "\n" + JsonMapper.toJson(entry)
  }
}
