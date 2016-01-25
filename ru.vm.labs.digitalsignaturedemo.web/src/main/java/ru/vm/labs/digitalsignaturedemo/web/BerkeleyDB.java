package ru.vm.labs.digitalsignaturedemo.web;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;

/*
 * We need a convinient wrapper to work with Berkeley DB along inside application
 * So this class is supposed to do it
 * 
 * 1) Initialize DB at application init phase
 * 2) Then we know that we are able to use BerkeleyDB.save BerkeleyDB.get inside servlets
 * 3) Close DN at application shutdown
 */
public class BerkeleyDB {
	
	private static final Logger logger = LogManager.getLogger(BerkeleyDB.class);
	
	private static PrimaryIndex<String, SimpleDocument> documentIndex;
	private static EntityStore store;
	private static Environment dbEnv;
	
	public static boolean InitDB(File databaseDir)
	{
		logger.debug("BerkeleyDB::InitDB() started");
		
		try {
	        // create a configuration for DB environment
	        EnvironmentConfig envConf = new EnvironmentConfig();
	        // environment will be created if not exists
	        envConf.setAllowCreate(true);
	 
	        // open/create the DB environment using config
	        dbEnv = new Environment(databaseDir, envConf);
	        
	        
	     // create a configuration for new Store	        
	        StoreConfig stConf = new StoreConfig();
	        stConf.setAllowCreate(true);
	        store = new EntityStore(dbEnv, "DPLSample", stConf);
	                
	        documentIndex = store.getPrimaryIndex(String.class, SimpleDocument.class);        
	        
	 
	    } catch (DatabaseException dbe) {
	        logger.error("Error :" + dbe.getMessage() );
	    }
		
		return false;
	}
	
	public static void save(SimpleDocument doc)
	{
		logger.debug("BerkeleyDB::save() started");
		
		//insert
        documentIndex.putNoReturn(doc);     	
	}

	public static SimpleDocument get(String docId)
	{		
		logger.debug("BerkeleyDB::get() started");
		
		return documentIndex.get(docId);
	}
	
	public static Map<String, SimpleDocument> getAll()
	{
		logger.debug("BerkeleyDB::get() started");
		
		//TODO It is insafety to return all database. only for the test app purposes
		return documentIndex.map();	
	}
	
	public static boolean ShutdownDB()
	{
		logger.debug("BerkeleyDB::ShutdownDB() started");
		
		store.close();
        dbEnv.close();
        
		return false;
	}
}
