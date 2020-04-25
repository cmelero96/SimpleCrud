This is a simple example of a CRUD application following a RESTful microservices structure using Java & Spring Boot.

== Installation

This project has been developed using the https://www.eclipse.org/downloads/[Eclipse IDE] (Version 4.15.0) and using
https://www.oracle.com/java/technologies/javase-jdk8-downloads.html[Java 1.8].

Setup of the project is fairly simple. Follow these steps if you're using Eclipse IDE (should be similar for other IDEs):

 - Import this project as a Maven project in your IDE. In Eclipse, right-click in the Project Explorer then select 
Import... -> Existing Maven Projects, and then pick the directory the project is located at, then select its pom.xml.
 
 - Do a Maven Install of the project. In Eclipse, right-click on your project folder in the Project Explorer, then
select Run As... -> Maven install. Here you will also be able to see the Test results, which hopefully will all be OK.

 - Run the project as a Spring Boot App. If you can't see this option within the Run As... selection, then you need to
install the Spring Tools Suite 4 plugin for Eclipse (Help tab -> Eclipse Marketplace -> Find id there!)

Alternatively, you can run this project from the command line as follows:

 - Open a command line and go to the root folder (the one that contains the pom.xml file).

 - Do "mvn install" there (without the double quotes).

 - Run the project "java -jar target/test-0.0.1-SNAPSHOT.jar" (without the double quotes).

(Alternatively, you can do "mvn spring-boot:run")

In both cases, the app is configured to run in the 8080 port of your localhost. You can change the port in the application.properties file.

Special notes:

 - The base url of the application will thus be: localhost:8080/api/

 - You can also check the Swagger API, with your app running, in the url: localhost:8080/api/swagger-ui.html

 - You can consult the javadoc either through the "javadoc" shortcut on the root folder, or inside the javadoc folder therein.

=== Features

The application to develop has to manage a collection of users with the following information:

* Username (unique)
* Name
* Email
* Gender
* Picture (only URL value)

Users will be persisted to a database. You can use any database of your preference, relational or
not. Usage of a memory database or one integrated in the app itself is advised for simplicity.

The application will provide the following JSON web services:

* */api/user/ (GET)*: return the list of all users.
* */api/user/{username}/ (GET)*: return a single user.
* */api/user/ (POST)*: create a user.
* */api/user/{username}/ (PUT)*: update the information of a single user.
* */api/user/{username}/ (DELETE)*: delete a single user.
* */api/user/generate/{number}/ (GET)*: generate a number, provided as a parameter, of random users.
To create the users you have to use the https://randomuser.me[Random User Generator] service. Users
will be added to the collection of existing users.

Extra features:

* Unit tests (at least one class).
* Pagination of the users list.
* API documentation using Swagger and javadoc.
