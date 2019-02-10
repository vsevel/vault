kubectl config view
kubectl create ns vault
kubectl config set-context $(kubectl config current-context) --namespace=vault
kubectl patch sts consul-server --type json -p='[{"op": "remove", "path": "/spec/template/spec/affinity"}]'