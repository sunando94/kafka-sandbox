package com.databricks

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.streaming.Trigger
import org.apache.spark.sql.types.{LongType, StringType}

object SparkKafkaConsumer {
  def main(args: Array[String]): Unit = {
    val spark = new SparkSession.Builder().master("local[*]").appName("TestKafkaConsumer").getOrCreate();
    val df = spark
      .readStream
      .format("kafka-custom")
      .option("kafka.interceptor.classes", "com.databricks.CustomTimeStampInterceptors")
      .option("kafka.bootstrap.servers", "PLAINTEXT://127.0.0.1:9092")
      .option("key.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer")
      .option("value.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer")
      .option("enable.auto.commit", "true")
      .option("auto.commit.interval.ms", "1000")
      .option("subscribe", "topic_61")
      .option("startingOffsets", """{"topic_61":{"0":1}}""")
      .load()
    val opdf = df.select(col("key").cast(StringType),col("value").cast(StringType),col("topic"),col("partition"),col("timestamp").cast("timestamp"))
    opdf.writeStream.format("console")
      .outputMode("append")
      .trigger(Trigger.Once())
     // .option("checkpointLocation", "checkpoint")
      .start()
      .awaitTermination()

  }
}
