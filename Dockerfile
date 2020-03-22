#### Stage 1: Build the application
FROM openjdk:8-jdk-alpine as build

# Set the current working directory inside the image
WORKDIR /evechargingsessions

# Copy maven executable to the image
COPY mvnw .
COPY .mvn .mvn

# Copy the pom.xml file
COPY pom.xml .

# Build all the dependencies in preparation to go offline.
# This is a separate step so the dependencies will be cached unless
# the pom.xml file has changed.
RUN ./mvnw dependency:go-offline -B

# Copy the project source
COPY src src

# Package the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#### Stage 2: A minimal docker image with command to run the app
FROM openjdk:8-jre-alpine

# Make port 8080 available to the world outside this container
EXPOSE 8080

ARG DEPENDENCY=/evechargingsessions/target/dependency

# Copy project dependencies from the build stage
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /evechargingsessions/lib
COPY --from=build ${DEPENDENCY}/META-INF /evechargingsessions/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /evechargingsessions

ENTRYPOINT ["java","-cp","evechargingsessions:evechargingsessions/lib/*","com.oguz.demo.evechargingsessions.EveChargingSessionsApplication"]