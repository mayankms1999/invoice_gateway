# Use Eclipse Temurin JDK 21 as the base image
FROM eclipse-temurin:21-jdk

ARG DOCKER_PRODUCT_PORT
ARG PRODUCT_DB_USER
ARG PRODUCT_DB_PASS

# Define runtime environment variables (ENV)
ENV DOCKER_PORT=${DOCKER_PRODUCT_PORT}
ENV DB_USER=${PRODUCT_DB_USER}
ENV DB_PASS=${PRODUCT_DB_PASS}

# Set the working directory inside the container
WORKDIR /app
# Copy the Maven build JAR file into the container
COPY target/product-service-1.0.0.jar app.jar
# Expose the port the app runs on
EXPOSE ${DOCKER_PORT}
# Run the application
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${DOCKER_PORT} --spring.datasource.username=${DB_USER} --spring.datasource.password=${DB_PASS}"]
