package hello.models;

/**
 * Created by priyammitra on 7/13/17.
 */
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

}
