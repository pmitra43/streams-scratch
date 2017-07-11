package hello;

import hello.models.CpuUsage;
import hello.serdes.CpuUsageDeserializer;
import hello.serdes.CpuUsageSerializer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;

import java.util.Properties;

public class HelloKafkaStreams {
    public static void main(String[] args) {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "anomaly-kafka-streams");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        settings.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");

        CpuUsageSerializer cpuUsageSerializer = new CpuUsageSerializer();
        CpuUsageDeserializer cpuUsageDeserializer = new CpuUsageDeserializer();
        StringSerializer stringSerializer = new StringSerializer();
        StringDeserializer stringDeserializer = new StringDeserializer();

        Serde<String> stringSerde = Serdes.serdeFrom(stringSerializer, stringDeserializer);
        Serde<CpuUsage> cpuUsageSerde = Serdes.serdeFrom(cpuUsageSerializer, cpuUsageDeserializer);

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, CpuUsage> rawStream = builder.stream(stringSerde, cpuUsageSerde, "hardware-usage")
                .filter((k, cpu) -> cpu.getCpu()>80.0);
        rawStream.to(stringSerde, cpuUsageSerde, "anomaly-count");
        System.out.println("Starting Anomaly detection(rule based) Example");

        KafkaStreams kafkaStreams = new KafkaStreams(builder, settings);
        kafkaStreams.start();
        System.out.println("Streaming started");
    }
}
