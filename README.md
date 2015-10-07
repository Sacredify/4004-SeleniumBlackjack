# 4004-SeleniumBlackjack
A basic blackjack web application to get experience using selenium as a testing method.

-- REPOSITORY CURRENTLY PRIVATE.


Some code taken from the web (spring documentation), see:

https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples/spring-boot-sample-websocket-tomcat


To build this project
---------------------

Building this project requires git (or download it manually), and maven. 

  1. `git clone https://github.com/Sacredify/4004-SeleniumBlackjack.git`
  2. `cd 4004-SeleniumBlackjack`
  3. `mvn clean install`
  

To run this project
-------------------

Build the project as above to assemble the stand-alone jar.

  1. `java -jar SeleniumBlackjack-1.0-SNAPSHOT.jar`

Allow the code to run until you see this message:
  
  `... c.c.blackjack.BlackJackApplication       : Started BlackJackApplication in 4.512 seconds`
  
After that, open any browser and navigate to:

  `http://localhost:8080/`

Running the tests
-----------------

Running the tests (selenium) is best done through maven:

  `mvn clean test`
  
Tests aren't run as part of the build process, although they are compiled for errors.
