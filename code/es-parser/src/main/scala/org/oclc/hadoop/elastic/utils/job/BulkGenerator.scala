package org.oclc.hadoop.elastic.utils.job

import java.net.InetAddress

import org.oclc.hadoop.elastic.utils.{EsFormatter, JsonMapper, TextLogParser}
import org.rogach.scallop.ScallopConf

import scala.io.Source

object BulkGenerator extends App {

  var lineCount = 0
  val cli = new CLI(this.args)

  Source.stdin
    .getLines()
    .foreach {
      line =>
        TextLogParser.parse(line)(cli.hostname.getOrElse(localHostName()),
          cli.role(), lineCount) match {
          case Some(e) => println(EsFormatter.toBulkApi(e, cli.index()))
          case None => Unit
        }
        lineCount += 1
    }
  // kick out an extra LF
  println()

  /**
   * Command line options we regard.
   * @param args
   */
  class CLI(args: Seq[String]) extends ScallopConf(args) {
    val hostname = opt[String](required = false)
    val index = opt[String](required = true)
    val role = opt[String](required = true)
    verify()
  }

  def localHostName(): String = {
    InetAddress.getLocalHost.getCanonicalHostName()
  }
}
