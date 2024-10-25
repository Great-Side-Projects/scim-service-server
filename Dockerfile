FROM openjdk:22-jdk-slim

ARG DATASOURCE_URL
ARG DATASOURCE_USERNAME
ARG DATASOURCE_PASSWORD

WORKDIR /app

COPY target/*.jar /app/scim-service-server.jar

EXPOSE 8080

ENV SPRING_DATASOURCE_URL=${DATASOURCE_URL} \
    SPRING_DATASOURCE_USERNAME=${DATASOURCE_USERNAME} \
    SPRING_DATASOURCE_PASSWORD=${DATASOURCE_PASSWORD}

ENTRYPOINT ["java", "-jar", "scim-service-server.jar"]