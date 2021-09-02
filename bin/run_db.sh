#!/usr/bin/env bash

docker run  -d --rm --name mb-postgres \
            -p 5432:5432 \
            -e POSTGRESQL_USERNAME=postgres \
            -e POSTGRESQL_PASSWORD=iu4w78hj \
            -e POSTGRESQL_DATABASE=messageboard \
            bitnami/postgresql:latest

docker ps

# docker stop mb-postgres
