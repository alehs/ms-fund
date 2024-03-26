FROM openjdk:17-jdk

# working directory inside the container
WORKDIR /app

COPY target/resource-processor-*.jar /app/resource-processor.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 8080

# Command to run the application
CMD ["/bin/sh", "-c", "java -jar /app/resource-processor.jar"]
