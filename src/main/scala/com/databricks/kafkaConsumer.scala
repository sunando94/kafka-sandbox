package com.databricks

import org.apache.kafka.clients.consumer.KafkaConsumer
import org.apache.kafka.common.TopicPartition
import org.apache.spark.sql.catalyst.util.DateTimeUtils

import java.sql.Timestamp
import java.util.Properties
import scala.collection.JavaConverters.{iterableAsScalaIterableConverter, seqAsJavaListConverter}

object kafkaConsumer {
  def main(args: Array[String]): Unit = {
    val props: Properties = new Properties()
    props.put("group.id", "test")
    props.put("bootstrap.servers", "PLAINTEXT://127.0.0.1:9092")
    props.put("key.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer",
      "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "1000")
    props.put("auto.offset.reset", "earliest")
    //    props.put(StreamsConfig.DEFAULT_TIMESTAMP_EXTRACTOR_CLASS_CONFIG,
    //      MyEventTimeExtractor.getClass.getName
    //    )
   // props.put("interceptor.classes", "com.databricks.CustomTimeStampInterceptors")

    val consumer = new KafkaConsumer(props)

    val topics = List("topic_61")
    try {
      val offset = 1
      val topicPartition = new TopicPartition("topic_61", 0)
      consumer.assign(List(topicPartition).asJava)
      consumer.seek(topicPartition, offset);
      while (true) {
        val records = consumer.poll(0)
        for (record <- records.asScala) {
          if (record.offset() == offset) {
            println("Topic: " + record.topic() + ", Key: " + record.key() + ", Value: " + record.value() +
              ", Offset: " + record.offset() + ", Partition: " + record.partition() +
              ", timestamp: " + DateTimeUtils.fromJavaTimestamp(new Timestamp(record.timestamp())))
          }
        }
      }
    } finally {
      consumer.close()
    }
  }
}
