package ru.vm.berryville.j2eewebix;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.type.ClobType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.sql.rowset.serial.SerialClob;
import javax.sql.rowset.serial.SerialException;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class DBHelper {
	
	private static final Logger logger = LogManager.getLogger(DBHelper.class);
	
	/*
	 * Saves the given object.
	 * Returns true if succeeds. else - false
	 */
	public static <T> boolean saveObject(T object)
	{
		logger.debug("starting to save object " + object.toString());
		
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        boolean result = false; //default result
                
        try {
        	trns = session.beginTransaction();
            session.saveOrUpdate(object);
            session.getTransaction().commit();   
            result = true;
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            logger.error("Hebirnate failed to save an object {" +  object.toString() +  "} :", e);            
        } finally {
        	//session.flush();
            session.close();
        }
        
        return result;
    }
	
	/*
	 * Service method to get manufacturer from DB
	 */
	public static Manufacturer getManufacturer(String objectID)
	{
		logger.debug("starting to obtain a manufacturer with id = " + objectID);
		
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Manufacturer result = null;
        
                
        try {
        	trns = session.beginTransaction();        	
        	// Id must be the same type as it was set in a model class
        	result = session.get(Manufacturer.class, new Integer(objectID));                             
            session.getTransaction().commit();             
            
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            logger.error("Hebirnate failed to get an object {" +  objectID.toString() +  "} :", e);            
        } finally {
        	//session.flush();
            session.close();
        }    
		
		return result;
	}
	
	/*
	 * returns all objects of the type Manufacturer
	 */
	public static ArrayList<Manufacturer> getAllManufacturesObjects()
	{
		logger.debug("starting to obtain all manufacturers");
		
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Manufacturer> result = null;
		
        try {
        	session.beginTransaction();
        	
        	result = (ArrayList<Manufacturer>) session.createQuery( "from Manufacturer" ).list();
        	
        	session.getTransaction().commit();
        	       	
        } catch (RuntimeException e) {
	         e.printStackTrace();
	         logger.error("Hebirnate failed to get all manufacturers:", e);     
	         return null;
	     } finally {
	     	//session.flush();
	         session.close();         
	     }	
        
        return result;
	}
	
	/*
	 * Deletes a given object from DB
	 */
	public static <T> boolean deleteObject(Class<?> type, String objectId)
	{
		logger.debug("starting to delete object " + objectId.toString() + "\n");
		
		boolean result = false;	
		
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        
                
        try {
        	trns = session.beginTransaction();
        	
        	// Id must be the same type as it was set in a model class
        	Object persistentInstance = session.load(type, new Integer(objectId));
            
        	if (persistentInstance != null) {
                session.delete(persistentInstance);                
            }
            
            session.getTransaction().commit();   
            result = true;
            
            logger.debug("Object with id " + objectId.toString() + " of type " + type.getName() 
            						+ " has been deleted\n");
            
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            logger.error("Hebirnate failed to delete an object {" +  objectId.toString() +  "} :", e);            
        } finally {
        	//session.flush();
            session.close();
        }        
        		
		return result;
	}
	
	/*
	 * Uses Hibernate feature to find all objects in DB which suits a criteria object
	 */
	public static List<Object> getObjectsFromDB(Object criteriaModel)
	{
		Session session = HibernateUtil.getSessionFactory().openSession();
        List<Object> results = null;
        
        try {       	    
        	results = session.createCriteria(criteriaModel.getClass()).add( Example.create(criteriaModel)).list();
              
        } catch (RuntimeException e) {
            
            e.printStackTrace();
            logger.error("Hebirnate failed to get an object {" +  criteriaModel.toString() +  "} :", e);            
        } finally {
        	//session.flush();
            session.close();
        }    
		
		return results;
	}
	
	/*
	 * Service method to get a Car from DB
	 */
	public static Car getCarByID(String objectID)
	{
		logger.debug("starting to obtain a car with id = " + objectID);
		
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Car result = null;
        
                
        try {
        	//trns = session.beginTransaction();        	
        	// Id must be the same type as it was set in a model class
        	result = session.get(Car.class, new Integer(objectID));                             
            //session.getTransaction().commit();             
            
        } catch (RuntimeException e) {
            if (trns != null) {
                trns.rollback();
            }
            e.printStackTrace();
            logger.error("Hebirnate failed to get an object {" +  objectID.toString() +  "} :", e);            
        } finally {
        	//session.flush();
            session.close();
        }    
		
		return result;
	}
	
	/*
	 * returns all objects of the type Car
	 */
	public static ArrayList<Car> getAllCarObjects()
	{
		logger.debug("starting to obtain all cars");
		
		Transaction trns = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        ArrayList<Car> result = null;
		
        try {
        	session.beginTransaction();
        	
        	result = (ArrayList<Car>) session.createQuery( "from Car" ).list();
        	
        	session.getTransaction().commit();
        	       	
        } catch (RuntimeException e) {
	         e.printStackTrace();
	         logger.error("Hebirnate failed to get all cars:", e);     
	         return null;
	     } finally {
	     	//session.flush();
	         session.close();         
	     }	
        
        return result;
	}
	

}
