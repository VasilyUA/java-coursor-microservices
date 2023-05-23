#!/bin/bash

echo "Start script:"

docker exec -it kafka bash -c "/opt/bitnami/kafka/bin/kafka-console-producer.sh --broker-list localhost:9092 --topic user_topic"