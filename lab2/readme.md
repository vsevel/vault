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
kubectl exec -it $pod sh
    export VAULT_CACERT=/var/run/secrets/kubernetes.io/serviceaccount/ca.crt
    vault status -address=https://vault.vault.svc.cluster.local:8200
    vault status -address=https://vault.vault:8200
    vault status -address=https://vault:8200

    vault operator init -key-shares=1 -key-threshold=1
    vault operator unseal XXX
    export VAULT_TOKEN=XXX
    vault write secret/toto password=titi
    vault read secret/toto
exit

kubectl exec -it $pod -- cat /var/run/secrets/kubernetes.io/serviceaccount/ca.crt > work/ca.crt
openssl x509 -in work/ca.crt -text
curl --cacert work/ca.crt -s -v https://myvault.mycompany.io/v1/sys/seal-status | jq

export VAULT_CACERT=work/ca.crt
vault status -address=https://myvault.mycompany.io

# get the logs from the nginx ingress controller
controller=$(kubectl get pods -L "app.kubernetes.io/name=ingress-nginx" -n ingress-nginx --output=jsonpath={.items..metadata.name})
kubectl logs $controller -n ingress-nginx

./remove-all.sh

```

Unseal Key 1: Ro69IeXEW+5rzYr2oKTLXng1Y/DNuya4XyLE2x/m5SM=

Initial Root Token: s.1xtK9Wy0RDe6WlAEoGQOJz5u