FROM eclipse-temurin:17
ENV USERNAME_DB=$dbuser
ENV PASSWORD_DB=$dbpass
RUN echo $dbuser
WORKDIR /app
COPY target/demo-java.jar ./
EXPOSE 8000
CMD ["java", "-jar", "demo-java.jar"]