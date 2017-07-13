package hello;

import hello.models.CpuAggregator;
import hello.models.CpuUsage;
import serializer.JsonDeserializer;
import serializer.JsonSerializer;
import org.apache.kafka.common.serialization.*;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.kstream.internals.WindowedDeserializer;
import org.apache.kafka.streams.kstream.internals.WindowedSerializer;

import java.util.Properties;

public class HelloKafkaStreams {
    public static void main(String[] args) {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "anomaly-kafka-streams");
        settings.put(StreamsConfig.STATE_DIR_CONFIG, "streams-pipe");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

        Predicate<String, CpuUsage> normalPredicate = (k, cpu) -> cpu.getCpu() <= 80;
        Predicate<String, CpuUsage> anomalyPredicate = (k, cpu) -> cpu.getCpu() > 80;

        JsonSerializer<CpuUsage> cpuUsageJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<CpuUsage> cpuUsageJsonDeserializer = new JsonDeserializer<>(CpuUsage.class);
        Serde<CpuUsage> cpuUsageSerde = Serdes.serdeFrom(cpuUsageJsonSerializer, cpuUsageJsonDeserializer);

        WindowedSerializer<String> stringWindowedSerializer = new WindowedSerializer<>();
        WindowedDeserializer<String> stringWindowedDeserializer = new WindowedDeserializer<>();
        Serde<Windowed<String>> integerWindowedSerde = Serdes.serdeFrom(stringWindowedSerializer, stringWindowedDeserializer);

        Serializer<String> stringSerializer = new JsonSerializer<>();
        Deserializer<String> stringDeserializer = new JsonDeserializer<>(String.class);
        Serde<String> stringSerde = Serdes.serdeFrom(stringSerializer, stringDeserializer);

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, CpuUsage> rawStream = builder.stream(stringSerde, cpuUsageSerde, "hardware-usage")
                .map((k, v) -> KeyValue.pair(v.getNodeID(), v));

        Serializer<CpuAggregator> cpuAggregatorSerializer = new JsonSerializer<>();
        Deserializer<CpuAggregator> cpuAggregatorJsonDeserializer = new JsonDeserializer<>(CpuAggregator.class);
        Serde<CpuAggregator> cpuAggregatorSerde = Serdes.serdeFrom(cpuAggregatorSerializer, cpuAggregatorJsonDeserializer);
        KTable<Windowed<String>, CpuAggregator> aggregatedTable =
                rawStream
                        .groupBy((k, v) -> k, stringSerde, cpuUsageSerde)
                        .aggregate(CpuAggregator::new, (k, v, cpuAggregator) -> cpuAggregator.add(v), TimeWindows.of(60 * 1000L), cpuAggregatorSerde, "AnomalyStore");

        aggregatedTable.toStream((window, v) -> v.getNodeID())
                .through(Serdes.String(), cpuAggregatorSerde, "all-alerts")
        .filter((k, v) -> v.getCount() > 9)
        .to(Serdes.String(), cpuAggregatorSerde, "actual-alert");
        ;

        KStream<String, CpuUsage>[] diffStreams = rawStream.branch(anomalyPredicate, normalPredicate);

        diffStreams[0].to(stringSerde, cpuUsageSerde, "anomaly-count");
        diffStreams[1].to(stringSerde, cpuUsageSerde, "normal-count");

        System.out.println("Starting Anomaly detection(rule based) Example");

        final KafkaStreams kafkaStreams = new KafkaStreams(builder, settings);

        kafkaStreams.cleanUp();
        kafkaStreams.start();
        System.out.println("Streaming started");

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
