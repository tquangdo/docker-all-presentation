# syntax=docker/dockerfile:1
FROM openjdk
#https://hub.docker.com/_/openjdk?tab=tags
#working directory 
WORKDIR /app
#copy from your Host(PC, laptop) to container
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
#Run this inside the image
RUN ./mvnw dependency:go-offline
COPY src ./src
#run inside container
CMD ["./mvnw", "spring-boot:run"]