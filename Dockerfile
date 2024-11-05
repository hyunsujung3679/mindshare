FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY main/build/libs/main-0.0.1-SNAPSHOT.jar mindshare.jar

ENV MYSQL_URL=jdbc:mysql://mindshare-1.c1waa6wu6dvf.ap-northeast-2.rds.amazonaws.com:3306/mindshare
ENV MYSQL_USERNAME=admin
ENV MYSQL_PASSWORD=rhakqjsro12!

ENTRYPOINT ["java", "-jar", "mindshare.jar"]