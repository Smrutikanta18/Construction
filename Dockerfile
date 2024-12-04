FROM maven:3.8.8-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/Construction-0.0.1-SNAPSHOT.jar Construction-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "Construction-0.0.1-SNAPSHOT.jar"]
