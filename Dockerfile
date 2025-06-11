# Use a Java 17 base image
FROM eclipse-temurin:17-jdk

# Set working directory inside the container
WORKDIR /app

# Copy everything to /app in the container
COPY . .

# Build the app using Maven Wrapper
RUN ./mvnw clean package -DskipTests

# Expose the port your Spring Boot app uses
EXPOSE 8080

# Start the application
CMD ["java", "-jar", "target/*.jar"]
