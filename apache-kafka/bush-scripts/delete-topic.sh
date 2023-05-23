#!/bin/bash

echo "Start script:"

docker exec -it kafka bash -c "/opt/bitnami/kafka/bin/kafka-topics.sh --delete --bootstrap-server localhost:9092 --topic news"

echo "delete topic"