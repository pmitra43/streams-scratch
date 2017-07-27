package anomalyDetector;

import anomalyDetector.models.AnomalyFilter;
import anomalyDetector.models.AnomalyDetector;
import anomalyDetector.models.CpuAnomalyDetectorOutput;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.processor.TopologyBuilder;
import org.apache.kafka.streams.state.Stores;
import serializer.JsonDeserializer;
import serializer.JsonSerializer;

import java.util.Properties;

public class CpuAnomalyFilterDriver {
    public static void main(String[] args) {
        TopologyBuilder topologyBuilder = new TopologyBuilder();

        JsonDeserializer<AnomalyDetector> cpuAnomalyDetectorJsonDeserializer = new JsonDeserializer<>(AnomalyDetector.class);

        JsonDeserializer cpuAnomalyDetectorOutputJsonDeserializer = new JsonDeserializer<>(CpuAnomalyDetectorOutput.class);
        JsonSerializer<CpuAnomalyDetectorOutput> cpuAnomalyDetectorOutputJsonSerializer = new JsonSerializer<>();
        StringSerializer stringSerializer = new StringSerializer();
        StringDeserializer stringDeserializer = new StringDeserializer();

        Serde<CpuAnomalyDetectorOutput> cpuAnomalyDetectorOutputSerde = Serdes.serdeFrom(cpuAnomalyDetectorOutputJsonSerializer,cpuAnomalyDetectorOutputJsonDeserializer);

        topologyBuilder.addSource("cpu-source", stringDeserializer, cpuAnomalyDetectorJsonDeserializer, "actual-alert")
                .addProcessor("summary", AnomalyFilter::new, "cpu-source")
                .addStateStore(Stores.create("cpu-anomalies-output").withStringKeys()
                        .withValues(cpuAnomalyDetectorOutputSerde).inMemory().maxEntries(100).build(),"summary")
                .addSink("sink-2", "filtered-anomaly-alerts", stringSerializer, cpuAnomalyDetectorOutputJsonSerializer, "summary");

        KafkaStreams streaming = new KafkaStreams(topologyBuilder, getProperties());
        streaming.start();
        System.out.println("Cpu Usage anomaly filter process now started");
    }

    private static Properties getProperties(){

        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "Sample-Stateful-Processor");
        props.put(StreamsConfig.STATE_DIR_CONFIG, "streams-pipe");
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "stateful_processor_id");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.COMMIT_INTERVAL_MS_CONFIG, "1000");
        return props;
    }
}
