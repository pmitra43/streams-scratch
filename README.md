# CPU Usage Anomaly Detection using Kafka Stream and Processor API
This is very basic anomaly detection example.
For continuous 10 seconds if cpu usage remains more than 80% then stream will send out alert.

## Requirements to build this project

1.    Java 8
2.    Gradle

## Requirements to run the examples

1.    [kafka](https://www.apache.org/dyn/closer.cgi?path=/kafka/0.11.0.0/kafka_2.11-0.11.0.0.tgz) 
2.    The [kafka-cpu-load-generator](https://github.com/pmitra43/kafka_load_generator.git) to simulate cpu usage load.


## Setup Instructions

#### Extact the kafka_2.11-0.11.0.0.tgz file ####
    tar -xvzf kafka_2.11-0.10.1.0.tgz


#### Start zookeeper and kafka
```
      kafka-install-dir/bin/zookeeper-server-start.sh kafka-install-dir/conf/zookeeper.properties
      kafka-install-dir/bin/kafka-server-start.sh kafka-install-dir/conf/server.properties
```

#### Install and start the kafka-cpu-load-generator
Clone the  [kafka-cpu-load-generator](https://github.com/pmitra43/kafka_load_generator.git) and start the jar.
```
    git clone https://github.com/pmitra43/kafka_load_generator.git
    cd kafka_load_generator/json-data-generator-1.2.2-SNAPSHOT
    java -jar json-data-generator-1.2.2-SNAPSHOT.jar cpuUsageConfig_firstCluster.json
```
Above generator will generate json data like this:
```
{"cluster":"firstCluster","nodeID":1,"cpu":92.9592,"timeCounter":462,"timestamp":"2017-07-26T16:02:15.931Z"}
{"cluster":"firstCluster","nodeID":1,"cpu":81.1288,"timeCounter":463,"timestamp":"2017-07-26T16:02:16.934Z"}
{"cluster":"firstCluster","nodeID":1,"cpu":94.1513,"timeCounter":464,"timestamp":"2017-07-26T16:02:17.939Z"}
{"cluster":"firstCluster","nodeID":1,"cpu":92.4383,"timeCounter":465,"timestamp":"2017-07-26T16:02:18.944Z"}
{"cluster":"firstCluster","nodeID":1,"cpu":98.3717,"timeCounter":466,"timestamp":"2017-07-26T16:02:19.946Z"}
```
#### Setup the kafka-streams repo
Clone or fork the repo
```
     git clone https://github.com/pmitra43/streams-scratch.git
     cd streams-scratch
```

### Start Anomaly detection kafka   ###
```
     cd streams-scratch
     ./gradlew runAnomalyDetectionKafkaStream &
     ./gradlew runCpuAnomalyProcessorDriver
```
OR
```
     cd streams-scratch
     sh start_stream.sh 
```

```AnomalyDetectionKafkaStreams``` will read the cpu usage into Kstream and performs groupBy, filter and aggregate operations.
 It sends the resulting output to ```actual-alert``` topic. It aggregate over 10sec of sliding window which advances by 1 second.
 
 ```CpuAnomalyFilterDriver``` reads messages from ```actual-alert``` topic and further filter out the the duplicate alerts in the same time window.
 It uses ```StateStore``` to store the alerts.

### Cpu usage consumer
```
   kafka-install-dir/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic cpu-usage
```

### Anomaly alerts consumer
```
   kafka-install-dir/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic filtered-anomaly-alerts
```

### 

