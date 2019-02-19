#!/bin/bash

kubectl delete deployment --all
kubectl delete cm --all
kubectl delete svc --all
kubectl delete pod --all
kubectl delete pvc --all