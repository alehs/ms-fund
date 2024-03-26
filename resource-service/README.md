# Resource Service

### Reference Documentation

### Run the application

to start the project locally with embedded DB run the following command
```bash
 mvn spring-boot:run -Pdev
```

### Run application as a Docker image

1. Build an application artifact
```bash
 mvn clean package
```
2. Run the docker build command to create a docker image for resource-service application
```bash
 docker build -t resource-service .
```
3. Create a custom bridge network
```bashmvn
docker network create resource-app-bridge
```
4. Start MySQL container
```bash
docker run -d \
--name mysql8 \
--network resource-app-bridge \
-v /path/to/config/mysql:/etc/mysql/conf.d \
-e MYSQL_DATABASE=as_resources \
-e TZ=Etc/UTC \
-e MYSQL_ROOT_PASSWORD=password \
-e MYSQL_AUTH_PLUGIN=caching_sha2_password \
-e MYSQL_USER_HOST='%' \
-p 3306:3306 \
mysql:latest \
mysqld --lower_case_table_names=1 --skip-ssl --character_set_server=utf8mb4 --explicit_defaults_for_timestamp
```
5. Start resource-app container
```bash
docker run -d \
--name resource-app \
--network resource-app-bridge \
-e JAVA_OPTIONS="-Xmx512m -Xms256m" \
-e SPRING_DATASOURCE_URL="jdbc:mysql://mysql8:3306/as_resources?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&allowPublicKeyRetrieval=true&useSSL=false" \
-e RESOURCE_METADATA_URL="http://songs-app:8081/" \
-e MYSQL_HOST=mysql8 \
-e MYSQL_PORT=3306 \
-p 8080:8080 \
resource-service
```

### Run application using Docker Compose
```bash
cd docker
docker compose -f mysql.yml up
docker compose -f app.yml up
```

### Run all applications using Docker Composer
from the project root directory run the following command
```bash
docker compose up
```
