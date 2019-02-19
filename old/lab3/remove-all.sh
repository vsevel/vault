#!/bin/bash

helm delete $(helm ls --short) --purge

rm -f work/*
kubectl delete ingress --all
kubectl delete svc --all
kubectl delete deployment --all
kubectl delete secret --all
kubectl delete csr --all
kubectl delete cm --all
kubectl delete pvc --all


