#!/bin/bash

set -e

mvn clean install
mkdir target/docker
cp src/main/docker/* target/docker
cp target/vault-demo-0.0.1-SNAPSHOT.jar target/docker
docker build target/docker -t vault-demo
kubectl delete -f vault-demo.yaml
kubectl apply -f vault-demo.yaml