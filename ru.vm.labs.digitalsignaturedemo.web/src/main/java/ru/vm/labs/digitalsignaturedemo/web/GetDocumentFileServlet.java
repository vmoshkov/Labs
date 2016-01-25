package ru.vm.labs.digitalsignaturedemo.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/*
 * Implements only GET
 * 1) get a specific object from BerkeleyDB by passed param
 * 2) get document's file bytes
 * 3) return file bytes to a client
 */
public class GetDocumentFileServlet extends HttpServlet {
	
	private static final Logger logger = LogManager.getLogger(GetDocumentFileServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		logger.debug("method GetDocumentFileServlet::doGet() is invoked.");
		
		// get request param docid
		String docid = req.getParameter("docid");
		
		if(docid==null || docid.length() < 3 ){
	    	  logger.error("GetDocumentFileServlet: request doesn't contain apropriate input paramenter value. set docid correctly!");
	    	  resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          return;
	     }
		
		SimpleDocument requestedDocument = BerkeleyDB.get(docid);
		
		if(requestedDocument==null)
		{
			logger.error("GetDocumentFileServlet: can't find document with id = " + docid);
	    	resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
	        return;
		}
		
		requestedDocument.getFilebytes();
		
		resp.setContentType("application/octet-stream");
		// tell to a browser to download content, not to preview
        resp.setHeader("Content-Disposition","attachment;filename="+requestedDocument.getFileName());
		
		// Get the  ServletOutputStream object from response to write the file to the output stream      
        ServletOutputStream out = resp.getOutputStream();
        out.write(requestedDocument.getFilebytes());
		out.flush();	
		out.close();
	}
}
