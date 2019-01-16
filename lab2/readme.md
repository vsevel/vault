# Vault lab2
```

kubectl config set-context $(kubectl config current-context) --namespace=vault

minikube ssh

#k8s-certs: /var/lib/minikube/certs/
#ca-certs: /etc/ssl/certs

#cat /var/lib/minikube/certs/ca.crt
#keytool -printcert -file ca.crt

create.sh

kubectl exec -it $(kubectl get pods --output=jsonpath={.items..metadata.name}) -- /bin/sh
export VAULT_CACERT=/var/run/secrets/kubernetes.io/serviceaccount/ca.crt
vault status -address=https://vault.vault.svc.cluster.local:8200
vault status -address=https://vault.vault:8200
vault status -address=https://vault:8200

vault operator init -key-shares=1 -key-threshold=1
    Unseal Key 1: L/vdTszLaemZSA2pyyyxpDefBrH5pNNWmZ4UCwOapWU=
    Initial Root Token: s.7Iqtd4loXwLyLFvszIXfQz01
vault operator unseal L/vdTszLaemZSA2pyyyxpDefBrH5pNNWmZ4UCwOapWU=
export VAULT_TOKEN=s.7Iqtd4loXwLyLFvszIXfQz01
vault write secret/toto password=titi
vault read secret/toto

Install ingress controller with pasthrough on minikube
    minikube addons enable ingress
    export SERVER_SECRET=vault-server-tls
    export CLIENT_SECRET=vault-client-tls
    export SERVER_CERT=tls.crt
    export SERVER_KEY=tls.key
    cp ca.crt tmp/vault-client-ca.crt
    cp vault-key.pem tmp/tls.key
    cp vault.crt tmp/tls.crt
    kubectl create secret generic $SERVER_SECRET --from-file=tmp/$SERVER_CERT --from-file=tmp/$SERVER_KEY
    kubectl create secret generic $CLIENT_SECRET --from-file=tmp/vault-client-ca.crt
    kubectl create -f vault-ingress.yaml
    helm install --set controller.extraArgs.enable-ssl-passthrough="" stable/nginx-ingress
    add "192.168.99.102  myvault" to /etc/hosts

Install ingresscontroller on docker for mac
kubectl apply -f nginx-ingress-controller/nginx-ingress-controller.yaml
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/master/deploy/provider/cloud-generic.yaml
running https on local machine: https://github.com/jnewland/local-dev-with-docker-for-mac-kubernetes

kubectl exec -it $(kubectl get pods -L "app=vault" --output=jsonpath={.items..metadata.name}) -- cat /var/run/secrets/kubernetes.io/serviceaccount/ca.crt > work/ca.crt

curl --cacert work/ca.crt -v https://myvault.mycompany.io/v1/sys/seal-status

export VAULT_CACERT=work/ca.crt
vault status -address=https://myvault.mycompany.io

kubectl logs $(kubectl get pods -L "app.kubernetes.io/name=ingress-nginx" -n ingress-nginx --output=jsonpath={.items..metadata.name}) -n ingress-nginx


delete.sh
```