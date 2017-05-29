#!/bin/bash

su - root
docker build -t kydybets/test /home/jenkins/workspace/test_docker
