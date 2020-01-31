#!/bin/bash
cd /home/ubuntu/mimo-Game-server
kill `lsof -i -n -P | grep TCP | grep 8080 | tr -s " " "\n" | sed -n 2p`
