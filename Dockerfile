# Stage 1: Build the application
FROM maven:3.8.7-eclipse-temurin-19 as build
WORKDIR /app
COPY pom.xml .
RUN ["/usr/local/bin/mvn-entrypoint.sh", "mvn", "verify", "clean", "--fail-never"]
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create the runtime image
FROM eclipse-temurin:19-jdk-jammy
VOLUME /tmp
COPY --from=build /app/target/hc-minimal-1.0.0.jar app.jar
ENV SERVER_SHUTDOWN=graceful
ENV SPRING_LIFECYCLE_TIMEOUT_PER_SHUTDOWN_PHASE=30s
ENTRYPOINT ["java", "-Xms2g", "-Xmx2g", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
