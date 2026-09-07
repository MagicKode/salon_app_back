FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8086

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
