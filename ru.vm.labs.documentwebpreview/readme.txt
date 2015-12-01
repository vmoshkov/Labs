demo-example of file preview

jre1.6.45 required

1) to run junit tests

mvn test

2) to run web form

mvn install -Dmaven.test.skip=true
copy documentwebpreview.war to the root directory where jetty-runner-8.1.0.jar exists
java -jar jetty-runner-8.1.0.jar --port 9090 documentwebpreview.war


