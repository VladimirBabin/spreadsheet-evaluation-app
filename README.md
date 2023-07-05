# Spreadsheet Evaluator App

This is a dockerized multimoduled Spring Boot project that bases on the Wix Growth Internship task for 2023 - to implement an application for downloading and computing a spreadsheet and sending it for verification (you can find more details on the task itself by navigating to Task.pdf in this directory). 

This project goes a little further then the task itself (which was implemented in evaluator module) and creates a multimodule app consisting of services for distributing the spreadsheet for the task (hub), verifying the computed spreadsheet (validator), as well as working and broken implementations of evaluator app and a swagger module for interactive control over the whole project.

## Requirements
For building and running the application you need:
- JDK 17.0.1
- Maven 3.8.7
- Docker 24.0.2

## Running the application locally
#### You can run the application via your IDE by starting all the services. 
To see the outputs for the working and broken implementations of evaluator you can open http://localhost:8090/swagger-ui/index.html#/ in your browser and run get commands in Swagger delegating to both implementations.

#### Alternatively you can run the app in Docker containers.
First, make sure to add hosts for the services' names in the /etc/hosts directory like in this example:

127.0.0.1       hub                       <br>
127.0.0.1       verificator               <br>
127.0.0.1       broken-verificator        <br>
127.0.0.1       swagger                   <br>
127.0.0.1       validator

Then run clean install commands with maven to build the project and start the docker containers in your IDE or with the following commands:

$ mvn clean install -DskipTests           <br>
$ docker-compose up -d

After the containers are up, you can repeat the process for opening Swagger in your browser and running the execute commands there. You can also inspect the computations for the evaluators apps with docker log commands.