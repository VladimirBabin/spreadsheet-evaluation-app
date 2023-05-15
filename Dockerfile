FROM maven:3.9.1-eclipse-temurin-17-alpine AS build
RUN mkdir /src
WORKDIR /src
COPY ./ ./
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
RUN addgroup -- sytem javauser && adduser -S -s /bin/false -G javauser javauser
RUN mkdir /app
COPY --from=build /scr/target/demo-0.0.1-SNAPSHOT.jar /app/demo.jar

