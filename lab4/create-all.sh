#!/bin/bash


helm install --name consul ../../consul-helm/
kubectl patch sts consul-server --type json -p='[{"op": "remove", "path": "/spec/template/spec/affinity"}]'
kubectl delete pods -l component=server

source create-cert.sh
kubectl apply -f vault-pvc.yaml
kubectl apply -f vault-deployment.yaml
kubectl apply -f vault-service.yaml
kubectl apply -f vault-ingress.yaml

