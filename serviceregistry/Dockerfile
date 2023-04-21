# Use a base image with Java 11 installed
FROM amazoncorretto:17-alpine-jdk

# Set the working directory to /app
WORKDIR /app

# Copy the necessary files to the container
COPY target/serviceregistry-1.0.0.jar /app

# Expose port 9010 for the service to listen on
EXPOSE 9010

# Set the command to run the service when the container starts
CMD ["java", "-jar", "serviceregistry-1.0.0.jar"]