package ru.vm.berryville.j2eewebix;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


public class MyAppContextListener implements ServletContextListener {
	
	private static final Logger logger = LogManager.getLogger(MyAppContextListener.class);
	

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		
		logger.debug("MyAppContextListener destroyed");
		
		

	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		
		logger.debug("MyAppContextListener started");	

	}

}
