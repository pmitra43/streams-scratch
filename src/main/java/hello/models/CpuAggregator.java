package hello.models;

public class CpuAggregator {
    private String nodeID;
    private int count;


    public CpuAggregator(){
    }

    public CpuAggregator(String nodeID, int count){
        this.nodeID = nodeID;
        this.count = count;
    }

    public CpuAggregator add(CpuUsage value)
    {
        count = count + 1;
        return this;
    }

    public String getNodeID() {
        return nodeID;
    }

    public int getCount() {
        return count;
    }
}
