package dathanb;

import org.apache.kafka.clients.producer.*;

import java.util.Properties;

public class Producer {
    public static void main(String[] args){
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "1");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 1000000);
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1000);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);

        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties)) {
            System.out.println(kafkaProducer.partitionsFor("kafka-foo"));
            for (int i = 0; i < 1000; i++) {
                System.out.println(i);
                var metadataFuture = kafkaProducer.send(new ProducerRecord<>("kafka-test", 0, null, "test message - " + i), callback());
                System.out.println(metadataFuture.get().partition());
                System.out.println(metadataFuture.get().topic());
                Thread.sleep(1000);
            }
            kafkaProducer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Callback callback() {
        return (metadata, exception) -> {
            System.out.println(metadata);
            System.out.println(exception);
        };
    }

}
