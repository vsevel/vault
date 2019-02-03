#!/bin/bash

kubectl create configmap vault-config --from-file=vault-config.json
kubectl apply -f vault-pvc.yaml
kubectl apply -f vault-deployment.yaml
kubectl apply -f vault-service.yaml