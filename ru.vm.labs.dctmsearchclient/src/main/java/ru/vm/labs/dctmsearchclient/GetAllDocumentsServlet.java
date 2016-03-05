package ru.vm.labs.dctmsearchclient;


import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;


public class GetAllDocumentsServlet extends HttpServlet {
	
	private static final Logger logger = LogManager.getLogger(GetAllDocumentsServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		logger.debug("method GetAllDocumentsServlet::doGet() is invoked.");
		
		SimpleEMCDocumentumService emcClient = new SimpleEMCDocumentumService();
		
		String fullsearchText = req.getParameter("fulltext");
		
		
		JSONArray ja  = emcClient.getAllDocumentObjectsWithText(fullsearchText);
				
		resp.setContentType("text/plain");
		resp.setCharacterEncoding("UTF-8");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = resp.getWriter();
		
		out.print(ja!=null? ja.toString() : "");
		out.flush();		
		
	}

}
