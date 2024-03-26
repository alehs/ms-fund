FROM openjdk:17-jdk

# working directory inside the container
WORKDIR /app

COPY target/config-service-*.jar /app/config-service.jar


COPY docker/config/* /app/config/

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Command to run the application
CMD ["/bin/sh", "-c", "java -jar /app/config-service.jar"]
