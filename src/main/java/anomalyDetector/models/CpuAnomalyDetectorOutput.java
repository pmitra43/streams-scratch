package anomalyDetector.models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class CpuAnomalyDetectorOutput {
    public String nodeId;
    public int timeArray[];
    public long lastUpdatedTime;
    public String startTimeStamp;
    public String endTimeStamp;
    static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSX", Locale.ENGLISH);

    public static CpuAnomalyDetectorOutput fromAnomaly(AnomalyDetector cpuAggregator) {
        CpuAnomalyDetectorOutput cpuAnomalyDetectorOutput = new CpuAnomalyDetectorOutput();
        cpuAnomalyDetectorOutput.nodeId = cpuAggregator.getNodeId();
        cpuAnomalyDetectorOutput.timeArray = cpuAggregator.getTimeArray();
        cpuAnomalyDetectorOutput.startTimeStamp = cpuAggregator.getStartTimeStamp();
        cpuAnomalyDetectorOutput.endTimeStamp = cpuAggregator.getEndTimeStamp();
        return cpuAnomalyDetectorOutput;
    }

    public void update(AnomalyDetector cpuAggregator) {

        LocalDateTime startDate = LocalDateTime.parse(cpuAggregator.getStartTimeStamp(), timeFormatter);
        LocalDateTime endDate = LocalDateTime.parse(this.endTimeStamp, timeFormatter);

        if (endDate.compareTo(startDate) < 0) {
            this.timeArray = cpuAggregator.getTimeArray();
            this.timeArray = cpuAggregator.getTimeArray();
            this.startTimeStamp = cpuAggregator.getStartTimeStamp();
            this.endTimeStamp = cpuAggregator.getEndTimeStamp();
            this.lastUpdatedTime = System.currentTimeMillis();
        }
    }

    public boolean updatedWithinLastPublish(long lastPublishedTime) {
        return lastPublishedTime < this.lastUpdatedTime;
    }
}
