#!/bin/bash

# kubectl create configmap vault-config --from-file=vault-config.json
# source create-cert.sh
# kubectl apply -f vault-pvc.yaml
# kubectl apply -f vault-deployment.yaml
# kubectl apply -f vault-service.yaml
# kubectl apply -f vault-ingress.yaml

helm install --name consul ./