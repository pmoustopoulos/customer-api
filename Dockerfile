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

# Create a system group and user (to avoid root execution)
RUN addgroup -S appuser && adduser -S -G appuser appuser

# Create the required directories and set permissions before switching users
RUN mkdir -p /app/data && chown -R appuser:appuser /app && chmod -R 750 /app

# Switch to non-root user
USER appuser

# Copy only the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the application port
EXPOSE 8088

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
