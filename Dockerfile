FROM openjdk:8-jdk

COPY target/*.jar /app/service.jar

ENTRYPOINT ["java", "-cp", "/app/service.jar"]