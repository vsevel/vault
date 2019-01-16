# Vault lab1
```

create.sh

kubectl get all

pod=$(kubectl get pods --output=jsonpath={.items..metadata.name})
kubectl exec -it $pod -- /bin/sh
cat /vault/config/local.json
wget -qO - http://localhost:8200/v1/sys/health?uninitcode=200

port=$(kubectl get svc --output=jsonpath={.items..spec.ports..nodePort})
ip=$(minikube ip)
curl -v "http://$ip:$port/v1/sys/health"

kubectl exec -it $pod -- /bin/sh
export VAULT_ADDR=http://127.0.0.1:8200
vault status
vault operator init -key-shares=1 -key-threshold=1
    Unseal Key 1: xwIWkRAqjX0zs9HMJPNgwbSUtXGVMWv+7cyKF7TWoPQ=
    Initial Root Token: s.7NOhpOUC0xn0wZLAHMcYQfwF
vault operator unseal xwIWkRAqjX0zs9HMJPNgwbSUtXGVMWv+7cyKF7TWoPQ=
export VAULT_TOKEN=s.7NOhpOUC0xn0wZLAHMcYQfwF
vault write secret/toto password=titi
vault read secret/toto

delete.sh

```