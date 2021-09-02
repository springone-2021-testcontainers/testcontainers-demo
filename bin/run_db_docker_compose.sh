#!/usr/bin/env bash

cat << EOF > tmp-stack.env
POSTGRESQL_USERNAME=postgres
POSTGRESQL_PASSWORD=iu4w78hj
POSTGRESQL_DATABASE=messageboard
EOF

cat << EOF > tmp-stack.yaml
version: '3.1'

services:

  db:
    image: bitnami/postgresql:latest
    restart: always
    env_file:
      - tmp-stack.env
    ports:
      - 5432:5432

  adminer:
    image: adminer
    restart: always
    ports:
      - 8080:8080
EOF

docker-compose -f tmp-stack.yaml up

rm tmp-stack.yaml
rm tmp-stack.env
