package hello.models;

public class CpuUsage {
    private String nodeID;
    private String timestamp;
    private double cpu;

    public CpuUsage(){

    }

    public CpuUsage(String nodeID, String timestamp, double cpu) {
        this.nodeID = nodeID;
        this.timestamp = timestamp;
        this.cpu = cpu;
    }

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getCpu() {
        return cpu;
    }

    public void setCpu(double cpu) {
        this.cpu = cpu;
    }
}
