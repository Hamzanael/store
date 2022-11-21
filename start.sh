#!/usr/bin/env bash
if ! docker info > /dev/null 2>&1; then
  echo "This script uses docker, and it isn't running - please start docker and try again!"
  exit 1
fi
echo "Starting the docker containers..."
docker-compose up -d
echo "Starting the server..."
./gradlew bootRun