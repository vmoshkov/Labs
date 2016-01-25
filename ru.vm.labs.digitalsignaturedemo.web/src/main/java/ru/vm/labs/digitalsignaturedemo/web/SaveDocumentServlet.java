package ru.vm.labs.digitalsignaturedemo.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/*
 * Implements only POST
 * POST method does:
 *  1) Gets attributes from HTML from
 *  2) Creates SimpleDocument object and saves it into Berkeley DB
 *  3) Calculates hash of object attributes
 *  4) Returns document hash and document id to HTTP-client
 */
public class SaveDocumentServlet extends HttpServlet  {
	
	private static final Logger logger = LogManager.getLogger(SaveDocumentServlet.class);
	
	/*
	 * HTTP POST SaveDocumentServlet
	 * 
	 * Chould be Multipart with content attached
	 * 
	 */
	 public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		 boolean isMultipart;
		 String fileName = "";
         String contentType = "";
         boolean isInMemory;
         long sizeInBytes = 0;
         String docID = "";
         byte[] fileBytes = null;
         String doc_hash = "";
		 
		 logger.debug("method SaveDocumentServlet::doPost() is invoked.");
		 
		// Check that we have a file upload request
	      isMultipart = ServletFileUpload.isMultipartContent(request);
	      
	      if( !isMultipart ){
	    	  logger.error("SaveDocumentServlet: the form does not contain multipart content.");
	    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          return;
	       }
	      
	      // Create a factory for disk-based file items
	      DiskFileItemFactory factory = new DiskFileItemFactory();

	      // Configure a repository (to ensure a secure temp location is used)
	      ServletContext servletContext = this.getServletConfig().getServletContext();
	      File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
	      
	      factory.setRepository(repository);
	      factory.setSizeThreshold(10485760); //set treshold equals to 10Mb
	      
	      // debug
	      logger.debug("javax.servlet.context.tempdir=" + repository.getCanonicalPath());

	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      
	      	      	      
	      try{
	    	  HashMap formData = new HashMap();
	    	  
	    	// Parse the request to get file items.
		      List fileItems = upload.parseRequest(request);
			
		      // Process the uploaded file items
		      Iterator i = fileItems.iterator();
		      
		      while ( i.hasNext () ) 
		      {
		    	  
		    	  FileItem fi = (FileItem)i.next();
		          if ( !fi.isFormField () )	
		          {
		        	  fileName = fi.getName();
		        	  contentType = fi.getContentType();
		        	  sizeInBytes = fi.getSize();
		        	  isInMemory = fi.isInMemory();
		        	  fileBytes = fi.get();
		        	 	             		            
		             logger.debug("fileName = " + fileName + ", contentType = " + contentType + ", fileSize =  " + sizeInBytes + ", isInMem = " + isInMemory);
		          }  
		          else
		          {
		        	  //process regular fields
		        	  String name = fi.getFieldName();
		        	  String value = fi.getString();
		        	  
		        	  formData.put(name, value);
		        	    	        	 
		          }
							
		      }	       
		      
		    //insert new object or update existing one
		    if(formData.get("doc_id").equals("undefined"))
		    {
		    	docID = String.valueOf(System.currentTimeMillis());
		    		    	
		    	BerkeleyDB.save(new SimpleDocument(docID, String.valueOf(formData.get("desc")), 
		    			new SimpleDateFormat("yyyy/MM/dd").parse(String.valueOf(formData.get("current_date"))), 
						String.valueOf(formData.get("author")), fileBytes,  FilenameUtils.getName(fileName)));
		    }
		    else
		    {
		    	docID = String.valueOf(formData.get("doc_id"));
		    	
		    	SimpleDocument updatedDoc = BerkeleyDB.get((String) formData.get("doc_id"));
		    	
		    	updatedDoc.setAuthor(String.valueOf(formData.get("author")));
		    	updatedDoc.setDescription(String.valueOf(formData.get("desc")));
		    	updatedDoc.setDateOfcreation(new SimpleDateFormat("yyyy/MM/dd").parse(String.valueOf(formData.get("current_date"))));
		    	updatedDoc.setSignature(String.valueOf(formData.get("doc_signature")));
		    			    	
		    	// it's tricky to save attachment for an existing document. because it is possible two different situations:
		    	// a) form has a new file object b) form hasn't a file object
		    	if(sizeInBytes !=  0)
		    	{
		    		//it's ok, just save
		    		updatedDoc.setFilebytes(fileBytes);
			    	updatedDoc.setFileName(FilenameUtils.getName(fileName));
		    	}
		    	else
		    	{
		    		// if a file attachment wasn't previuosly set, then rise an error
		    		if ((updatedDoc.getFilebytes()==null) || (updatedDoc.getFilebytes().length < 1))
		    		{
		    		  logger.error("SaveDocumentServlet: error saving new version of document with id " + docID  + " -> the request params have no attached content.");
		   	    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		   	          return;
		    		}
		    		//else just keep old file
		    	}
		    	
		    	
		    	BerkeleyDB.save(updatedDoc);
		    }
		    
		  		  
		    SimpleDocument resultDoc = BerkeleyDB.get(docID);
		    
		    if (resultDoc!=null)
		    {
		    	logger.debug("document with id = " + docID + " was succesfully saved!");
		    			    			    	
		    	doc_hash = resultDoc.getDocumentHashAsHexString();
		    }
		      
	      }
	      catch(Exception ex)
	      {
	    	  logger.error("Exception in SaveDocumentServlet:", ex);
	    	  ex.printStackTrace();
	    	  
	    	  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    	  return;
	      }
	       	    	  
		 response.setStatus(HttpServletResponse.SC_CREATED);
			 
		 response.setContentType("text/plain");
		 response.setHeader("doc_id", docID);
		 response.setHeader("doc_hash", doc_hash);
			 			 
		 response.flushBuffer();
		          
	 }

}
