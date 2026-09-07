FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Копируем собранный JAR
COPY build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
