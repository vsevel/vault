# Vault lab4
```

kubectl config set-context $(kubectl config current-context) --namespace=vault

./create-all.sh

kubectl create -f role.yaml

docker pull cfmanteiga/alpine-bash-curl-jq
kubectl create -f busybox.yaml
kubectl exec -it busybox sh

curl -s --cacert /var/run/secrets/kubernetes.io/serviceaccount/ca.crt \
    https://kubernetes.default.svc/api/v1/namespaces/vault/pods?labelSelector=app%3Dvault \
    | jq '.items[] | {ip: .status.podIP, name: .metadata.name}'

kubectl get pod -l app=vault -o=custom-columns=POD_IP:.status.podIP,NAME:.metadata.name

curl -s -k https://10-1-0-216.vault.pod.cluster.local:8200/v1/sys/seal-status | jq

curl -s -k --request PUT -d '{"secret_shares": 1,"secret_threshold": 1}' https://10-1-0-216.vault.pod.cluster.local:8200/v1/sys/init | jq

{
  "keys": [
    "142345e74aa55a602523f38167469e58c9f61be55825608c6c9a49a53f39989c"
  ],
  "keys_base64": [
    "FCNF50qlWmAlI/OBZ0aeWMn2G+VYJWCMbJpJpT85mJw="
  ],
  "root_token": "s.1SpG2yCxDxTCoP2IrouVaV8R"
}

curl -s -k --request PUT -d '{"key":"FCNF50qlWmAlI/OBZ0aeWMn2G+VYJWCMbJpJpT85mJw="}' https://10-1-0-216.vault.pod.cluster.local:8200/v1/sys/unseal | jq






./remove-all.sh

```

key=zXqoYiqm5jGbHBpUxURma0wKgNn7us+UrFy00ePDB2o=
token=s.5foeL86gCMfsbJ4qFs3ZVp8O
