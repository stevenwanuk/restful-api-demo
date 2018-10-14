## restful-api-demo

### Assignment

You are required to create a simple RESTful software service that will allow a merchant to create a new simple offer. Offers, once created, may be queried. After the period of time defined on the offer it should expire and further requests to query the offer should reflect that somehow. Before an offer has expired users may cancel it.


### Guidelines
- The solution should be written in Java, Scala
- The merchant should be able to interact with the service over HTTP
- No restrictions on external libraries
- Submit as a git repository (link to GitHub, BitBucket, etc)
- We are looking for a simple solution representative of an enterprise deliverable
- Use TDD
- Please pay attention to OO design; clean code, adherence to SOLID principles
- As a simplification offers may be persisted to a file, embedded database or held in memory

You can ignore authentication and authorization concerns


### assumptions

- valid offers are: not expired, not cancelled, 
- Users are only able to view / cancel / update their valid offers
- "Cancel" action sets "deleted" flag to "True"
  

### initial db data

sql file: /src/main/resources/import.sql


### tests

- Unit tests are under folder /src/test/

- Integration tests are under folder /src/integTest

- Acceptance tests are under folder /src/accepTest


### compile & run

>  gradle clean build integrationTest bootRun

>  http://localhost:8080


