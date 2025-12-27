FROM amazoncorretto:17-alpine-jdk
LABEL maintainer="rafi"
COPY build/libs/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar"]
