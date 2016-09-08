package ru.vm.berryville.j2eewebix;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Base64;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/*
 * Gets all cars from DB
 */
public class GetAllAdverts extends HttpServlet {
	
	private static final Logger logger = LogManager.getLogger(GetAllAdverts.class);
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("method GetAllAdverts::doGet() is invoked.");
		
		ArrayList<Car> cars = DBHelper.getAllCarObjects();
		
		JSONArray ja = new JSONArray();
    	
		if (cars!=null)
		{
	    	for ( Car carObject : cars) {
	    	    	    	    
	    	    JSONObject singleCar = new JSONObject();
	    	    
	    	    singleCar.put("entry_date", carObject.getEntryDate().toString());
	    	    singleCar.put("prise", carObject.getPrise());
	    	    singleCar.put("manufacturer", carObject.getManufacturer().getName());
	    	    singleCar.put("model", carObject.getModel());
	    	    
	    	    byte[] imageBytes = null;
	    	    
	    	    //HACK! Actually, returns only one image
	    	    for (Image img: carObject.getImages())
	    	    {
	    	    	imageBytes = img.getData();
	    	    	break;
	    	    }
	    	    singleCar.put("photo", imageBytes!=null ? Base64.getEncoder().encodeToString(imageBytes) : "");
	    	    singleCar.put("id", carObject.getId());
	    	    
	    	    ja.put(singleCar);
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
