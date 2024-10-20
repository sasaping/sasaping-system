# Dockerfile
FROM openjdk:17-jdk-slim

# 시간대 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

WORKDIR /app

ARG FILE_DIRECTORY

COPY $FILE_DIRECTORY/build/libs/*.jar /app/app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
