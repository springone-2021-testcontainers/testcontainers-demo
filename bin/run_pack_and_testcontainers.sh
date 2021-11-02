#!/usr/bin/env bash

if [[ -z ${DOCKER_HOST_IP} ]]; then
  read -p "Enter Docker host IP: " DOCKER_HOST_IP;
fi

#brew install socat
socat TCP-LISTEN:2375,reuseaddr,fork UNIX-CONNECT:/var/run/docker.sock &
## verify with:
#telnet ${DOCKER_HOST_IP} 2375

pack build springone-message-board \
    -e BP_MAVEN_BUILD_ARGUMENTS='-Dtest=Demo2_Toxiproxy_Tst test package'  \
    -e DOCKER_HOST=tcp://${DOCKER_HOST_IP}:2375

pkill socat
