FROM eclipse-temurin:17.0.8_7-jdk-alpine
ARG dbuser
ARG dbpass
ARG port=8000
ENV USERNAME_DB=$dbuser
ENV PASSWORD_DB=$dbpass
ENV PORT=$port
WORKDIR /app
COPY target/demo-java.jar ./
EXPOSE $PORT
CMD ["java", "-jar", "demo-java.jar"]
HEALTHCHECK --interval=60s --retries=5 --start-period=15s --timeout=10s CMD wget --no-verbose --tries=1 --spider "localhost:$PORT/api/actuator/health" || exit 1