#!/bin/bash

su - jenkins
docker build -t kydybets/test /home/jenkins/workspace/test_docker
