# Use stable OpenJDK 17
FROM eclipse-temurin:17-jdk

# Set working directory
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Install Maven and build project
RUN apt-get update && apt-get install -y maven
RUN mvn clean package -DskipTests

# Copy the built jar
COPY target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]
