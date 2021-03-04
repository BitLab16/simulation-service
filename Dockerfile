FROM openjdk:11-jre-slim

COPY target/simulation-service-1.0-SNAPSHOT-jar-with-dependencies.jar /app.jar

CMD ["java", "-jar", "/app.jar"]