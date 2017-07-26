package anomalyDetector.models;

public class AnomalyDetector {
    private String nodeID;
    private int count;
    private int[] timeArray;
    private String startTimeStamp;
    private String endTimeStamp;

    public int getCount() {
        return count;
    }

    public AnomalyDetector() {
        nodeID = "1";
        count = 0;
        timeArray = new int[2];
        timeArray[0] = -1;
        timeArray[1] = -1;
        startTimeStamp = "";
        endTimeStamp = "";
    }


    public AnomalyDetector add(CpuUsage value) {
        this.nodeID = value.getNodeID();
        if (startTimeStamp == "")
            startTimeStamp = value.getTimestamp();
        if (timeArray[0] == -1) {
            timeArray[0] = value.getTimeCounter();
        }
        this.nodeID = value.getNodeID();
        timeArray[1] = value.getTimeCounter();
        endTimeStamp = value.getTimestamp();
        count = count + 1;
        return this;
    }

    public String getStartTimeStamp() {
        return startTimeStamp;
    }

    public String getEndTimeStamp() {
        return endTimeStamp;
    }

    public String getNodeId() {
        return this.nodeID;
    }

    public int[] getTimeArray() {
        return this.timeArray;
    }
}
