package hello.models;

public class CpuUsage {
    private int nodeID;
    private String timestamp;
    private double cpu;

    public CpuUsage(int nodeID, String timestamp, double cpu) {
        this.nodeID = nodeID;
        this.timestamp = timestamp;
        this.cpu = cpu;
    }

    public int getNodeID() {
        return nodeID;
    }

    public void setNodeID(int nodeID) {
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
