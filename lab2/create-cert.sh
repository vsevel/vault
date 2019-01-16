#!/bin/bash

mkdir work
cp csr.json work
pushd work

# Create private key and CSR
cfssl genkey csr.json | cfssljson -bare vault

# Create CSR k8s object
cat <<EOF | kubectl create -f -
apiVersion: certificates.k8s.io/v1beta1
kind: CertificateSigningRequest
metadata:
  name: vault.vault
spec:
  groups:
  - system:authenticated
  request: $(cat vault.csr | base64 | tr -d '\n')
  usages:
  - digital signature
  - key encipherment
  - server auth
EOF

# Approve certificate
kubectl certificate approve vault.vault

# Download public key
kubectl get csr vault.vault -o jsonpath='{.status.certificate}' | base64 --decode > vault.crt

cp vault-key.pem tls.key
cp vault.crt tls.crt
kubectl create secret tls vault-tls --key ./tls.key --cert ./tls.crt

# Display public key content
keytool -printcert -file tls.crt
  #Propriétaire : CN=vault.vault.svc.cluster.local
  #Emetteur : CN=kubernetes

popd