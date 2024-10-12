# Dockerfile
FROM gradle:8.10.1-jdk17 AS build

WORKDIR /app

ARG FILE_DIRECTORY

COPY $FILE_DIRECTORY/build/libs/*SNAPSHOT.jar ./app.jar
COPY $FILE_DIRECTORY/src/main/resources/application.yml .
COPY $FILE_DIRECTORY/src/main/resources/application-prod.yml .

CMD ["java", "-jar", "app.jar", "--spring.config.location=file:/app/application.yml"]
