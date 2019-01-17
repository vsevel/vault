# Vault lab1
```

./create-all.sh

kubectl get all

pod=$(kubectl get pods --output=jsonpath={.items..metadata.name})
kubectl exec -it $pod -- /bin/sh
cat /vault/config/local.json
wget -qO - http://localhost:8200/v1/sys/health?uninitcode=200
exit

port=$(kubectl get svc --output=jsonpath={.items..spec.ports..nodePort})
curl -v "http://localhost:$port/v1/sys/health"

kubectl exec -it $pod -- /bin/sh
export VAULT_ADDR=http://127.0.0.1:8200
vault status
vault operator init -key-shares=1 -key-threshold=1
    Unseal Key 1: tJJnfemPL59ICAEGSc7D2jdR4TkKbpxylkWn7aGZzD4=
    Initial Root Token: s.1kuUziwDmvqm8LBuTv2JDhgu
vault operator unseal tJJnfemPL59ICAEGSc7D2jdR4TkKbpxylkWn7aGZzD4=
export VAULT_TOKEN=s.1kuUziwDmvqm8LBuTv2JDhgu
vault write secret/toto password=titi
vault read secret/toto
exit

./remove-all.sh

```