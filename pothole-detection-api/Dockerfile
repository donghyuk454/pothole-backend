FROM eclipse-temurin:17-jdk-focal
WORKDIR /pothole-detection-api
COPY ./build/libs/*.jar pothole-detection-api.jar
ENTRYPOINT ["java", "-Dspring.profiles.active=detection-dev", "-jar" , "pothole-detection-api.jar"]