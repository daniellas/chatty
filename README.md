# Introduction

This is simple chat web application named **Chatty**.

# Online demo

Demo version is deployed on Heroku and available under link: https://tranquil-retreat-49438.herokuapp.com/ 

# Architecture

Chatty uses STOMP over WebSocket protocol for chat messages processing. I have choosen STOP protocol because it is supported out 
of the box by Spring Framework used as technology base. For more sophisticated use cases it would be worth to consider XMPP protocol but due to limited time
and bdudget it was much easier to use STOMP.

Chatty uses **RxJava** for asynchronous processing. I have choosen this approach because asynchronous applications scale well.

# Technologies
## Back-end
* Java8
* RxJava
* Spring Boot
* Spring Security
* Spring Messaging
* Spring Data JPA
* Hibernate
* Jolokia

## Fron-end
## Tools

# Manageability

# Building

# Running
