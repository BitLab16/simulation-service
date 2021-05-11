FROM maven:3.6.3-adoptopenjdk-15 AS MAVEN_ENV
WORKDIR /build/
COPY pom.xml /build
COPY src /build/src
RUN mvn clean package -DskipTests=true

FROM adoptopenjdk/openjdk15:jre-15.0.2_7-alpine
COPY  --from=MAVEN_ENV /build/target/simulation-service-*jar-with-dependencies.jar app.jar
COPY data .
ENTRYPOINT ["java", "-jar", "app.jar"]
