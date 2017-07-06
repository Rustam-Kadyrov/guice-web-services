[![Build Status](https://travis-ci.org/Rustam-Kadyrov/guice-web-services.svg?branch=master)](https://travis-ci.org/Rustam-Kadyrov/guice-web-services)

# Web services application
This is a standalone web application based on a stack:
Guice DI, Guice servlet, Jetty embedded, Guice persist, Hibernate JPA, H2-in-memory database, Jersey, Jackson. All logs are directed to console.
There is no Spring at this time, folks. :)

## Description
Application contains user and account endpoints and allows to manage users, it's accounts, recharge account with money and do transfers between accounts.
Validations are presented. To find out RESTful contract see [this test](server/src/test/java/com/rustam/project/ApplicationTest.java) which invokes application's endpoints. 

## Run standalone

This command starts application on [the local address](http://localhost:8081/my-app/)

    gradle run
    
## Run tests

This task cleans working directories and starts test. This test lunches application and invokes many endpoints through rest client. Due to that it outputs results to the log.  

    gradle
