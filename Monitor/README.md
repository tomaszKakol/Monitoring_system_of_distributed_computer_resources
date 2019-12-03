############################### ######## Monitor README ######## ###############################

######## Technologies:

    Java

######## Libraries:

    Spring Boot

######## Import Maven Project in Eclipse:

    File > Import > Maven > Existing Maven Project
	
Make sure that there is path to JDK set. (Window > Preferences > Java > Installed JREs)

######## Working with project (In Eclipse):

Before you build your aplication set up monitorId, Api Gateway URL and Api endpoint for monitor:


To set monitorId:

	Modify variable MONITORID in file application.properties (src/main/resources)

To set Api Gateway URL (url Api Gateway where the POST requests for registration monitor should be sent)
	
	Modify variable API_GATEWAY_URL in file application.properties (src/main/resources) 

To set Api endpoint for monitor

	Modify variable API_ENDPOINT in file application.properties (src/main/resources)
	
To set Api endpoint for authorization (default (internal stub) "http://localhost:8080/v1/protected")

	Modify variable AUTH_SERVICE_URL in file application.properties (src/main/resources) 



To run project:

    Run As > Maven build
	Set goals: spring-boot:run

To clean project:

    Run As > Maven clean
	
To run tests:
	
	Run As > Maven test
	
######## Working with project (via console):

Before you build your aplication set up monitorId, Api Gateway URL and Api endpoint for monitor:


To set monitorId:

	Modify variable MONITORID in file application.properties (src/main/resources)

To set Api Gateway URL (your url Api Gateway where the POST requests for registration monitor should be sent)
	
	Modify variable API_GATEWAY_URL in file application.properties (src/main/resources) 

To set Api endpoint for monitor (default "http://localhost:8080/")

	Modify variable API_ENDPOINT in file application.properties (src/main/resources) 
	
To set Api endpoint for authorization (default (internal stub) "http://localhost:8080/v1/protected")

	Modify variable AUTH_SERVICE_URL in file application.properties (src/main/resources) 




To set monitorId:

	Modify variable MONITORID in file application.properties (src/main/resources)



To be able to run your Spring Boot app you will need to first build it. To build and package a Spring Boot app into a single executable Jar file with a Maven, 
use the below command. You will need to run it from the project folder which contains the pom.xml file:

	mvn install
	mvn install -DskipTests

Run Spring Boot app using Maven:

	mvn spring-boot:run

To run tests:

	mvn test
	
To clean the project:

	mvn clean
	
	
######## Using working application:

URL for monitor with "v1" as MONITORID :
	
	http://localhost:8080/v1/
	
Available endpoints:

	http://localhost:8080/v1/hosts (GET/POST)
	http://localhost:8080/v1/hosts/{id} (GET/DELETE)
	http://localhost:8080/v1/metrics (GET/POST)
	http://localhost:8080/v1/metrics/{id} (GET/DELETE)
	http://localhost:8080/v1/metrics/{id}/measurements (GET/POST)

Date format (string):

	"dd/MM/yyyy HH:mm:ss"
	
Implemented application is able to work with the same database or different databases for each monitor instance.

Present configuration in file "application.properties" uses ElephantSQL database which allows only one connection. 
To run aplication use your own Postgres database or one from five below:

#Database1:

	spring.datasource.url=jdbc:postgresql://isilo.db.elephantsql.com:5432/vegjsokh
	spring.datasource.username=vegjsokh
	spring.datasource.password=Zp9wCQA_wU5YNeR4LoGfF4N5903IzkC1

#Database2:

	spring.datasource.url=jdbc:postgresql://isilo.db.elephantsql.com:5432/snpbibhg
	spring.datasource.username=snpbibhg
	spring.datasource.password=UVnMOFHZagzIBbl6ycGBxAAIDHpPRDfo

#Database3:

	spring.datasource.url=jdbc:postgresql://isilo.db.elephantsql.com:5432/gnwawwon
	spring.datasource.username=gnwawwon
	spring.datasource.password=vFl82Hl7SZl05RnlRVN4G1PCGfarWi4L

#Database4:

	spring.datasource.url=jdbc:postgresql://isilo.db.elephantsql.com:5432/pimzarig
	spring.datasource.username=pimzarig
	spring.datasource.password=G25-WhwJTlFtq7r11Ddwmvp9yFsUgmTM

#Database5:

	spring.datasource.url=jdbc:postgresql://isilo.db.elephantsql.com:5432/gspdqqfw
	spring.datasource.username=gspdqqfw
	spring.datasource.password=UuhpK-l_1OcsROonjavXSpfXvJTkKP5_






The limitations of data in database are visible in file:

	src/main/resources/schema.sql
	
For now there limitations:

	host-id: 40 chars
	os: 255 chars
	monitor-id: 40 chars 
	metric-id: 40 chars
	type: 40 chars 
	unit: 40 chars 
	user-id: 40 chars 
	monitor-id: 40 chars 
	kind: 40 chars 
	val: 48 chars 
	ts: string in date format


The example contents of POST requests are visible in folder "examples"



	
	
