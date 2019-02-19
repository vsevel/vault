# Installation
```
# Install xcode cli
xcode-select --install

# Install bash completion
brew install bash-completion

# Add the following line to your ~/.bash_profile:
[ -f /usr/local/etc/bash_completion ] && . /usr/local/etc/bash_completion

# Improve macos shell
http://osxdaily.com/2013/02/05/improve-terminal-appearance-mac-os-x/

# cfssl
brew install cfssl

# vault
brew install vault

# vault
brew install jq

# helm
brew install kubernetes-helm
# see https://docs.helm.sh/using_helm/
helm init

# good to know
docker rmi $(docker images -q)

# docker for mac
https://docs.docker.com/docker-for-mac/release-notes/#stable-releases-of-2018
activate kubernetes
kubectl config get-contexts
kubectl config use-context docker-for-desktop
see https://raw.githubusercontent.com/kubernetes/dashboard/v1.10.1/src/deploy/recommended/kubernetes-dashboard.yaml

# install dashboard 
kubectl apply -f kubernetes-dashboard.yaml
see https://github.com/kubernetes/dashboard/wiki/Creating-sample-user
kubectl apply -f admin-user.yaml
kubectl apply -f admin-user-cluster-role.yaml
kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep admin-user | awk '{print $1}')
    => token
kubectl proxy
access dashboard at: http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/.

# create and use vault namespace
kubectl create namespace vault
kubectl config set-context $(kubectl config current-context) --namespace=vault

# nginx ingress controller (docker for mac)
kubectl apply -f nginx-ingress-controller/nginx-ingress-controller.yaml
kubectl apply -f nginx-ingress-controller/cloud-generic.yaml

# nginx ingress controller (minikube)
https://kubernetes.github.io/ingress-nginx/deploy/#minikube

# demoit
https://golang.org/doc/install#macos
https://github.com/dgageot/demoit#install

# fetch consul helm chart
git clone https://github.com/helm/charts/tree/master/stable/consul
=> comment affinity from line 86 to 94


```
