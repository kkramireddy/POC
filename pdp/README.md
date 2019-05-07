## TGT PDP RESTFul API
The intent of this project is to develop API using RESTful architecture style. This Application interacts with another API for getting product description and NO-SQL data store for product price details. One version of API is protected using OAuth2.0 standard. The services this API exposes are 

* __/products/{productId} : GET :__  It provides the product details (description and price) for the given product Id.
    * If prodcut is not found, it returns Http Status Code _NOT FOUND_. 
    * If proudct price is not found, it returns product description with empty price information.


* __/proudcts/{productId} : PUT :__ Updates the product price information with the details provided if proudct price details already available in the the system of record.
    * If product price details are not available already, it returns Http Status Code _NOT FOUND_. 
    * If both currency code and price are not given, it returns Http Status Code _BAD REQUEST_.

## Implementation
#### Tech Stack
> Java 1.8, SpringBoot 2.0, Spring Data JPA, Spring Retry, MongoDB, Project Lombok, Java Lambda, SonarQube, JaCoCo

#### Implementation Highlights
  * Integration with No-SQL Mongo DB using Spring Data JPA.
  * Consume RESTful service using Spring RestTemplate.
  * Use Project Lombok framework to eliminate boiler plate code.
  * Execute calls to systems outside the applicaton using Spring Retry  Template - to cover momentary network blips and other high availability issues.
  * Develop Automation Test Cases for both Unit and Integration Testing usign JUnit, Mockito, AssertJ and Spring MockMvc.
  * Load the initial proudct prcie data into Monog DB using Spring CommandLineRunner.
  * Generate Code Quality metrics using SonarQube.
  * Generate Automation Test Coverage reports using JaCoCo library and publish them to SonarQube.
  * Use Spring profile concept to create beans specific to environment.
  * Use Java Lambda features (Streams, functional interface and method reference) in writing code. 
  * Run the the App and MongoDB as containers on Docker platform.

## Launch Application
#### Pre-Requisites
    1. JDK 1.8
    2. Maven 3.5.x
    3. Access to MongoDB(local/remote)
    4. Git(for cloning)
    5. Postman for API testing
    6. Access to SonarQube(local/remote)
    7. Project lombok setup for IDE (https://projectlombok.org/)
#### Run Application in Workstation
##### Get Source Code
    1. Clone or download repository
    2. Import the project as mvn project into your favorite IDE
    3. Configure MongoDB datasource details in Application.properties
##### Run from IDE
    * Launch Application by right clicking on the class com.target.pdp.PdpApplication and Run As Java Project
##### Run from SHELL/CMD
```bash
    mvn clean package
    java -jar /[outoutdir]/pdp-0.0.1-SNAPSHOT.jar
    or 
    mvn spring-boot:run
```
##### Publish SonarQube results

Refer [SonarQubeDoc](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Maven) for SonarQube remote server configuration if not running locally

Execute command to publish code analysis and coverage report __mvn clean verify sonar:sonar__

##### Access the API
Open Postman

Import Collection by clicking on
[![Run in Postman](https://run.pstmn.io/button.svg)](https://app.getpostman.com/run-collection/9d225b99531b071585d2#?env%5Bawsip%5D=W3sia2V5IjoiYWNjZXNzVG9rZW5VUkwiLCJ2YWx1ZSI6Imh0dHBzOi8vZGV2LTk1NjcxMi5va3RhcHJldmlldy5jb20vb2F1dGgyL2RlZmF1bHQvdjEvdG9rZW4iLCJkZXNjcmlwdGlvbiI6IiIsImVuYWJsZWQiOnRydWV9LHsia2V5IjoiY2xpZW50SWQiLCJ2YWx1ZSI6IjBvYWtvYzN5NzlSdFVqczhuMGg3IiwiZGVzY3JpcHRpb24iOiIiLCJlbmFibGVkIjp0cnVlfSx7ImtleSI6ImNsaWVudFNlY3JldCIsInZhbHVlIjoiTkliaXZUWnNyTVNfQS1xQ3pza2pSYXRpNk04U1FvSnZ6Mk5ZZHg5QiIsImRlc2NyaXB0aW9uIjoiIiwiZW5hYmxlZCI6dHJ1ZX0seyJrZXkiOiJob3N0IiwidmFsdWUiOiJodHRwOi8vNTQuMTc0LjIxLjM0OjgwODAiLCJkZXNjcmlwdGlvbiI6IiIsImVuYWJsZWQiOnRydWV9XQ==)

If the above embedded link doesn't work, use the link [pdpcollection](https://www.getpostman.com/collections/9d225b99531b071585d2). This doesn't include Environment settings.

Select the Environment __awsapi__ for environment variables used in Collection.

For secured API access, select Authorization type as "OAuth2.0" and configure GetNewAccessToken details with Environment variables provided in __awsapi__ and obtain a token before submitting a request.

##### App Docker Image
```
docker pull kkramireddy/pdp:latest
```

## Implementation Tasks
* [x] Develop API
* [x] Automate Unit and Integration testing
* [X] Perform Static Code Analysis using SonarQube
* [X] Measure Automation Testing Coverage using JaCoCo
* [X] Document API details
* [X] Host the containerized API and MongoDB in AWS
* [X] Create Postman Collection
