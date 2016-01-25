package ru.vm.labs.digitalsignaturedemo.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;

/*
 * Implements only GET
 * 1) get a specific object from BerkeleyDB by passed param
 * 2) transform object to JSON
 * 3) return JSON to HTTP-client
 */
public class GetDocumentServlet extends HttpServlet{
	
	private static final Logger logger = LogManager.getLogger(GetDocumentServlet.class);
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		logger.debug("method GetDocumentServlet::doGet() is invoked.");
		
		// get request param docid
		String docid = req.getParameter("docid");
		
		if(docid==null || docid.length() < 3 ){
	    	  logger.error("GetDocumentServlet: request doesn't contain apropriate input paramenter value. set docid correctly!");
	    	  resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          return;
	     }
		
		SimpleDocument requestedDocument = BerkeleyDB.get(docid);
		
		if(requestedDocument==null)
		{
			logger.error("GetDocumentServlet: can't find document with id = " + docid);
	    	resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	        return;
		}
		
		
		JSONObject jsonDoc = new JSONObject();
		jsonDoc.put("fileName", requestedDocument.getFileName());
		jsonDoc.put("dateOfcreation", new SimpleDateFormat("yyyy/MM/dd").format(requestedDocument.getDateOfcreation()));
		jsonDoc.put("author", requestedDocument.getAuthor());
		jsonDoc.put("description", requestedDocument.getDescription());
		jsonDoc.put("doc_id", requestedDocument.getId());	
		jsonDoc.put("doc_hash", requestedDocument.getDocumentHashAsHexString());
		jsonDoc.put("doc_signature", requestedDocument.getSignature());
		
		//resp.setContentType("application/json");
		resp.setContentType("text/plain");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = resp.getWriter();
		out.print(jsonDoc.toString());
		out.flush();	
	
	}
	

}
