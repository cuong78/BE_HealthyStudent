# Use Maven to build the application
FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Use OpenJDK 21 for running the application
FROM openjdk:21
COPY --from=build /target/swagger-api-server.jar swagger-api-server.jar

# Expose port 8080
EXPOSE 8080

# Set the active Spring profile to production
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application
ENTRYPOINT ["java", "-jar", "swagger-api-server.jar"]
