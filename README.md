# demo-spring-microservice-with-teamcity

## Docker Image Build Command

```shell
docker build -t venkara/demo-spring-microservices-with-teamcity:1.0.0 .
```

## Docker Container Run Command
```shell
docker run --name demo-app -p 8080:8080 -d venkara/demo-spring-microservices-with-teamcity:1.0.0
```

## Publish Docker Image to Docker Hub
```shell
docker login

docker push venkara/demo-spring-microservices-with-teamcity:1.0.0
```
