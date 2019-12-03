FROM openjdk:8-jre-slim

WORKDIR /app

COPY ./target/monitor-0.0.1-SNAPSHOT.jar /app

EXPOSE 8080

CMD ["java", "-jar", "monitor-0.0.1-SNAPSHOT.jar"]