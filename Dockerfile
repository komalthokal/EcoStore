# Stage 1: Build with Maven + Java pre-installed
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only pom.xml first (helps caching)
COPY pom.xml .

# Download dependencies only (optional but faster)
RUN mvn dependency:go-offline

# Copy the rest of the source code
COPY src ./src

# Build the project (skip tests for faster build)
RUN mvn clean package -DskipTests

# Stage 2: Run Spring Boot app using lightweight JDK
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy the built JAR from the first stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java","-jar","app.jar"]
