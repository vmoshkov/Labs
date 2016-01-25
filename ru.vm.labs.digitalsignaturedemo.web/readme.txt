First of all, this sample doesn't work in any browser but IE. This behavoir is caused by promises. It is not used in this example.

jre7 is required to build and run this application

Steps to run:

1) open <your projects folder>/ru.vm.labs.digitalsignaturedemo.web/src/main/webapp/WEB-INF/web.xml
2) set the directory where the application's DB will be created 

<!-- where to place a database -->
	<context-param>
		<param-name>database_folder</param-name>
		<param-value> path to some folder </param-value>
	</context-param>
	
3) mvn install (in the project's root directory)
4) mvn jetty:run (in the project's root directory)
5) open InternetExplorer browser, navigate http://localhost:8080/test/
6) you should see now the application's main page

Steps to test:
a) save a document
b) after a document is saved it could be signed
c) click "sign document". select a certificate that could be used for signing(not all available certificates could be used for signing). 
The document hash technically will be signed.
d) now the document is signed. Thus, it could be verified.
e) click "verify signature". The signature should be valid. We verify the signature against the hash.
f) next change any input field and save the document again.
g) click "verify signature". Now the signature should be invalid. This is because fields has been changed. As the result the document hash was chaneched as well. 
But the signature stays the same. So it could not be verified against the new hash.

Technology stack: CryproPro cadesplugin, jquery, jquesry.datatable, css, javax.servlet, org.apache.commons.fileupload, json, Oracle Berkeley DB
