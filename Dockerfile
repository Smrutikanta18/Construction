FROM openjdk:17-jdk-slim
COPY target/Construction-0.0.1-SNAPSHOT.jar Construction-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "Construction-0.0.1-SNAPSHOT.jar"]
