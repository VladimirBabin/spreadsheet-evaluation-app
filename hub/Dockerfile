FROM maven:3.8-openjdk-17 AS build
RUN mkdir /src
WORKDIR /src
COPY ./ ./
RUN mvn clean package -DskipTests

FROM openjdk:17
RUN groupadd --system javauser && useradd -s /bin/false -g javauser javauser
RUN mkdir /app
COPY --from=build /src/target/Spreadsheet_Evaluator-0.0.1-SNAPSHOT.jar /app/java-application.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "java-application.jar"]
