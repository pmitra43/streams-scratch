package anomalyDetector.models;

import org.apache.kafka.streams.processor.AbstractProcessor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.KeyValueStore;

import java.util.Objects;

public class AnomalyFilter extends AbstractProcessor<String, AnomalyDetector> {

    private KeyValueStore<String, CpuAnomalyDetectorOutput> outputStore;
    private ProcessorContext context;
    private static long lastPublishedTime = 0;

    public void process(String key, AnomalyDetector cpuAggregator) {
        String nodeId = cpuAggregator.getNodeId();
        CpuAnomalyDetectorOutput cpuAnomalyDetectorOutput = outputStore.get(nodeId);
        if (cpuAnomalyDetectorOutput == null) {
            cpuAnomalyDetectorOutput = CpuAnomalyDetectorOutput.fromAnomaly(cpuAggregator);
        } else {
            cpuAnomalyDetectorOutput.update(cpuAggregator);
        }

        outputStore.put(nodeId, cpuAnomalyDetectorOutput);
        this.context.commit();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void init(ProcessorContext context) {
        this.context = context;
        this.context.schedule(1000);
        outputStore = (KeyValueStore<String, CpuAnomalyDetectorOutput>) this.context.getStateStore("cpu-anomalies-output");
        outputStore.flush();
        Objects.requireNonNull(outputStore, "State store can't be null");

    }

    @Override
    public void punctuate(long streamTime) {
        KeyValueIterator<String, CpuAnomalyDetectorOutput> it = outputStore.all();
        long currentTime = System.currentTimeMillis();

        while (it.hasNext()) {
            CpuAnomalyDetectorOutput summary = it.next().value;
            if (summary.updatedWithinLastPublish(AnomalyFilter.lastPublishedTime)) {
                AnomalyFilter.lastPublishedTime = currentTime;
                this.context.forward(summary.nodeId, summary);
            }
        }
    }
}
