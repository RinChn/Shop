FROM maven:3.9.5-eclipse-temurin-17 as builder
WORKDIR /app
COPY mvnw pom.xml ./
COPY ./src ./src
RUN mvn clean install -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
EXPOSE 8080
COPY --from=builder /app/target/*.jar /app/*.jar
COPY src/main/resources/parameters/ExchangeRate.json /app/src/main/resources/parameters/ExchangeRate.json
ENTRYPOINT ["java", "-jar", "/app/*.jar"]