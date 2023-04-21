# Documentation

## Service Registry
Eureka Service is running on port 9010. Gateway and Restservice are registering on this component.

## Gateway
Gateway is running on port 9000. It registers at serviceregistry and routes all incoming requests to the restservice.

## Rest Service
Rest service is running on a random port. It can be started in multiple instances and communicates via RabbitMQ with other instances.

## Start Application
In order to start the application, the `docker-compose.yml` can be used. It includes all necessery containers (RabbitMQ, Registry, Gateway, Restservice).
It can be built with `docker compose build` and started with `docker compose up`.

Beforehand the single applications must be built. Either with `mvn clean package install` in each directory or with by executing the `build.sh` script. 