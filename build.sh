#!/bin/bash

# Build gateway
cd gateway
mvn clean install package

cd ../serviceregistry
mvn clean install package

cd ../restservice
mvn clean install package