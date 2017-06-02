# Introduction

This is simple chat web application named **Chatty**.

# Online demo

Demo version is deployed on Heroku and available under link: https://tranquil-retreat-49438.herokuapp.com/ 

# Architecture

Chatty uses STOMP over WebSocket protocol for chat messages processing. I have choosen STOP protocol because it is supported out 
of the box by Spring Framework used as technology base. For more sophisticated use cases it would be worth to consider XMPP protocol but due to limited time
and budget it was much easier to use STOMP.

Chatty manages internally STOMP topics subscriptions for proper messages routing.

There is simple REST API (maturity level 2) exposed for chats retrieval and creation.

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

## Front-end
* AngularJS 1.6
* Restangular
* Angular UI Router 
* Sock.JS
* Stomp.JS
* Twitter Bootstrap 3

## Tools
* Maven 3

# Manageability
Chatty exposes standard Spring Boot Actuator management endpoint. Additionaly chatty exposes some JMX endpoints for monitoring and management.
There is also JMX-HTTP bridge available via Jolokia under **/jolokia** path. To access any management HTTP endpint you need to sign-in as **admin** user.

# Configuration
Standard configuration properties are stored in **src/main/resources/application.yml** file. Configuration properties can be overriden by command line arguments or
environment variables.  

# Building

# Running
