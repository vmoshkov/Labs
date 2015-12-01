package ru.vm.labs.documentwebpreview;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.aspose.words.DocumentBuilder;
import com.aspose.words.License;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

/*
 * Servlet which gets filename as input parameter
 * This file is transformed to pdf
 * Servlet returns this PDF-file to a client
 */
public class GetContent extends HttpServlet {
	
	private static final Logger logger = LogManager.getLogger(GetContent.class);
	
	private static String WORD_TEMPLATE = "files/demo.docx";
	private static String LICENSE_FILE_NAME = "Aspose.Total.Java.lic";
	
	public void init() throws ServletException
	  {
		  logger.debug("method init() is invoked.");	
		  
	      // Do required initialization
	            
	      try
	      {
	    	  License license = new License();
	          license.setLicense(this.getClass().getClassLoader().getResourceAsStream(LICENSE_FILE_NAME));
	          System.out.println("License set successfully.");
	        }
	        catch (Exception e)
	        {
	            // We do not ship any license with this example, visit the Aspose site to obtain either a temporary or permanent license.
	            System.out.println("There was an error setting the license: " + e.getMessage());
	            logger.error(e);
	        }
	  }

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ByteArrayOutputStream resultStream = new ByteArrayOutputStream();

		logger.debug("method GetContent::doGet() is invoked.");

		String fileName = request.getParameter("filename");

		logger.debug("request param fileName=" + fileName);
		
		fileName = WORD_TEMPLATE; // now always default value
		
		/* Try to load and save into stream a document */
		try {
								
			// Load the document from the resources.
			Document doc = new Document(this.getClass().getClassLoader().getResourceAsStream(fileName));

			// Save a document as PDF to the destination stream
			doc.save(resultStream, SaveFormat.PDF);

			logger.debug("loaded document filename = "
					+ doc.getOriginalFileName() + " and its buffered size = "
					+ resultStream.size());

		} catch (Exception e) {
			System.out.println("There was an error with a document: "
					+ e.getMessage());
			logger.error(e);
			e.printStackTrace();

			// Если что-то пошло не так...

			try {
				// create blank error document
				Document errDoc = new Document();

				// DocumentBuilder provides members to easily add content to
				// a document.
				DocumentBuilder builder = new DocumentBuilder(errDoc);
				// Write a new paragraph in the document with the text which
				// contains error message
				builder.writeln("There was an error with a document: "
						+ e.getMessage());
				
				// Save now as OutputStream
				errDoc.save(resultStream, SaveFormat.PDF);

				// set proper Content Length and Content type for HTTP
				// Response
				response.setContentLength(resultStream.size());
				response.setContentType("application/pdf");

				OutputStream responseOutputStream = response.getOutputStream();

				// write data to HTTP Output stream
				responseOutputStream.write(resultStream.toByteArray());
				responseOutputStream.flush();
				responseOutputStream.close();

				resultStream.close();

			} catch (Exception ex) {
				ex.printStackTrace();
				logger.error("Absolutely everything failed and no content will be returned to a client. Reason is:", ex);
			}

			return;

		}

		// set proper Content Length and Content type for HTTP Response
		response.setContentLength(resultStream.size());
		response.setContentType("application/pdf");

		// if it's required to tell to a browser to download content instead of
		// previewing...
		// response.addHeader("Content-Disposition", "attachment; filename=Aspose_DocToPDF.pdf");

		OutputStream responseOutputStream = response.getOutputStream();

		// write data to HTTP Output stream
		responseOutputStream.write(resultStream.toByteArray());
		responseOutputStream.flush();
		responseOutputStream.close();

		resultStream.close();

	}
	  
	  public void destroy()
	  {
	      // do nothing.
	  }

}
