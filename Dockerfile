FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY main/build/libs/main-0.0.1-SNAPSHOT.jar mindshare.jar
ENTRYPOINT ["java", "-jar", "mindshare.jar"]