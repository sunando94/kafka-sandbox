package com.databricks

import org.apache.kafka.clients.consumer.{ConsumerInterceptor, ConsumerRecord, ConsumerRecords}
import org.apache.kafka.common.TopicPartition
import org.apache.kafka.common.record.TimestampType

import java.util
import java.util.Collections
import scala.collection.JavaConverters._

class CustomTimeStampInterceptors[K, V] extends ConsumerInterceptor[K, V] {
  private var clientId: String = _

  override def configure(configs: util.Map[String, _]): Unit = {
    // Extract the clientId from the configurations.
    clientId = configs.get("client.id").asInstanceOf[String]

    // Other configurations, if needed, can be extracted similarly.
    // For example, you might want to extract other properties like
    // "bootstrap.servers", "group.id", etc., depending on your use case.
  }

  override def onConsume(records: ConsumerRecords[K, V]): ConsumerRecords[K, V] = {

    // Create an empty map for partitions and records
    val emptyPartitionMap: java.util.Map[TopicPartition, java.util.List[ConsumerRecord[K, V]]] = Collections.emptyMap();

    // Create the empty ConsumerRecords object
    var consumerRecords: ConsumerRecords[K, V] = new ConsumerRecords(emptyPartitionMap)

    val mutablePartitionMap: util.Map[TopicPartition, util.List[ConsumerRecord[K, V]]] =
      consumerRecords.partitions().asScala.foldLeft(new util.HashMap[TopicPartition, util.List[ConsumerRecord[K, V]]]) {
        (map, partition) => map.put(partition, new util.ArrayList[ConsumerRecord[K, V]]); map
      }

    val consumerRecordList = new util.ArrayList[ConsumerRecord[K, V]]

    for (partition <- records.partitions().asScala) {
      val topic = partition.topic()

      val recordsInPartition = records.records(partition).asScala
      for (record <- recordsInPartition) {
        if (record.timestamp().toString.length > 13) {
          val newRecord = new ConsumerRecord[K, V](
            record.topic(),
            record.partition(),
            record.offset(),
            record.timestamp() / 1000,
            TimestampType.CREATE_TIME,
            record.serializedKeySize(),
            record.serializedValueSize(),
            record.key(),
            record.value(),
            record.headers(),
            record.leaderEpoch()
          )

          consumerRecordList.add(newRecord)

        }
        else {
          consumerRecordList.add(record)
        }
      }

      mutablePartitionMap.put(new TopicPartition(topic, partition.partition()), consumerRecordList)

    }
    val interceptRecordsOP: ConsumerRecords[K, V] = new ConsumerRecords(mutablePartitionMap)

    interceptRecordsOP
  }


  override def onCommit(offsets: util.Map[org.apache.kafka.common.TopicPartition, org.apache.kafka.clients.consumer.OffsetAndMetadata]): Unit = {

  }

  override def close(): Unit = {
    println("close")
  }
}