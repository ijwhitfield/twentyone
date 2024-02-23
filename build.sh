#!/bin/bash
mvn clean install -f ./shared/
mvn clean install -f ./client/
mvn clean install -f ./server/
cp ./client/target/client-0.1.0.jar ./bin/client.jar
cp ./server/target/server-0.1.0.jar ./bin/server.jar
cp ./shared/target/shared-0.1.0.jar ./bin/shared.jar
