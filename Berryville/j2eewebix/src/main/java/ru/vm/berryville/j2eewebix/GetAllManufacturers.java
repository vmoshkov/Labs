package ru.vm.berryville.j2eewebix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Gets all manufacturers from DB
 */
public class GetAllManufacturers extends HttpServlet {
	
	private static final Logger logger = LogManager.getLogger(GetAllManufacturers.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("method GetAllManufacturers::doGet() is invoked.");
		
		ArrayList<Manufacturer> manufacturers = DBHelper.getAllManufacturesObjects();
		
		JSONArray ja = new JSONArray();
    	
		if (ja!=null)
		{
	    	for ( Manufacturer man : manufacturers) {
	    	    System.out.println(man.getName());
	    	    
	    	    JSONObject singleManufacturer = new JSONObject();
	    	    
	    	    singleManufacturer.put("title", man.getName());
	    	    singleManufacturer.put("country", man.getCountry());
	    	    singleManufacturer.put("id", man.getId());
	    	    
	    	    ja.put(singleManufacturer);
	    	}
		}
    	
    	response.setContentType("text/plain; charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = response.getWriter();
		out.print(ja.toString());
		out.flush();	
	}

}
