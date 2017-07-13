package hello.models;

public class CpuAggregator {
    private String nodeID;
    private int count;
    private String startTimeStamp;
    private String endTimeStamp;

    public int getCount() {
        return count;
    }

    public CpuAggregator() {
        nodeID = "1";
        count = 0;
        startTimeStamp = "";
        endTimeStamp = "";
    }


    public CpuAggregator(String nodeID, int count, String startTimeStamp, String endTimeStamp) {
        this.nodeID = nodeID;
        this.count = count;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public CpuAggregator add(CpuUsage value) {
        if(startTimeStamp == "")
            startTimeStamp = value.getTimestamp();
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
}
