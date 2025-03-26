# Use the OpenJDK 21 JDK base image
FROM openjdk:21-jdk-slim

ARG DOCKER_PRODUCT_PORT

# Define runtime environment variables (ENV)
ENV DOCKER_PORT=${DOCKER_PRODUCT_PORT}

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file into the container
COPY target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE ${DOCKER_PORT}

# Run the application
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${DOCKER_PORT}"]

