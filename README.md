# Introduction

This is simple chat web application named **Chatty**.

# Online demo

Demo version is deployed on Heroku and available under link: https://tranquil-retreat-49438.herokuapp.com/ 

# Architecture

Chatty uses STOMP over WebSocket protocol for chat messages processing. I have choosen STOP protocol because it is supported out 
of the box by Spring Framework used as base framework. For more sophisticated use cases it would be worth to consider XMPP protocol but due to limited time
and budget it was much easier to use STOMP.

Chatty manages internally STOMP user subscriptions for proper messages routing.

There is simple REST API exposed for chats retrieval and creation.

Chatty uses **RxJava** for asynchronous processing. I have choosen this approach because asynchronous applications scale well.

# Technologies
## Back-end
* Java 8
* RxJava
* Spring Boot
* Spring Security
* Spring Messaging
* Spring Data JPA
* Hibernate
* H2 Database
* Jolokia
* Slf4J/Logback
* Spring Actuator
* Spring Test
* JUnit
* Mockito
* Lombok

## Front-end
* AngularJS 1.6
* Restangular
* Angular UI Router 
* Sock.JS
* Stomp.JS
* Twitter Bootstrap 3

## Tools
* Maven 3
* Jacoco

# Manageability
Chatty exposes standard Spring Boot Actuator management endpoint. Additionaly chatty exposes some JMX endpoints for monitoring and management.
There is also JMX-HTTP bridge available via Jolokia under **/jolokia** path. To access any management HTTP endpint you need to sign-in as **admin** user.

# Configuration
Standard configuration properties are stored in **src/main/resources/application.yml** file. Configuration properties can be overriden by command line arguments or
environment variables.  

# Notes on building, testing and runing
You need  **maven 3** and **Java 8 JDK** installed to build Chatty or run tests.

# Building
To build Chatty just enter main project folder and run **mvn clean install**. This will build executable jar **target/chatty-[version].jar**.

# Testing
To run all unit and integration tests run **mvn clean verify** in root project folder. All test will be exexuted and test coverage will be stored in 
**target/site/jacoco** folder. Open **index.html** file to see test coverage report generated by **Jacoco**. 

# Running
To run Chatty execute command **java -jar chatty-[version].jar**.

You can also run Chatty via **spring-boot-maven-plugin**. Enter project root folder and run **mvn spring-boot:run**

Chatty uses port number **8080** by default. In case of port conflicts just override port configuration property by adding command line option:
* **--server.port=[port number]** in case of running it via **java**
* **-Dserver.port=[port number]** on case of running it via **maven**
