FROM openjdk:8-jre-alpine
LABEL maintainer="imax"
VOLUME /app
EXPOSE 8080
ARG JAR_FILE=/target/azmoonet-0.0.1-SNAPSHOT.jar
ADD ${JAR_FILE} /app/azmoonet-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app/azmoonet-0.0.1-SNAPSHOT.jar"]
