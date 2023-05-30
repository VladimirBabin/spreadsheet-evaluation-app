FROM maven:3.8-openjdk-17 AS build
RUN mkdir /src
WORKDIR /src
COPY .. ./
RUN mvn clean package -DskipTests

FROM openjdk:17
RUN groupadd --system javauser && useradd -s /bin/false -g javauser javauser
RUN mkdir /app
COPY --from=build /src/target/spreadsheet-evaluator-swagger-0.0.1-SNAPSHOT.jar /app/java-application.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "java-application.jar"]
