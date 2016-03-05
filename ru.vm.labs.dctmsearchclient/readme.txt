
This application demonstrates the usage of EMC Documentum DFC API along with DFC Query Builder.
To make this programm working:

1) find and open ...src/main/webapp/WEB-INF/classes/dfc.properties
2) set the valid connection parametes to EMC Documentum Content Server docbrocer (you should have an installed and runnung one!)
3) navigate to the root folder of the project 
4) find a pom.xml
5) mvn jetty:run-war
6) open a browser and navigate http://localhost:8080/test
7) type a word(s) which should be contained in the documents to find
8) run a search


technoligy stack: DFC Documentum, DFC Query Builder, maven-surefire-plugin (configure how to add additional libs), maven-resources-plugin (copy resources), CSS, jquery, jquery-datatable, javax.servlet
