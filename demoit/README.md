```
# cleanup
./cleanup.sh

# create and use vault namespace
kubectl create namespace vault
kubectl config set-context $(kubectl config current-context) --namespace=vault

# start demoit
./demoit.sh
```