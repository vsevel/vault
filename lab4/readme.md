# Vault lab4
```

kubectl config set-context $(kubectl config current-context) --namespace=vault

./create-all.sh

kubectl create -f role-pod-reader.yaml
kubectl create -f cluster-role-auth-delegator.yaml

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



jwt=$(kubectl exec vault-85f8bb65df-7499s -- cat /var/run/secrets/kubernetes.io/serviceaccount/token)
kubectl exec vault-85f8bb65df-7499s -- cat /var/run/secrets/kubernetes.io/serviceaccount/ca.crt >> work/ca.crt
export VAULT_TOKEN=s.1SpG2yCxDxTCoP2IrouVaV8R
export VAULT_SKIP_VERIFY=true
export VAULT_ADDR=https://myvault.mycompany.io
vault auth enable kubernetes
vault write auth/kubernetes/config token_reviewer_jwt=$jwt kubernetes_host=https://kubernetes.default.svc kubernetes_ca_cert=@work/ca.crt
vault read auth/kubernetes/config
vault policy write spring-native-example spring-native-example.hcl
vault write auth/kubernetes/role/spring-native-example bound_service_account_names='*' bound_service_account_namespaces='*' policies=spring-native-example ttl=2h
vault write secret/spring-native-example password=pwd
vault read secret/spring-native-example

jwt=$(kubectl exec busybox -- cat /var/run/secrets/kubernetes.io/serviceaccount/token)
vault write auth/kubernetes/login role=spring-native-example jwt=$jwt
export VAULT_TOKEN=s.13qaRWW9bTySrObShqTUVjnO
vault read secret/spring-native-example


./remove-all.sh

```
