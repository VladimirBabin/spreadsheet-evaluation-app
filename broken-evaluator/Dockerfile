FROM maven:3.8-openjdk-17
RUN groupadd --system javauser && useradd -s /bin/false -g javauser javauser
RUN mkdir /app
COPY /target/broken-evaluator-0.0.1-SNAPSHOT.jar /app/java-application.jar
WORKDIR /app
RUN chown -R javauser:javauser /app
USER javauser
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "java-application.jar"]
