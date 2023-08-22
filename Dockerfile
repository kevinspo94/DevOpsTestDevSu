FROM openjdk:17
WORKDIR /app
COPY target/demo-java.jar ./
EXPOSE 8000
CMD ["java", "-jar", "demo-java.jar"]