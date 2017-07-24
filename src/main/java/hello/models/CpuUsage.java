package hello.models;

public class CpuUsage {
    private String cluster;
    private String nodeID;
    private String timestamp;
    private double cpu;
    private int timeCounter;

    public CpuUsage() {

    }

    public CpuUsage(String cluster, String nodeID, String timestamp, int timeCounter, double cpu) {
        this.cluster = cluster;
        this.nodeID = nodeID;
        this.timestamp = timestamp;
        this.timeCounter = timeCounter;
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

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public int getTimeCounter() {
        return timeCounter;
    }

    public void setTimeCounter(int timeCounter) {
        this.timeCounter = timeCounter;
    }
}
