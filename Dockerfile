FROM openjdk:8-jdk-alpine
LABEL maintainer="hnael99@gmail.com"
VOLUME /main-app
ADD build/libs/bloobloom-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar","/app.jar"]