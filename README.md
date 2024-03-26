### Testing

Test plan for the project is the following:

1. Unit tests coverage 80%
   - example: ResourceServiceTest
2. Integration tests
   - API tests using MockMVC: covering all endpoints
     - example: ResourcesControllerIT
   - Database integration tests using TestContainers: covering all repositories
3. Component tests: covering main business scenarios of each microservice
   - in our case resource-processor.process and resrouce-service.upload functionality could be covered. 
      - example:
4. Contract tests: this kind of tests overlaps with API integration tests. So one of them could be kept.
5. End-to-end test: covering interaction of all microservices end-to-end.
   - happy-path of uploading new file.
     - example: e2e folder




### Containerization task

Created docker containers for all the services:
- resource-service
- resource-processor
- song-service

common configuration was moved to the .evn file.
see docker-compose.yml for more details. 

to deploy containers run:
```bash
docker-compose up
```

### Eureka discovery task:

1. Created eureka-server application
2. To resource and songs applications added eureka client dependency spring-cloud-starter-netflix-eureka-client
3. Configured eureka client connection in application.properties
4. Updated docker compose to pass eureka server url to applications

To run:
build all projects and run the docker compose

1. Build eureka service:

```bash
cd eureka-service-discovery
mvn clean package
docker build -t eureka-service .
```

2. Build resources application:

```bash
cd resource-service
mvn clean package
docker build -t esource-service .
```

3. Build songs service application:

```bash
cd song-service
mvn clean package
docker build -t song-service.
```

4. Run all using docker compose

```bash
docker compose up
```

