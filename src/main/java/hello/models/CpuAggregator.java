package hello.models;

public class CpuAggregator {
    private String nodeID;
    private int count;
    private int[] timeArray;
    private String startTimeStamp;
    private String endTimeStamp;

    public int getCount() {
        return count;
    }

    public CpuAggregator() {
        nodeID = "1";
        count = 0;
        timeArray = new int[2];
        timeArray[0] = -1;
        timeArray[1] = -1;
        startTimeStamp = "";
        endTimeStamp = "";
    }

//    public CpuAggregator(String nodeID, int count, String startTimeStamp, String endTimeStamp, int[] timeArray) {
//        this.nodeID = nodeID;
//        this.count = count;
//        this.startTimeStamp = startTimeStamp;
//        this.endTimeStamp = endTimeStamp;
//        this.timeArray = timeArray;
//    }

    public CpuAggregator add(CpuUsage value) {
        if (startTimeStamp == "")
            startTimeStamp = value.getTimestamp();
        if (timeArray[0]==-1){
            timeArray[0] = value.getTimeCounter();
        }
        this.nodeID = value.getNodeID();
        timeArray[1]=value.getTimeCounter();
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

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }
}
