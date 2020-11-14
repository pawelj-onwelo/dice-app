# Roll Dice Assignement solution

## Solution description
As a result of the assignment, a Springboot Application was created.

The application uses *Java 11*, *Spring Boot 2.3.5* and *Maven*.

**Assumptions:**
* currently, JPA is out-of-the-box configured for H2 DB for simplicity 
* configuration of rounding - it's hardcoded as HALF_UP

**Following topics were not considered during implementation:**
* business logic in DB queries - all calculations are made in Java to have single place for business logic
* endpoints security
* application delivery and deploy model (eg. containerization)
* scalability (it may be achieved by introducing simple load balancer)

### Run the project
To build application, please use maven command:
```shell script
mvn clean install
```

In order to run application you can use following options:
- after successful build, go to `target` folder and run following command: 
```shell script
java -jar ./dice-app.jar
```
- if you want to directly run Spring Boot from Maven, following command should be used: 
```shell script
mvn spring-boot:run
```

As Sprint Boot application is running, it can be reached on `http://localhost:8080` base URL.


### Available Endpoints

#### Roll Dice (Assignment 1)
Method: **POST**

URI: `/roll`

##### Request query params:
###### Required
* `dice` - amount of dice
* `sides` - amount of sides of each dice
* `rolls` - amount of rolls performed for all dice

###### Optional
none

##### Response:

###### HTTP 200
Array of objects that contains fields:
* `result` - total result 
* `amount` - amount of times given total was obtained in the simulation

###### HTTP 400
Improper entry data. Could be any of:
- improper dice amount: less or equal 0
- improper rolls amount: less or equal 0
- improper dice sides amount: less than 4

##### Example `curl` requests

```shell script
curl --location --request POST 'http://localhost:8080/roll?dice=2&rolls=10&sides=5'
```

```shell script
curl --location --request POST 'http://localhost:8080/roll?dice=3&rolls=100&sides=6'
```


#### Statistics (Assignment 3)
Both points are implemented as single endpoint for the purpose.
They may be simply separated as 2 separate - independent service methods are responsible for each assignment points.
By default, without optional params, the endpoint will provide just the total statistics.
When both optional request parameters are provided, then the response will be fulfilled with distribution section.

Method: **GET**

URI: `/statistics`

##### Request query params:

###### Required
none

###### Optional
* `dice` - amount of dice for which distribution should be calculated and returned
* `sides` - amount of sides of each dice for which distribution should be calculated and returned

##### Response:

###### HTTP 200
Object that contains:
* `total` - sectiion that contains total's statistics
* `distribution`- optional section that contains distribution information

###### HTTP 412
There were no statistics found

###### `total` section details
Array of objects that contains fields:
* `diceNo` - amount of dice
* `sidesNo` - amount of dice sides
* `totalSimulations` - total number of simulations
* `totalRolls` - total rolls made

###### `distribution` section details
Array of objects that contains fields:
* `sum` - sum result for whichc distribution is counted
* `percentage` - distribution percentage amount

##### Example `curl` requests

```shell script
curl --location --request GET 'http://localhost:8080/statistics'
```

```shell script
curl --location --request GET 'http://localhost:8080/statistics?dice=2&sides=5'
``` 



### Code Details
Application is a single monolith service, that offers complete solution.
It's designed using simple layered architecture, where we have:
* **API layer** - `com.avaloq.dice.app.api` package, which contains both controllers and transfer objects used for REST communication purpose. There is also Global exception handler to deal with exceptions -> HTTP status codes translation
* **Business logic layer** - `com.avaloq.dice.app.service` package, contains business logic of the application
* **Domain Model** - `com.avaloq.dice.app.model` package, contains domain classes used on sevice-business logic layer
* **Exceptions** - `com.avaloq.dice.app.exceptions` package, contains all business exceptions used in the solution

### Tests
Application has couple tests prepared in order to proof it's properly working. All of them can be found in `src/test/java` maven folder.

In order to run tests following script can be used:
 ```shell script
mvn clean test
 ```

<hr/>

## Assignement description

### Assignment 1: Create a Spring Boot application
Create a REST endpoint to execute a dice distribution simulation:
1. Roll 3 pieces of 6-sided dice a total of 100 times.
    1. For every roll sum the rolled number from the dice (the result will be between 3 and 18).
    2. Count how many times each total has been rolled.
    2. Return this as a JSON structure.
2. Make the number of dice, the sides of the dice and the total number of rolls configurable through query parameters.
3. Add input validation:
    1. The number of dice and the total number of rolls must be at least 1.
    2. The sides of a dice must be at least 4

### Assignment 3: Store the result of the simulation from Assignment 1 in a database

Create a REST endpoint that can query the stored data:
1. Return the total number of simulations and total rolls made, grouped by all existing dice number–dice side combinations.
    1. Eg. if there were two calls to the REST endpoint for 3 pieces of 6 sided dice, once with a total number of rolls of 100 and once with a total number of rolls of 200, then there were a total of 2 simulations, with a total of 300 rolls for this combination.
2. For a given dice number–dice side combination, return the relative distribution, compared to the total rolls, for all the simulations.
    1. In case of a total of 300 rolls, if the sum „3” was rolled 4 times, that would be 1.33%.
    2. If the sum „4” was rolled 3 times, that would be 1%.
    3. If the total „5” was rolled 11 times, that would be 3.66%. Etc...