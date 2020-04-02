package org.oclc.hadoop.elastic.utils

trait LogParser {
  def parse(text: String)(host: String, role: String, offset: Long): Option[LogEntry]
}

/**
 * a parsed log line
 *
 * @param rawText
 * @param hostname
 * @param service
 *
 * time sample: 2020-03-12 00:00:15,375
 */
case class LogEntry(timeStamp: String, level: String, message: String, hostname: String, role: String, offset: Long = 0) {
  lazy val epoch: Long = DateHelper.toEpochSecond(timeStamp)
  lazy val epochMillis: Long = DateHelper.toEpochMillis(timeStamp)
}


object TextLogParser extends LogParser {
  val pat = raw"(\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2},\d{3})\s([^\s]+)\s+(.*)".r

  val parseText = new PartialFunction[String, (String, String, String)] {
    override def isDefinedAt(x: String): Boolean = !pat.findAllMatchIn(x).isEmpty

    override def apply(str: String) = {
      str match {
        case pat(ts, lvl, msg) => (ts, lvl, msg)
      }
    }
  }

  override def parse(text: String)(host: String, role: String, offset: Long = 0): Option[LogEntry] = {
    if (parseText.isDefinedAt(text)) {
      val e = parseText.apply(text)
      Some(LogEntry(e._1, e._2, e._3, host, role, offset))
    } else None
  }
}
