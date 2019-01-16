#!/bin/bash

kubectl delete pod --all
kubectl delete deployment --all
kubectl delete cm --all
kubectl delete pvc --all
kubectl delete svc --all