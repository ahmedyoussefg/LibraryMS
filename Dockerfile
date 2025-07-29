FROM maven:3.9.11-eclipse-temurin-21-alpine AS build

WORKDIR /app
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests dependency:copy-dependencies

FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/LibraryMS-1.0-SNAPSHOT.jar app.jar
COPY --from=build /app/target/dependency ./dependency

COPY .env .env

ENTRYPOINT ["java", "-cp", "app.jar:dependency/*", "org.Main"]
