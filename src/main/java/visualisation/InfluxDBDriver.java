package visualisation;

import anomalyDetector.models.CpuUsage;
import com.google.gson.Gson;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import util.TimeFormatter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class InfluxDBDriver {
    private InfluxDB influxDB;

    InfluxDBDriver(String host, String user, String password) {
        influxDB = InfluxDBFactory.connect(host, user, password);
        influxDB.setDatabase("cpu_data");
    }

    public void writePoint(String measurement, String jsonString) {
        CpuUsage cpuUsage = convertStringToObject(jsonString);
        Date utcTimestamp = TimeFormatter.stringToDate(cpuUsage.getTimestamp());
        Point point = Point.measurement(measurement)
                .time(utcTimestamp.getTime(), TimeUnit.MILLISECONDS)
                .tag("nodeID", cpuUsage.getNodeID())
                .addField("cpu-usage", cpuUsage.getCpu())
                .build();

        influxDB.write(point);
    }

    private CpuUsage convertStringToObject(String jsonString) {
        Gson gson = new Gson();
        return gson.fromJson(jsonString, CpuUsage.class);
    }

}
