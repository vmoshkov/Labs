package ru.vm.berryville.j2eewebix;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetTypedObjectByID extends HttpServlet{
	
	private static final Logger logger = LogManager.getLogger(GetTypedObjectByID.class);
	
	/*
	 * Метод получения данных из базы об объекте.
	 * id - идентификатор объекта в базе
	 * type - тип объекта. Значения manufacturer или advert
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		logger.debug("method GetAllManufacturers::doGet() is invoked.");
		
		 String objectID = request.getParameter("id");
		 String type = request.getParameter("type");
		 		  
		  if(objectID==null || objectID.length()==0 || type==null || type.length()==0)
		  {
			  logger.error("GetTypedObjectByID::doGet() has no required params. Stop execution.");
	    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          return;
		  }	  
		  
		  JSONObject result = null;
		  
		  if(type.equals("manufacturer"))
		  {
			  result = getManufacturer(objectID);
		  }
		  else if(type.equals("advert"))
		  {
			  result = getAdvert(objectID);  
		  }
		  
		  if(result==null)
		  {
			  logger.error("GetTypedObjectByID::doGet() failed to get object with id = " + objectID);
			  response.setStatus(HttpServletResponse.SC_NO_CONTENT);
	          return;
		  }
			  
    	
    	response.setContentType("text/plain; charset=utf-8");
    	response.setCharacterEncoding("UTF-8");
		// Get the printwriter object from response to write the required json object to the output stream      
		PrintWriter out = response.getWriter();
		out.print(result.toString());
		out.flush();	
	}
	
	/*
	 * Utility layer to get Manufacturer object from DB as well as to transform it to JSON
	 */
	private JSONObject getManufacturer(String objectID)
	{
		Manufacturer man = DBHelper.getManufacturer(objectID);
		
		if(man==null) 
			return null;
		
		
		JSONObject singleManufacturer = new JSONObject();
    	    
    	singleManufacturer.put("title", man.getName());
    	singleManufacturer.put("country", man.getCountry());
    	singleManufacturer.put("id", man.getId());
    	    
		return singleManufacturer;
		
	}
	
	/*
	 * Utility layer to get Car object from DB as well as to transform it to JSON
	 */	
	private JSONObject getAdvert(String objectID)
	{
		Car car = DBHelper.getCarByID(objectID);
		
		if(car==null)
			return null;
		
		JSONObject carObject = new JSONObject();
		
		carObject.put("id", car.getId());
		carObject.put("date", car.getEntryDate().toString());
		carObject.put("year", car.getYear());
		carObject.put("manufacturer", car.getManufacturer().getName());
		carObject.put("model", car.getModel());
		carObject.put("description", car.getDescription().toString());
		carObject.put("prise", car.getPrise());
		carObject.put("person", car.getContactPerson());
		carObject.put("phone", car.getContactPhone());
		
		JSONArray jsonImages = new JSONArray();
		Set<Image> carImages = car.getImages();
		
		for (Image image : carImages) {
			JSONObject jsonImage = new JSONObject();
			
			jsonImage.put("filename", image.getFilename());
		
			try {
				jsonImage.put("data", image.getData() !=null ? Base64.getEncoder().encodeToString(image.getData()) : "");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				jsonImage.put("data", "");
			}
			
			jsonImages.put(jsonImage);
		}
		
		carObject.put("images", jsonImages);
		
		return carObject;
	}
	

}
