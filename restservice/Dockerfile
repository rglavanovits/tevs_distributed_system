# Use a base image with Java 11 installed
FROM amazoncorretto:17-alpine-jdk

# Set the working directory to /app
WORKDIR /app

# Copy the necessary files to the container
COPY target/restservice-1.0.0.jar /app

# Expose port 8080 for the service to listen on
# EXPOSE 8080

# Set the command to run the service when the container starts
CMD ["java", "-jar", "restservice-1.0.0.jar"]