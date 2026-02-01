FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copy only necessary files
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies first (faster builds)
RUN chmod +x mvnw && ./mvnw dependency:go-offline

# Copy source code
COPY src src

# Build the jar
RUN ./mvnw clean package -DskipTests

# Run the exact jar
CMD ["java", "-jar", "target/Book-0.0.1-SNAPSHOT.jar"]
