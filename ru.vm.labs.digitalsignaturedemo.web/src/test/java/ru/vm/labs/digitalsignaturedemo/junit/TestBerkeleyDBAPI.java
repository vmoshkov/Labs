package ru.vm.labs.digitalsignaturedemo.junit;

import java.io.File;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ru.vm.labs.digitalsignaturedemo.web.SimpleDocument;

import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseException;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;
import com.sleepycat.persist.EntityStore;
import com.sleepycat.persist.PrimaryIndex;
import com.sleepycat.persist.StoreConfig;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestBerkeleyDBAPI {
	
	@Test
	public void TestBerkeleyDBBaseCase ()
	{
		System.out.println("TestBerkeleyDBBaseCase ()...");
		
		
		System.out.println("current directory = " + System.getProperty("user.dir"));
		
		try {
	        // create a configuration for DB environment
	        EnvironmentConfig envConf = new EnvironmentConfig();
	        // environment will be created if not exists
	        envConf.setAllowCreate(true);
	 
	        // open/create the DB environment using config
	        //String currentPath = System.getProperty("user.dir");
	        File testDBFolder = new File(System.getProperty("user.dir"),"testDB");
	        
	        if(!testDBFolder.exists())
	        {
	        	testDBFolder.mkdir();
	        }
	        
	        Environment dbEnv = new Environment(testDBFolder, envConf);
	        
	        
	     // create a configuration for DB
	        DatabaseConfig dbConf = new DatabaseConfig();
	        // db will be created if not exits
	        dbConf.setAllowCreate(true);
	         
	        // create/open testDB using config
	       // Database testDB = dbEnv.openDatabase(null, "testDB", dbConf);
	        
	        StoreConfig stConf = new StoreConfig();
	        stConf.setAllowCreate(true);
	        EntityStore store = new EntityStore(dbEnv, "DPLSample", stConf);
	        
	        PrimaryIndex<String, SimpleDocument> documentIndex;
	        
	        documentIndex = store.getPrimaryIndex(String.class, SimpleDocument.class);
	        
	        //insert
	        documentIndex.put(new SimpleDocument("version1", "my new document", new java.util.Date(), "vladimir moshkov", new byte[64], "empty file.txt"));
	        
	      //retrieve
	        SimpleDocument doc2 = documentIndex.get("version1");//retrieve
	        
	        System.out.println("document description = " + doc2.getDescription());
	        
	        //.putNoReturn(new Employee(“u180”, “Locke”, null));//
	        //Update
	        //userIndex.delete(“u180”);//delete
	        store.close();
	        dbEnv.close();

	        
	 
	    } catch (DatabaseException dbe) {
	        System.out.println("Error :" + dbe.getMessage() );
	    }
	}

}
