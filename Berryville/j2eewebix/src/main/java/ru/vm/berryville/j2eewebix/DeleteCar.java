package ru.vm.berryville.j2eewebix;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/*
 * Deletes a given car from DB
 */
public class DeleteCar extends HttpServlet {
	
	private static final Logger logger = LogManager.getLogger(DeleteCar.class);
	
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		 
		 logger.debug("method DeleteCar::doPost() is invoked.");
		 
		 String objectID = request.getParameter("id");
		 
		  
		  if(objectID==null || objectID.length()==0)
		  {
			  logger.error("DeleteCar::doPost() has no required params. Stop execution.");
	    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          return;
		  }	  
		  				  
		  if(!DBHelper.deleteObject(Car.class, objectID))
		  {
			  logger.error("DeleteCar::doPost() failed to delete an Car object with id = " + 
					  				objectID + ". Stop execution.");
			  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	          return;
		  }
		  
		  response.setStatus(HttpServletResponse.SC_OK);		 
	 }

}
