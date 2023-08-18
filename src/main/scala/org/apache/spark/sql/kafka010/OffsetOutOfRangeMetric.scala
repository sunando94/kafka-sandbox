package org.apache.spark.sql.kafka010.custom

import org.apache.spark.sql.connector.metric.CustomSumMetric

private[spark] class OffsetOutOfRangeMetric extends CustomSumMetric {
  override def name(): String = "offsetOutOfRange"

  override def description(): String = "estimated number of fetched offsets out of range"
}
