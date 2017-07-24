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

import java.util.Properties;

public class HelloKafkaStreams {
    static int anomalyCondition = 80;
    static int anomalyDuration = 10;

    public static void main(String[] args) {
        Properties settings = new Properties();
        settings.put(StreamsConfig.APPLICATION_ID_CONFIG, "anomaly-kafka-streams");
        settings.put(StreamsConfig.STATE_DIR_CONFIG, "streams-pipe");
        settings.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        settings.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        settings.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        settings.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000");

        Predicate<String, CpuUsage> normalPredicate = (k, cpu) -> cpu.getCpu() <= anomalyCondition;
        Predicate<String, CpuUsage> anomalyPredicate = (k, cpu) -> cpu.getCpu() > anomalyCondition;

        JsonSerializer<CpuUsage> cpuUsageJsonSerializer = new JsonSerializer<>();
        JsonDeserializer<CpuUsage> cpuUsageJsonDeserializer = new JsonDeserializer<>(CpuUsage.class);
        Serde<CpuUsage> cpuUsageSerde = Serdes.serdeFrom(cpuUsageJsonSerializer, cpuUsageJsonDeserializer);

        Serializer<String> stringSerializer = new JsonSerializer<>();
        Deserializer<String> stringDeserializer = new JsonDeserializer<>(String.class);
        Serde<String> stringSerde = Serdes.serdeFrom(stringSerializer, stringDeserializer);

        Serializer<CpuAggregator> cpuAggregatorSerializer = new JsonSerializer<>();
        Deserializer<CpuAggregator> cpuAggregatorJsonDeserializer = new JsonDeserializer<>(CpuAggregator.class);
        Serde<CpuAggregator> cpuAggregatorSerde = Serdes.serdeFrom(cpuAggregatorSerializer, cpuAggregatorJsonDeserializer);

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, CpuUsage> sourceStream = builder.stream(stringSerde, cpuUsageSerde, "cpu-usage")
                .map((k,v) -> new KeyValue<>(v.getNodeID(), v));

        KTable<Windowed<String>, CpuAggregator> aggregatedTable =
        sourceStream.filter((k, v) -> v.getCpu() > anomalyCondition)
                .groupByKey(stringSerde, cpuUsageSerde)
                .aggregate(CpuAggregator::new, (k,v,cpuAggregator)->cpuAggregator.add(v),
                        TimeWindows.of(anomalyDuration *1000L).advanceBy(1000L),
                        cpuAggregatorSerde, "AnomalyStore");

        aggregatedTable.toStream((window, v) -> v.getStartTimeStamp())
                .through(Serdes.String(), cpuAggregatorSerde, "all-alerts")
                .filter((k, v) -> v.getCount() > 9)
                .to(Serdes.String(), cpuAggregatorSerde, "actual-alert")
        ;

        KStream<String, CpuUsage>[] diffStreams = sourceStream.branch(anomalyPredicate, normalPredicate);

        diffStreams[0].to(stringSerde, cpuUsageSerde, "anomaly-count");
        diffStreams[1].to(stringSerde, cpuUsageSerde, "normal-count");

        final KafkaStreams kafkaStreams = new KafkaStreams(builder, settings);

        kafkaStreams.cleanUp();
        kafkaStreams.start();
        System.out.println("Streaming started");

        Runtime.getRuntime().addShutdownHook(new Thread(kafkaStreams::close));
    }
}
