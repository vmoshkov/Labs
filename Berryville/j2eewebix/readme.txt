
Car Desk Demo Application demonstrates a simple programm that provides the ability to create advertisements for a sell;

JRE 8 required.

To run this example


1. mvn clean install
2. mvn jetty:run
3. Open a browser and navigate to http://localhost:8080/j2eewebix/


Technology stack: webix, hibernate, javax.servlet, derby DB


**************

Папка db-derby-10.12.1.1-bin нужна для того чтобы обеспечить билд утилитой для доступа к базе, которая лежит в папке data/carsdeskdb

Для запуска утилиты работы с базой:
export DERBY_HOME=/home/administrator/workspace/Berryville/j2eewebix/db-derby-10.12.1.1-bin
export DERBY_INSTALL=/home/administrator/workspace/Berryville/j2eewebix/db-derby-10.12.1.1-bin
export CLASSPATH=$DERBY_INSTALL/lib/derby.jar:$DERBY_INSTALL/lib/derbynet.jar:$DERBY_INSTALL/lib/derbyclient.jar:$DERBY_INSTALL/lib/derbytools.jar:$DERBY_INSTALL/lib/derbyrun.jar:$CLASSPATH
java  org.apache.derby.tools.ij

connect 'jdbc:derby:/home/administrator/workspace/Berryville/j2eewebix/data/carsdeskdb;create=false';




















