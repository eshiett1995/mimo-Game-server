#!/bin/bash
cd /home/ubuntu/mimo-Game-server
kill $(lsof -t -i :8080)
nohup mvn spring-boot:run &
apt update
