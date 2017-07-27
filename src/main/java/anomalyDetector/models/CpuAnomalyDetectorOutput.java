package anomalyDetector.models;

import util.TimeFormatter;

import java.util.Date;

public class CpuAnomalyDetectorOutput {
    public String nodeId;
    public int timeArray[];
    public long lastUpdatedTime;
    public String startTimeStamp;
    public String endTimeStamp;

    public static CpuAnomalyDetectorOutput fromAnomaly(AnomalyDetector cpuAggregator) {
        CpuAnomalyDetectorOutput cpuAnomalyDetectorOutput = new CpuAnomalyDetectorOutput();
        cpuAnomalyDetectorOutput.nodeId = cpuAggregator.getNodeId();
        cpuAnomalyDetectorOutput.timeArray = cpuAggregator.getTimeArray();
        cpuAnomalyDetectorOutput.startTimeStamp = cpuAggregator.getStartTimeStamp();
        cpuAnomalyDetectorOutput.endTimeStamp = cpuAggregator.getEndTimeStamp();
        return cpuAnomalyDetectorOutput;
    }

    public void update(AnomalyDetector cpuAggregator) {

        Date startDate = TimeFormatter.stringToDate(cpuAggregator.getStartTimeStamp());
        Date endDate = TimeFormatter.stringToDate(this.endTimeStamp);

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
