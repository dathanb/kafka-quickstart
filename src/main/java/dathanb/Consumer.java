package dathanb;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.util.*;

public class Consumer {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "default");

        TopicPartition topic = new TopicPartition("kafka-test", 0);
        System.out.println("Creating consumer");
        KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(properties);
        try (kafkaConsumer) {
//            kafkaConsumer.assign(Collections.singleton(topic));
            kafkaConsumer.subscribe(Collections.singletonList("kafka-test"));
            System.out.println(kafkaConsumer.assignment());
            kafkaConsumer.seekToBeginning(Collections.singleton(topic));
            // subscribe to all partitions for the kafka-test topic
            while (true) {
                System.out.println("Polling");
                ConsumerRecords<String, String> records = kafkaConsumer.poll(10);
                System.out.println("Got consumerrecords");
                for (var record : records) {
                    System.out.println(String.format("Topic - %s, Partition - %d, Value: %s", record.topic(), record.partition(), record.value()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

}
