FROM openjdk:17-jdk

# working directory inside the container
WORKDIR /app

COPY target/eureka-service-discovery-*.jar /app/eureka-service-discovery.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8761

# Command to run the application
ENTRYPOINT ["java","-jar","/app/eureka-service-discovery.jar"]

