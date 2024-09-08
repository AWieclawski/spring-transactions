# spring transactions API

 Rest API for exercise different combinations of transactions based on postgresql database.
 Used typical JPA Repositories and customised generic Criteria Builder Dao to search even by values of field in Embedded Entity.
 Criteria Builder Dao recognise if field is Entity with Many-To-One relation to generic Entity of Dao.
 Search logic has been implemented without knowing the entity identifiers used in the database, 
 that requires the use of other unique values and ensures their uniqueness. 


## Requirements:

 - JDK 11 
 -- vide: `https://itslinuxfoss.com/how-to-fix-the-java-command-not-found-in-linux/`

 - Maven 
 -- vide: `https://linuxhint.com/mvn-command-found/`

 - Linux OS (Ubuntu)

 - IDE for JDK with Lombok and Spring plugins 
 -- vide: `https://www.eclipse.org/downloads/packages/`
   (ex. standalone "Eclipse IDE for Enterprise Java and Web Developers" with soft link on the desktop) 
 -- vide: `https://www.baeldung.com/lombok-ide`	
 

## Run:

1. Clone repository using: 
```
	$ git clone <repository-url> 
```
2. Get in main `pom.xml` directory and type in CLI:   
```
	$ mvn clean package -DskipTests
```
2. Get in `.../target/` directory and type in CLI:
```
	$ java -jar *-SNAPSHOT.jar
``` 


## Examples of:

- body:

  `{"firstName": "John", "lastName":"Smith", "email": "johnsmith@gmail.com", "login": "johnsmith", "address": {"city": "New York", "postalCode":"41-641", "country": "USA"}}`

- useful end-points:

 POST `localhost:8080/users/save`

 POST `localhost:8080/users/required-nested` 
 
 POST `localhost:8080/users/required-requiresnew`

 POST `localhost:8080/users/required-required`

 and after some posts:

 GET `localhost:8080/users/3`

 POST `localhost:8080/users/bycitynames` body: `["Chicago", "Moscow"]`

 POST `localhost:8080/users/bycountrynames` body: `["USA", "Poland"]`

 POST `localhost:8080/users/map/bycitynames` body: `["New York", "Kursk"]`

 POST `localhost:8080/users/map/bycountrynames` body: `["USA", "Russia"]`

 POST `localhost:8080/users/byemailcontent` body: `{"content": "@gmail."}`



