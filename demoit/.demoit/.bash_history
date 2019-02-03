kubectl config view
kubectl create ns vault
kubectl config set-context $(kubectl config current-context) --namespace=vault