#!/bin/bash

echo "Start script:"

docker exec -it kafka bash -c "/opt/bitnami/kafka/bin/kafka-topics.sh --alter --bootstrap-server localhost:9092 --topic news --partitions 1" # You can only change the partition in a larger direction