FROM maven:3.9.6-eclipse-temurin-17-focal AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=build /app/target/app.jar .
ENTRYPOINT ["java", "-jar", "app.jar"]