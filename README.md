# Web services application
This is a standalone web application based on a stack:
Guice DI, Guice servlet, Jetty embedded, Guice persist, Hibernate JPA, H2-in-memory database, Jersey, Jackson. All logs are directed to console.
There is no Spring at this time, folks. :)

## Run standalone

This command starts application on [the local address](http://localhost:8081/my-app/)

    gradle run
    
## Run tests

This task cleans working directories and starts test. This test lunches application and invokes many endpoints through rest client. Due that it outputs results to the log.  

    gradle