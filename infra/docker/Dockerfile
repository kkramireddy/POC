# JDK
FROM openjdk:8
#Application artifact
ADD target/pdp-0.0.1-SNAPSHOT.jar /app/pdp/pdp-0.0.1-SNAPSHOT.jar
#Working Dirctory
WORKDIR /app/pdp
#Port Accessibility
EXPOSE 8080
#Container execution point
ENTRYPOINT ["java","-jar","pdp-0.0.1-SNAPSHOT.jar"]
