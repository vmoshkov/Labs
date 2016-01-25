package ru.vm.labs.digitalsignaturedemo.web;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DigitalSignatureDemoWebServletContextListener implements ServletContextListener {
	
	private static final Logger logger = LogManager.getLogger(DigitalSignatureDemoWebServletContextListener.class);
	
	//Run this before web application is started
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		logger.debug("DigitalSignatureDemoWebServletContextListener started");	
						
		// We should detect where the DB should be placed
		  ServletContext servletContext = arg0.getServletContext();
		  File repository = new File(servletContext.getInitParameter("database_folder"), "berkeleyDB");
		  
		  if(!repository.exists())
		  {
			  repository.mkdir();
		  }
					
		//now it's time to init Db
		BerkeleyDB.InitDB(repository);
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		logger.debug("DigitalSignatureDemoWebServletContextListener destroyed");
		
		//now it's time to shutdown Db
		BerkeleyDB.ShutdownDB();
	}


}
