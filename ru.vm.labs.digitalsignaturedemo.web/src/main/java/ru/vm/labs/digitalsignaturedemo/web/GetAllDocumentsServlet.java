package ru.vm.labs.digitalsignaturedemo.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Implements only GET
 * 1) get all objects from BerkeleyDB
 * 2) transform object array to JSON
 * 3) return JSON to HTTP-client
 */
public class GetAllDocumentsServlet extends HttpServlet {

	private static final Logger logger = LogManager
			.getLogger(GetAllDocumentsServlet.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		logger.debug("method GetAllDocumentsServlet::doGet() is invoked.");

		JSONArray ja = new JSONArray();
		Map<String, SimpleDocument> allObjects = BerkeleyDB.getAll();

		if (allObjects != null && !allObjects.isEmpty()) {
			

			// iterate through key set and transform to JSON
			for (String key : allObjects.keySet()) {
				SimpleDocument singleDoc = allObjects.get(key);
			
				JSONObject singleClub = new JSONObject();
				singleClub.put("fileName", singleDoc.getFileName());
				singleClub.put("dateOfcreation", new SimpleDateFormat("yyyy/MM/dd").format(singleDoc.getDateOfcreation()));
				singleClub.put("author", singleDoc.getAuthor());
				singleClub.put("description", singleDoc.getDescription());
				singleClub.put("doc_id", singleDoc.getId());	
				

				ja.put(singleClub);
			}
						
		} else {
			logger.error("the database doesn't return any object. it seems to be empty.");
		}
		
		//resp.setContentType("application/json");
		resp.setContentType("text/plain");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = resp.getWriter();
		out.print(ja.toString());
		out.flush();		
		
	}
}
