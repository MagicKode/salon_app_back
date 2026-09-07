FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

# Копируем собранный JAR-файл
COPY build/libs/*.jar app.jar

# ✅ Копируем Firebase ключ
COPY src/main/resources/firebase-service-account.json /app/firebase-service-account.json
# Открываем порт
EXPOSE 8085

# Запускаем с профилем docker
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=docker", "app.jar"]
