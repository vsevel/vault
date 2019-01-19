# Vault lab2
```

kubectl config set-context $(kubectl config current-context) --namespace=vault

Install ingresscontroller on docker for mac (from nginx-ingress-controller/)
kubectl apply -f nginx-ingress-controller.yaml
see https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/provider/cloud-generic.yaml
kubectl apply -f cloud-generic.yaml

add "127.0.0.1       myvault.mycompany.io" to /etc/hosts

./create-all.sh

pod=$(kubectl get pods --output=jsonpath={.items..metadata.name})
kubectl exec -it $pod -- /bin/sh
export VAULT_CACERT=/var/run/secrets/kubernetes.io/serviceaccount/ca.crt
vault status -address=https://vault.vault.svc.cluster.local:8200
vault status -address=https://vault.vault:8200
vault status -address=https://vault:8200

vault operator init -key-shares=1 -key-threshold=1
    Unseal Key 1: I4CM5fuYdwdd85bPCCyzulXkXa3S+GjXiavt4r/CV1w=
    Initial Root Token: s.kKECwHyEZEaaM6xoq78f05Vl
vault operator unseal I4CM5fuYdwdd85bPCCyzulXkXa3S+GjXiavt4r/CV1w=
export VAULT_TOKEN=s.kKECwHyEZEaaM6xoq78f05Vl
vault write secret/toto password=titi
vault read secret/toto
exit

kubectl exec -it $pod -- cat /var/run/secrets/kubernetes.io/serviceaccount/ca.crt > work/ca.crt
openssl x509 -in work/ca.crt -text
curl --cacert work/ca.crt -v https://myvault.mycompany.io/v1/sys/seal-status

export VAULT_CACERT=work/ca.crt
vault status -address=https://myvault.mycompany.io

get the logs from the nginx ingress controller
controller=$(kubectl get pods -L "app.kubernetes.io/name=ingress-nginx" -n ingress-nginx --output=jsonpath={.items..metadata.name})
kubectl logs $controller -n ingress-nginx

./remove-all.sh

```