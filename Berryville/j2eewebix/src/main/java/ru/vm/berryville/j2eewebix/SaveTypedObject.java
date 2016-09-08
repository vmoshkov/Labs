package ru.vm.berryville.j2eewebix;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.text.DateFormatter;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/*
 * Servlet class which enables to save or update object 
 */
public class SaveTypedObject extends HttpServlet {
	
	private static final Logger logger = LogManager.getLogger(SaveTypedObject.class);
	
	/*
	 * Метод сохранения данных в базу об объекте.
	 * обязательный параметр type - тип объекта. Значения manufacturer или advert
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		logger.debug("method SaveTypedObject::doPost() is invoked with request = " + request.toString());
		 
		String type = request.getParameter("type");
		
		// type is absolutely required param
		if(type==null || type.length()==0)
		{
			logger.error("SaveTypedObject::doPost() has no required param \"type\". Stop execution.");
	    	response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        return;
		}
		
		if(type.equals("manufacturer"))
		{
			String manufacturer = request.getParameter("manufacturer");
			String country = request.getParameter("country");
			String id = request.getParameter("id");
			Manufacturer manufacturerObject = null;
			
			// manufacturer and country are both required
			if(manufacturer==null || manufacturer.length()==0 || country==null || country.length()==0 )
			{
				  logger.error("SaveTypedObject::doPost() has no required params. Stop execution.");
		    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		          return;
			}
			
			// try to get object from DB
			if(id!=null)
			{
				manufacturerObject = DBHelper.getManufacturer(id);
			}
			
			// set new values for a object
			if(manufacturerObject == null)
			{
				manufacturerObject = new Manufacturer(manufacturer, country);
			}
			else
			{
				manufacturerObject.setName(manufacturer);
				manufacturerObject.setCountry(country);
			}
			
			if(!DBHelper.saveObject(manufacturerObject))
			{
			  logger.error("SaveTypedObject::doPost() failed to save manufacturer object. Stop execution.");
			  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		      return;
			}
			 
		}
		else if(type.equals("advert"))
		{
			Car carObject = null;
			String carId = request.getParameter("car_id");
			boolean isMultipart = ServletFileUpload.isMultipartContent(request);
			 
			// try to get object from DB
			if((carId!=null) && (carId.length()>0))
			{
			   carObject = DBHelper.getCarByID(carId);
			}
		      
		      // if request was a multipart, then we expect to have the file(s)
		      if(isMultipart){
		    	 logger.error("Unexpected situation: cannot proceed multipart request for car with id = " + carId);
		         throw new UnsupportedOperationException("Unexpected situation: cannot proceed multipart request for car with id = " + carId);			      
		      }
		      else // just obtain values from the form and save on object
		      {
		    	
					
				  //if object doesn´t already exist, just create new
				  if(carObject==null)
				  {
						carObject = new Car();
				  }
					
		    	  if (request.getParameter("item_date")!=null)
		    	  {
		    		  String sEntryDate = request.getParameter("item_date").trim();
		    		  
		    		  carObject.setEntryDate(java.time.LocalDate.parse(sEntryDate.substring(0, 10),  DateTimeFormatter.ISO_DATE) );
		    	  }
		    	  
		    	  carObject.setContactPerson(request.getParameter("contact_person"));
				  carObject.setContactPhone(request.getParameter("phone"));
				  carObject.setModel(request.getParameter("car_model"));	
				  carObject.setYear(Integer.parseInt(request.getParameter("car_year")));
				  carObject.setDescription(request.getParameter("car_desc"));
				  carObject.setPrise(Double.parseDouble(request.getParameter("car_prise")));
				  
				  // get a manufacturer
				  Manufacturer man = new Manufacturer();
				  man.setName(request.getParameter("car_manufacturer"));
				  List<Object> results = DBHelper.getObjectsFromDB(man); 
				  
				  if ((results!=null) && (results.size()>0))
						man = (Manufacturer) results.get(0);
				  else
					  throw new IllegalArgumentException("Cannot save a car object, because cannot find Manufacturer with name " + 
							  			request.getParameter("car_manufacturer")); 
					
				  carObject.setManufacturer(man);	
				  
				  if( DBHelper.saveObject(carObject))
				  {
						logger.debug("SaveTypedObject::doPost(): car was saved!");
						
						// now I need to know Id of this object
						List cars = DBHelper.getObjectsFromDB(carObject);
						
						if ((cars!=null) && (cars.size()>0))
						{
							carObject = (Car) cars.get(0);
							logger.debug("SaveTypedObject::doPost(): saved car id = " + carObject.getId());
							
							response.addHeader("car_id", carObject.getId().toString());
						}
						else
							throw new UnsupportedOperationException("Unexpected situation: cannot obtain object Id");
						
				  }
				  else
				  {
					  logger.debug("SaveTypedObject::doPost(): failed to save a car");	
					  throw new UnsupportedOperationException("failed to save a car");
				  }
		      }
		      
		      
	    	  
		}
		
		response.setStatus(HttpServletResponse.SC_CREATED);
	}
	
	

}
