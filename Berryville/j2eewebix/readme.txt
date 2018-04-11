
Car Desk Demo Application demonstrates a simple app that provides an ability to create advertisements for car's selling;

Requirements:
1) docker
2) maven
3) jre 8

To run this example:

a) create Docker image and start Docker container as discribed here - https://github.com/vmoshkov/Labs/tree/master/DerbyDockerImage

b) compile and run the application localy:
1. mvn clean install
2. mvn jetty:run
3. Open a browser and navigate to http://localhost:8080/j2eewebix/


Technology stack: webix, hibernate, javax.servlet, Apache Derby DB




















