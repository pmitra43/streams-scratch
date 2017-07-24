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


    public CpuAggregator(String nodeID, int count, String startTimeStamp, String endTimeStamp) {
        this.nodeID = nodeID;
        this.count = count;
        this.startTimeStamp = startTimeStamp;
        this.endTimeStamp = endTimeStamp;
    }

    public CpuAggregator add(CpuUsage value) {
        if (startTimeStamp == "")
            startTimeStamp = value.getTimestamp();
        if (timeArray[0]==-1){
            timeArray[0] = value.getTimeCounter();
        }

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
}
