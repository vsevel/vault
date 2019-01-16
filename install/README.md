# Installation
```
# Install xcode cli
xcode-select --install

# Install virtual box 6

# Install bash completion
brew install bash-completion

# Add the following line to your ~/.bash_profile:
[ -f /usr/local/etc/bash_completion ] && . /usr/local/etc/bash_completion

# Install minikube
brew cask install minikube

# Improve macos shell
http://osxdaily.com/2013/02/05/improve-terminal-appearance-mac-os-x/

# Start minikube
minikube start

# Use local images by re-using the Docker daemon
eval $(minikube docker-env)

# cfssl
brew install cfssl

# vault
brew install vault

# Install Helm
brew install kubernetes-helm.

# good to know
minikube ssh
minikube dashboard
docker rmi $(docker images -q)

# docker for mac
https://docs.docker.com/docker-for-mac/release-notes/#stable-releases-of-2018
activate kubernetes
kubectl config get-contexts
kubectl config use-context docker-for-desktop
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v1.10.1/src/deploy/recommended/kubernetes-dashboard.yaml
https://github.com/kubernetes/dashboard/wiki/Creating-sample-user
kubectl apply -f admin-user.yaml
kubectl apply -f admin-user-cluster-role.yaml
kubectl -n kube-system describe secret $(kubectl -n kube-system get secret | grep admin-user | awk '{print $1}')
    => token
kubectl proxy
access dashboard at: http://localhost:8001/api/v1/namespaces/kube-system/services/https:kubernetes-dashboard:/proxy/.

kubectl delete namespace vault
kubectl create namespace vault
kubectl config set-context $(kubectl config current-context) --namespace=vault

```


```
### route vs ingress
https://blog.openshift.com/kubernetes-ingress-vs-openshift-route/