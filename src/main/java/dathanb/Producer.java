package dathanb;

import org.apache.kafka.clients.producer.*;
import org.apache.log4j.Level;

import java.util.Properties;

public class Producer {

    public static void main(String[] args){
        org.apache.log4j.Logger.getLogger("kafka").setLevel(Level.TRACE);

        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        properties.put(ProducerConfig.LINGER_MS_CONFIG, 1000);
        properties.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, 5000);

        try (KafkaProducer<String, String> kafkaProducer = new KafkaProducer<>(properties)) {
            System.out.println(kafkaProducer.partitionsFor("kafka-test"));
            for (int i = 0; i < 1000; i++) {
                System.out.println(i);
                kafkaProducer.send(new ProducerRecord<>("kafka-test", 0, null, "test message - " + i), callback());
            }
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
