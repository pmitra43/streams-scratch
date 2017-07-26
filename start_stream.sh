#!/bin/bash

./gradlew runAnomalyDetectionKafkaStream &
./gradlew runCpuAnomalyProcessorDriver 
