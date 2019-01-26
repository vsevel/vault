# Vault lab3
```

kubectl config set-context $(kubectl config current-context) --namespace=vault

git clone https://github.com/hashicorp/consul-helm.git
consul_helm_path=`pwd`/consul-helm/
git checkout v0.5.0

./create-all.sh
kubectl get pods --watch

kubectl exec -it consul-server-0 sh
    consul members
exit

kubectl exec -it $(kubectl get pod -l component=client -o custom-columns=:metadata.name) sh
    consul members
    curl http://docker-for-desktop:8500/v1/status/leader
exit

kubectl port-forward consul-server-0 8500:8500

kubectl exec -it $(kubectl get pod -l app=vault -o custom-columns=:metadata.name) sh
    export VAULT_SKIP_VERIFY=true
    vault status
    vault operator init -key-shares=1 -key-threshold=1
    key=...
    token=...
    vault operator unseal $key
    export VAULT_TOKEN=$token
    vault write secret/toto password=titi
    vault read secret/toto
exit

kubectl scale --replicas=2 deployment vault

# unseal new standby vault pod and read value
    key=...
    token=...
    export VAULT_SKIP_VERIFY=true
    vault operator unseal $key
    export VAULT_TOKEN=$token
    vault read secret/toto
exit

curl -s -k https://myvault.mycompany.io/v1/sys/health | jq
curl -s -k --header "X-Vault-Token: $token" https://myvault.mycompany.io/v1/secret/toto | jq  '.data' 

./remove-all.sh

```

key=9xxrMfkh+Yky8oJETZIJkkQrXs2hsJKutIEEnpxuBHk=
token=s.3qJgLocJNjX5Qc7m8QJer1pZ