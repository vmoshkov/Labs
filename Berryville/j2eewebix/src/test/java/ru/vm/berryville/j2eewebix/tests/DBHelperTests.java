package ru.vm.berryville.j2eewebix.tests;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import ru.vm.berryville.j2eewebix.Car;
import ru.vm.berryville.j2eewebix.DBHelper;
import ru.vm.berryville.j2eewebix.Image;
import ru.vm.berryville.j2eewebix.Manufacturer;

import static org.junit.Assert.*;


public class DBHelperTests {
	
	private static SessionFactory factory;
	
	@Before
	public void init() throws HibernateException, URISyntaxException {
		
		// Create the SessionFactory from hibernate.cfg.xml
        Configuration config = new Configuration();

        config.configure(new File(this.getClass().getClassLoader().getResource("hibernate.cfg.xml").toURI()));
	    factory = config.buildSessionFactory();
	           
	}
	
	@After
	public void tearDown() {
		if (factory!=null)
			factory.close();
	}
	
	public void saveManufacturer()
	{
		System.out.println(" saveManufacturer() started");
		
		Manufacturer newMan = new Manufacturer("BMW", "Germany");
		
		if( DBHelper.saveObject(newMan))
			System.out.println(" new manufacturer was saved!");
		else
			System.out.println(" failed to save a new manufacturer");
	}
	
	public void saveCar() throws URISyntaxException
	{
		System.out.println(" saveCar() started");
		
		// save new car
		Car singleCar = new Car();
						
		singleCar.setContactPerson("Dennis Forinault");
		singleCar.setContactPhone("111-35-555-34-21");
		singleCar.setDescription("Just a good car");
				
		singleCar.setEntryDate(java.time.LocalDate.parse("2016-11-23"));
		
		// get a manufacturer
		Manufacturer man = new Manufacturer();
		man.setName("BMW");
		List<Object> results = DBHelper.getObjectsFromDB(man); 
		
		assertTrue("BMW manufacturer should exist in DB", (results!=null) && (results.size()>0));
		
		if ((results!=null) && (results.size()>0))
			man = (Manufacturer) results.get(0);
		
		singleCar.setManufacturer(man);
		
		// get list of images
		HashSet<Image> carImages = new HashSet<>();
 		for (int im=1; im<4; im++)
		{
			File imageFile = new File(this.getClass().getClassLoader().getResource("files/mers_g_amg" + im + ".jpeg").toURI());
			
			byte[] fileBytes = new byte[(int) imageFile.length()];
	        try {
	        	 FileInputStream fis = new FileInputStream(imageFile);
	             fis.read(fileBytes);
	              
	         } catch (FileNotFoundException e) {
	                     System.out.println("File Not Found.");
	                     e.printStackTrace();
	         }
	         catch (IOException e1) {
	                  System.out.println("Error Reading The File.");
	                   e1.printStackTrace();
	         }
							
			Image newImage = new Image();
			newImage.setCar(singleCar);
			newImage.setFilename(imageFile.getName());
			newImage.setData(fileBytes);
			
			carImages.add(newImage);
			
		}
 		singleCar.setImages(carImages);
 						
		singleCar.setModel("530i");
		singleCar.setYear(2012);
		singleCar.setPrise(30000);
				
		if( DBHelper.saveObject(singleCar))
			System.out.println(" new car was saved!");
		else
			System.out.println(" failed to save a new car");
	}
	
	public void deleteAllCars()
	{
		for (int k=37; k< 43; k++)
		{
			DBHelper.deleteObject(Car.class, String.valueOf(k));
		}
		
	}
	
	public void printAllCars()
	{
		Session session = factory.openSession();
		
		List<Car> cars = session.createQuery( "from Car" ).list();
		
		for (Car car : cars) {
			System.out.println(car.toString());
		}
		
		session.close();
	}
	
	public void printAllManufacturers()
	{
		Session session = factory.openSession();
		
		List<Manufacturer> mans = session.createQuery( "from Manufacturer" ).list();
		
		for (Manufacturer man : mans) {
			System.out.println(man.toString());
		}
		
		session.close();
	}
	
	public void printAllImages()
	{
		Session session = factory.openSession();
		
		List<Image> imgs = session.createQuery( "from Image" ).list();
		
		for (Image img : imgs) {
			System.out.println(img.toString());
		}
		
		session.close();
	}
	
	//@Test
	public void testTime()
	{
		System.out.println(LocalDate.parse("2016-11-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		System.out.println(LocalDate.parse("2016-11-23", DateTimeFormatter.ofPattern("yyyy-MM-dd")).toEpochDay());
		
		
		System.out.println();
	}
	
	@Test
	public void TestMainMethod() throws URISyntaxException, FileNotFoundException 
	{
		//saveManufacturer();
		saveCar();
		printAllManufacturers();
		printAllCars();
		printAllImages();
		//deleteAllCars();	
				
	}

}
