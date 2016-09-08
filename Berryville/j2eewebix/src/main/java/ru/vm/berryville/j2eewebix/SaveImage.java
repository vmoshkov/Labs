package ru.vm.berryville.j2eewebix;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class SaveImage extends HttpServlet {

	private static final Logger logger = LogManager.getLogger(SaveImage.class);

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

		logger.debug("method SaveImage::doGet() is invoked.");
		

		HashMap formData = new HashMap(); // Поля формы
		HashSet<Image> carImages = new HashSet<>(); // Список передданых файлов
		Car carObject = null;
		String carId;
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		
		if (!isMultipart) {
			logger.error("Unexpected situation: request for car is not multipart; cannot proceed to save car images");
			throw new UnsupportedOperationException("Unexpected situation: request for car is not multipart; cannot proceed to save car images");
		}

		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();

		// Configure a repository (to ensure a secure temp location is used)
		ServletContext servletContext = this.getServletConfig().getServletContext();
		File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");

		factory.setRepository(repository);
		factory.setSizeThreshold(10485760); // set treshold equals to 10Mb

		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);

		// Parse the request to get file items.
		List fileItems;
		try {
			fileItems = upload.parseRequest(request);
		} catch (FileUploadException e) {
			logger.error("Unexpected situation: cannot get request´s items; cannot proceed to save car images");
			throw new UnsupportedOperationException("Unexpected situation: cannot get request´s items; cannot proceed to save car images");
		}

		// Process the uploaded file items
		Iterator i = fileItems.iterator();

		while (i.hasNext()) {

			FileItem fi = (FileItem) i.next();

			// Process the uploaded file items
			if (!fi.isFormField()) {
				String fileName = fi.getName();
				String contentType = fi.getContentType();
				long sizeInBytes = fi.getSize();
				boolean isInMemory = fi.isInMemory();

				logger.debug("fileName = " + fileName + ", contentType = " + contentType + ", fileSize =  "
						+ sizeInBytes + ", isInMem = " + isInMemory);

				BufferedInputStream buffer = new BufferedInputStream(fi.getInputStream());

				byte[] fileBytes = new byte[(int) sizeInBytes + 1];

				while (buffer.available() > 0) {
					int read = buffer.read(fileBytes, 0, (int) sizeInBytes);
				}

				Image newImage = new Image();
				newImage.setFilename(fileName);
				newImage.setData(fileBytes);
				
				carImages.add(newImage);

			} else {
				// process regular fields
				String name = fi.getFieldName();
				String value = fi.getString();

				formData.put(name, value);
			}
		}
		
		// now get the car´s id
		carId = formData.get("car_id").toString();
		
		
		// try to get object from DB 
		if((carId!=null) && (carId.length()>0))
		{ 
			carObject = DBHelper.getCarByID(carId); 
		}
		 
		// if object does not exist, it´s an error 
		if(carObject==null) {
			logger.error("Unexpected situation: cannot obtain car with id = " + carId + "; cannot proceed to save car images"); 
			throw new UnsupportedOperationException("Unexpected situation: cannot obtain car with id = " + carId); 
		}
		
		// add images!!!
		// update a referense to a car and add image to a set
		for(Image img: carImages)
		{
			img.setCar(carObject);
			carObject.getImages().add(img);
		}
		

		if (DBHelper.saveObject(carObject)) {
			logger.debug("SaveImage::doGet(): car was saved!");

			// now I need to know Id of this object
			List cars = DBHelper.getObjectsFromDB(carObject);

			if ((cars != null) && (cars.size() > 0)) {
				carObject = (Car) cars.get(0);
				logger.debug("SaveImage::doGet(): saved car id = " + carObject.getId());

				response.addHeader("car_id", carObject.getId().toString());
			} else
				throw new UnsupportedOperationException("Unexpected situation: cannot obtain object Id");

		} else {
			logger.error("SaveImage::doGet(): failed to save a car");
			throw new UnsupportedOperationException("failed to save a car");
		}
	


		response.setContentType("text/plain; charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(response.SC_CREATED);
	
		// Get the printwriter object from response to write the required json
		// object to the output stream
		PrintWriter out = response.getWriter();out.print("Everything OK!");out.flush();

	}

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request,response);
	}

	/*
	 * Утилитарная функция для извлечения файлов из реквеста и добавления их в
	 * объект Car´a
	 */
	private void addImages2Car(HttpServletRequest request, Car carObject) throws FileUploadException, IOException {

	}

}
