# Vault lab3
```

kubectl config set-context $(kubectl config current-context) --namespace=vault

git clone https://github.com/hashicorp/consul-helm.git
cd consul-helm
git checkout v0.5.0
# see https://docs.helm.sh/using_helm/
helm init

helm install --name consul ./
kubectl patch sts consul-server --type json -p='[{"op": "remove", "path": "/spec/template/spec/affinity"}]'
kubectl delete pods -l 'component=server'

kubectl exec -it consul-server-0 -- /bin/sh
consul members
exit

kubectl exec -it consul-ctj4z -- /bin/sh

./remove-all.sh

```