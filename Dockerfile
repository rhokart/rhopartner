FROM openjdk:17-alpine
EXPOSE 8086
COPY target/rhopartner-0.0.1-SNAPSHOT.jar rhopartner.jar
ENTRYPOINT ["java","-jar","rhopartner.jar"]
