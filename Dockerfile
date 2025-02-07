# Reference:
# - https://docs.docker.com/get-started/docker-concepts/building-images/multi-stage-builds/
# ==== BUILD STAGE ====
FROM eclipse-temurin:21 AS build
WORKDIR /app

# Copy Maven Wrapper and dependency files first (better caching)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw

# Download dependencies before adding source code (leveraging cache)
RUN ./mvnw dependency:go-offline

# Copy application source code and build the JAR
COPY src src
RUN ./mvnw clean package -DskipTests

# ==== RUNTIME STAGE ====
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app

# Reference: https://testdriven.io/tips/687229d1-9de8-4198-b3d7-d199a1440531/
# Create a system group (nonrootgroup) and a non-root user (nonrootuser) to avoid root execution
RUN addgroup -S nonrootgroup && adduser -S -G nonrootgroup nonrootuser


# Reference: https://stackoverflow.com/a/19547462
# Create the required directories and set permissions before switching users
RUN mkdir -p /app/data && chown -R nonrootuser:nonrootgroup /app && chmod -R 750 /app

# Switch to non-root user
USER nonrootuser

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8088

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
