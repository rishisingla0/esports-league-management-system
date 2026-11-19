# Stage 1: Build application
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY . .

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Run application
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

RUN groupadd --system spring \
    && useradd --system --gid spring spring

USER spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
