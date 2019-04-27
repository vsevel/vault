
cleanup:
```
./cleanup.sh
```

create and use vault namespace:
```
kubectl create namespace vault
kubectl config set-context $(kubectl config current-context) --namespace=vault
```

start demoit:
```
./demoit.sh
```

Execute quarkus example (from ext-quarkus-vault):

start postgres:
```
mkdir /tmp/pgdata
docker run --rm --name mypostgres -it -p 5432:5432 -v /tmp/pgdata:/var/lib/postgresql/data -e POSTGRES_PASSWORD=bar postgres
```

build app
```
./mvnw clean package
docker build -f src/main/docker/Dockerfile.jvm -t quarkusapp .
```

Deploy in default ns
```
kubectl apply -f quarkusapp.yaml -n default
kubectl logs quarkusapp -n default
```

You should see: 
```
using password properties: {password=bar}
```

Cleanup
```
kubectl delete pod quarkusapp -n default
```