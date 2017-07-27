package visualisation;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.*;
import serializer.JsonDeserializer;

import java.util.Arrays;
import java.util.Properties;

public class ConsumeTopic {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "influx-consumer");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
        props.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, "30000");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        System.out.println(Serdes.String().getClass().getName());
        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);

        String topic = "cpu-usage";
        consumer.subscribe(Arrays.asList(topic));

        while (true){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String,String> record: records) {
                System.out.println(record.value());
            }
        }
    }
}
