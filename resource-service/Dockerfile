FROM openjdk:17-jdk

# working directory inside the container
WORKDIR /app

COPY target/resource-service-*.jar /app/resource-service.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Wait-for script to wait for MySQL to be available
COPY wait-for-it.sh /app/wait-for-it.sh
RUN chmod +x /app/wait-for-it.sh

# Command to run the application
CMD ["/bin/sh", "-c", "/app/wait-for-it.sh $MYSQL_HOST:$MYSQL_PORT -- java -jar resource-service.jar"]
