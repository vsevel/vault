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
exit

kubectl port-forward consul-server-0 8500:8500


./remove-all.sh

```