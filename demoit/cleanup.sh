#!/bin/bash

helm delete $(helm ls --short) --purge

kubectl delete ingress --all -n vault
kubectl delete svc --all -n vault
kubectl delete deployment --all -n vault
kubectl delete secret --all -n vault
kubectl delete csr --all -n vault
kubectl delete cm --all -n vault
kubectl delete pvc --all -n vault
kubectl delete pod --all -n vault

kubectl delete ns vault

kubectl delete ingress --all -n default
kubectl delete svc --all -n default
kubectl delete deployment --all -n default

