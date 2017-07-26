package anomalyDetector.models;

public class CpuUsage {
    private String cluster;
    private String nodeID;
    private String timestamp;
    private double cpu;
    private int timeCounter;

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

    public String getTimestamp() {
        return timestamp;
    }

    public double getCpu() {
        return cpu;
    }

    public int getTimeCounter() {
        return timeCounter;
    }

}
