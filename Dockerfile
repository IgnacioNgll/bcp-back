
FROM openjdk:8-jre-alpine

WORKDIR /

RUN mkdir app
COPY target/proyBpc-0.0.1-SNAPSHOT.jar /app
WORKDIR /app

#Exponemos el puerto 8098
EXPOSE 8098


CMD ["java","-jar","proyBpc-0.0.1-SNAPSHOT.jar"]
