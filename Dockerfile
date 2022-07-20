FROM openjdk:17-jdk-alpine

ARG EXTRACTED

RUN addgroup -S spring && adduser -S spring -G spring
RUN mkdir -p /logs /archived-logs && chown -R spring:spring /logs /archived-logs
USER spring:spring

COPY ${EXTRACTED}/dependencies/ ./
COPY ${EXTRACTED}/spring-boot-loader/ ./
COPY ${EXTRACTED}/snapshot-dependencies/ ./
RUN true #https://stackoverflow.com/questions/51115856/docker-failed-to-export-image-failed-to-create-image-failed-to-get-layer
COPY ${EXTRACTED}/application/ ./

ENTRYPOINT ["java","org.springframework.boot.loader.JarLauncher"]
