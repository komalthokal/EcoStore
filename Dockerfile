# Use OpenJDK 17 as base image
FROM openjdk:17


# Set working directory inside container
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build the project
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Copy the jar file
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
