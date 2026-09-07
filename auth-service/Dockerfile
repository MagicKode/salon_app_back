FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY build/libs/app.jar app.jar

COPY src/main/resources/application-docker.yml /app/BOOT-INF/classes/application-docker.yml

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
