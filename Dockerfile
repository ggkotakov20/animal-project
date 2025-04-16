# Използваме официалния образ на OpenJDK
FROM openjdk:17-jdk-slim

# Копираме JAR файла в контейнера
COPY animal-project/target/animal-project-0.0.1-SNAPSHOT.jar /app.jar


# Настройваме командата за стартиране на приложението
ENTRYPOINT ["java", "-jar", "/app.jar"]
