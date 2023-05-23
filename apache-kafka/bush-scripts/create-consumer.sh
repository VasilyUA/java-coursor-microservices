#!/bin/bash

echo "Start script:"

docker exec -it kafka bash -c "/opt/bitnami/kafka/bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic news --from-beginning"