FROM openjdk:17-jdk-slim
COPY target/*.jar GymProject.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "GymProject.jar"]
