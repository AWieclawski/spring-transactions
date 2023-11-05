# spring transactions API

 Rest API for exercise different combinations of transactions based on posgresql database 


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

  `{"firstName": "John", "lastName":"Smith"}`

- useful end-points:

 POST `localhost:8080/users/required-nested` 
 
 POST `localhost:8080/users/required-requiresnew`

 POST `localhost:8080/users/required-required`

 and after some posts:

 GET `localhost:8080/users/3`



