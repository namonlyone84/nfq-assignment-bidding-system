## Assumtion
 - For the demonstration purpose and to reduce the complexity, authentication and authorization are not taken in account in this trial assignment
 - Main REST APIs are in BiddingController and JobController. UserController is created for testing purpose only.
 - To simulate the authorization and authentication, some RESTs just need to add "userId=" to the URI request.
 - Job terminology refer to "bidding item" in the requirement.

## Usage:
 - Import maven project then re-import to download and set up maven dependencies (prefer to use Intellij IDE)
 - The project is build on Springboot, so just run BiddingSystemApplication main entry to start server
 - Server automatically start up on port 8080. To change server port, modify "server.port" property in application.property
 - Test REST APIs with url specify in controllers.
	* For example to bid a job execute http request to:  http://localhost:8080/api/jobs/{jobId}/bidding?userId=1
 - Run IntegrationTest.java with the IDE to execute functional integration test.
 - Run LoadTest.java with the IDE to execute load test as in requirement.
 - "mvn package" also compile, package and run all tests of the application.

