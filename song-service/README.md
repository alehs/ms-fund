# Song Service

### Start the project
to start the project locally with embedded DB run the following command
```bash
 mvn spring-boot:run -Pdev
```

### Build the docker image

#### 1. Build an application artifact
```bash
mvn clean package
```

#### 2. Run the docker build command to create a docker image for song-service application
```bash
docker build -t song-service .
```
