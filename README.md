# Demo project using Axon Framework

* By [Maxym Mykhalchuk](https://blog.maxym.dp.ua)
* Built on [Spring Boot 3](https://spring.io/projects/spring-boot) / [Spring Framework 6](https://spring.io)
* Using [Axon Framework 4.7.0](https://developer.axoniq.io/axon-framework/overview)

### To Start

* Start Axon Server on default ports 8024 and 8124, e.g. `docker run -p 8024:8024 -p 8124:8124 -d axoniq/axonserver:4.6.10-jdk-17-dev-nonroot`
* Start all the services `./gradlew bootRun --parallel --max-workers 6`
* Open [User Service UI](http://localhost:8080), [Inventory Service UI](http://localhost:8081) and [Order Service UI](http://localhost:8082)