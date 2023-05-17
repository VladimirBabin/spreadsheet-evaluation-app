FROM maven:3.9-eclipse-temurin-17-alpine AS build
RUN mkdir /src
WORKDIR /src
COPY ./ ./
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -- sytem javauser && adduser -S -s /bin/false -G javauser javauser
RUN mkdir /app
COPY --from=build /scr/target/Spreadsheet_Evaluator-0.0.1-SNAPSHOT.jar /app/java-application.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
EXPOSE 8081
ENTRYPOINT "java" "-jar" "java-application.jar"
