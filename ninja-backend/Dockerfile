# Use the official OpenJDK image, which is based on Linux Alpine
FROM openjdk:17-jdk-slim
# Set the working directory in the container
WORKDIR /app
# Copy the packaged jar file into the container
COPY target/*.jar app.jar
# Run the jar file
ENTRYPOINT ["java","-jar","/app/app.jar"]
# Expose the port
EXPOSE 8080
