## TGT PDP RESTFul API
The intent of this project is to develop API using RESTful architecture style. This API exposes following services

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

## Launch Application
#### Pre-Requisites
    1. JDK 1.8
    2. Maven 3.5.x
    3. Access to MongoDB(local/remote)
    4. Git(for cloning)
    5. Postman for API testing
    6. Access to SonarQube(local/remote)
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

refer [SonarQubeDoc](https://docs.sonarqube.org/display/SCAN/Analyzing+with+SonarQube+Scanner+for+Maven) for SonarQube remote server configuration if not running locally

Execute command to publish code analysis and coverage report __mvn clean verify sonar:sonar__

##### Access the API
    * Open Postman
    * Get proudct details : http://localhost:8080/products/{productId} of type GET request
    * Update product price details : http://localhost:8080/products/{productId} of type PUT request
        * Body content in the format 
                {
                    "currencyCode":"USD",
                    "price":"19.22"
                }

## Implementation Tasks
* [x] Develop API
* [x] Develop Unit and Integration testing automation
* [X] Perform Static Code Analysis using SonarQube
* [X] Measure automation testing coverage using JaCoCo
* [X] Document API details
* [ ] Host the API in AWS
* [ ] Create Postman collection
