package hello.serdes;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.models.CpuUsage;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

public class CpuUsageDeserializer implements Deserializer<CpuUsage> {
    @Override
    public void configure(Map map, boolean b) {

    }

    @Override
    public CpuUsage deserialize(String s, byte[] bytes) {
        ObjectMapper objectMapper = new ObjectMapper();
        CpuUsage cpuUsage = null;
        try {
            cpuUsage = objectMapper.readValue(bytes, CpuUsage.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cpuUsage;
    }

    @Override
    public void close() {

    }
}
