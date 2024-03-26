FROM openjdk:17-jdk

# working directory inside the container
WORKDIR /app

COPY target/auth-server-*.jar /app/auth-server.jar

# Expose the port that your Spring Boot application will run on
EXPOSE 9100

# Command to run the application
CMD ["/bin/sh", "-c", "java -jar /app/auth-server.jar"]