package com.databricks

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

object kafkaProducer {

  def main(args: Array[String]): Unit = {
    import java.util.Properties

      val props:Properties = new Properties()
      props.put("bootstrap.servers","PLAINTEXT://127.0.0.1:9092")
      props.put("key.serializer",
        "org.apache.kafka.common.serialization.StringSerializer")
      props.put("value.serializer",
        "org.apache.kafka.common.serialization.StringSerializer")
      props.put("acks","all")
      val producer = new KafkaProducer[String, String](props)
      val topic = "topic_61"
      try {
          val record = new ProducerRecord[String, String](topic, 0,"3542336370913665500".toLong,"test_key", "My broken record")
        val metadata = producer.send(record)
        printf(s"sent record(key=%s value=%s) " +
          "meta(partition=%d, offset=%d)\n",
          record.key(), record.value(), metadata.get().partition(),
          metadata.get().offset())

      }finally {
        producer.close()
      }

  }

}
