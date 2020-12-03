package org.oclc.hadoop.elastic.utils.job

import org.oclc.hadoop.elastic.utils.CsvUtils

import java.io.File

/**
 * reads a CSV and renders it into ELK bulk format.  Caller must supply the filename
 * and the name of the index.
 */
object CsvToEsBulk extends App {
  assert(args.length == 2, "required: file-path, index-name")
  CsvUtils.csvToBulkFormat(new File(args(0)), args(1))
}
