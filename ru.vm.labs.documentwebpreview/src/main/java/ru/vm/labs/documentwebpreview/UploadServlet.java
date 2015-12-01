package ru.vm.labs.documentwebpreview;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.aspose.cells.CellsHelper;
import com.aspose.cells.ImageFormat;
import com.aspose.cells.ImageOrPrintOptions;
import com.aspose.cells.PrintingPageType;
import com.aspose.cells.SheetRender;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.slides.ISlide;
import com.aspose.slides.Presentation;
import com.aspose.words.Document;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontSettings;
import com.aspose.words.Orientation;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;



/*
* 1. Gets FORM Data
* 2. Gets file(s)
* 3. Renders 1 file to PDF
* 4. Returns PDF as stream back* 
*/
public class UploadServlet extends HttpServlet {
	private static final Logger logger = LogManager.getLogger(UploadServlet.class);
	
	/*
	 * LISTS OF SUPPORTED FORMATS
	 */
	private static String SUPPORTED_PDF_FORMAT = "application/pdf";
	
	private static final List<String> SUPPORTED_IMAGE_FORMATS = Arrays.asList("image/bmp","image/gif", "image/jpeg", "image/png", "image/tiff");
	
	private static final List<String> SUPPORTED_MSWORD_FORMATS = Arrays.asList("text/plain", "application/rtf", "text/richtext", "application/msword" /*.doc*/, 
			"application/vnd.openxmlformats-officedocument.wordprocessingml.document" /*.docx*/, "text/x-log");
	
	private static final List<String> SUPPORTED_MSEXCEL_FORMATS = Arrays.asList("application/vnd.ms-excel", 
																				"application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" /*.xlsx*/);
	
	private static final List<String> SUPPORTED_MSPPT_FORMATS = Arrays.asList("application/vnd.ms-powerpoint", 
 												"application/vnd.openxmlformats-officedocument.presentationml.presentation" /*.pptx*/);
	
	private static final List<String> SUPPORTED_MSPROJECT_FORMATS = Arrays.asList("application/vnd.ms-project");	
	
	
	/*
	 * HTTP GET UploadServlet
	 * 
	 * Required param is content id, i.e.
	 * 
	 * http://localhost/UploadServlet?id=<id of requested pdf rendition>
	 * 
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, java.io.IOException {

		logger.debug("method UploadServlet::doGet() is invoked.");	
		  
		  String fileID = request.getParameter("id");
		  
		  if(fileID==null || fileID.length()==0)
		  {
			  logger.error("UploadServlet:doGet() has no required param. Stop execution.");
	    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          return;
		  }
		  
		  // Try to find requested resource
		  ServletContext servletContext = this.getServletConfig().getServletContext();
		  File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
		  File tmpFileDirectory = new File(repository, fileID);
		  
		  if(tmpFileDirectory.exists())
		  {		  
			  // now we have to find only the one file in the chosen directory
			  File[] filesList = tmpFileDirectory.listFiles();
			  
			  if (filesList!=null && filesList.length > 0)
			  {
				  File requestedFile = filesList[0];
				  
				  // set proper Content Length and Content type for HTTP Response
			      response.setContentLength((int) requestedFile.length());
			      response.setContentType("application/pdf");
			      
			      logger.debug("Resource attributes are: filename = " + requestedFile.getName() +
			    		  "contentType = " + servletContext.getMimeType(requestedFile.getAbsolutePath()));
			      		      
				  InputStream fis = new FileInputStream(requestedFile);
				  
				  OutputStream os = response.getOutputStream();
			      byte[] bufferData = new byte[1024];
			      int read=0;
			      while((read = fis.read(bufferData))!= -1){
			            os.write(bufferData, 0, read);
			      }
			      os.flush();
			      os.close();
			      fis.close();
			  }
			  else
			  {
				  logger.error("UploadServlet:doGet() did not find requested resource. Stop execution.");
		    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		          return;
			  }
		  }
		  else
		  {
			  logger.error("UploadServlet:doGet() did not find requested resource. Stop execution.");
	    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          return;
		  }	
	}

	/*
	 * HTTP POST UploadServlet
	 * 
	 * Chould be Multipart with content attached
	 * 
	 */
	 public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, java.io.IOException {
		 boolean isMultipart;
		 String fileName = "";
         String contentType = "";
         boolean isInMemory;
         long sizeInBytes = 0;
         String fileTempID = "";
		 
		 logger.debug("method UploadServlet::doPost() is invoked.");
		 
		// Check that we have a file upload request
	      isMultipart = ServletFileUpload.isMultipartContent(request);
	      
	      if( !isMultipart ){
	    	  logger.error("UploadServlet: the form does not contain multipart content.");
	    	  response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	          return;
	       }
	      
	      // Create a factory for disk-based file items
	      DiskFileItemFactory factory = new DiskFileItemFactory();

	      // Configure a repository (to ensure a secure temp location is used)
	      ServletContext servletContext = this.getServletConfig().getServletContext();
	      File repository = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
	      
	      factory.setRepository(repository);
	      factory.setSizeThreshold(10485760); //set treshold equals to 10Mb
	      
	      // debug
	      logger.debug("javax.servlet.context.tempdir=" + repository.getCanonicalPath());

	      // Create a new file upload handler
	      ServletFileUpload upload = new ServletFileUpload(factory);
	      	      
	      try{
	    	  
	    	// Parse the request to get file items.
		      List fileItems = upload.parseRequest(request);
			
		      // Process the uploaded file items
		      Iterator i = fileItems.iterator();
		      
		      while ( i.hasNext () ) 
		      {
		    	  
		    	  FileItem fi = (FileItem)i.next();
		          if ( !fi.isFormField () )	
		          {
		        	  fileName = fi.getName();
		        	  contentType = fi.getContentType();
		        	  sizeInBytes = fi.getSize();
		        	  isInMemory = fi.isInMemory();
		        	 	             		            
		             logger.debug("fileName = " + fileName + ", contentType = " + contentType + ", fileSize =  " + sizeInBytes + ", isInMem = " + isInMemory);
		             
		             
		             /*
		              * Here the code, which
		              * 1) depend on mime type calls valid function
		              * 2) if no functions correspond to a mime type, then generate pdf doc with error
		              * 3) returns bytearray with pdf here
		              */
		             
		             ByteArrayOutputStream baos = null;
		                      	             
		             
		             if (SUPPORTED_MSWORD_FORMATS.contains(contentType))
		             {
		            	 logger.debug("got MSWORD or simple text.");
		            	 
		            	 baos = getPDFfromWord(fi.getInputStream());
		             }
		             else if(SUPPORTED_IMAGE_FORMATS.contains(contentType))
		             {
		            	 logger.debug("got image.");
		            	 
		            	 baos = getPDFfromImage(fi.getInputStream());
		            	 
		             }
		             else if(SUPPORTED_MSEXCEL_FORMATS.contains(contentType))
		             {
		            	 logger.debug("got excel sheets");
		            	 		            	      	 
		            	 baos = getPDFfromExcel(fi.getInputStream());
		             }
		             else if (SUPPORTED_MSPPT_FORMATS.contains(contentType))
		             {
		            	 logger.debug("got presentation");
		            	 
		            	 baos = getPDFfromPPT(fi.getInputStream());
		             }
		             else if (SUPPORTED_MSPROJECT_FORMATS.contains(contentType))
		             {
		            	 logger.debug("got project");
		             }
		             else if(contentType.equals(SUPPORTED_PDF_FORMAT))
		             {
		            	 logger.debug("got PDF. have nothing to convert");
		            	 		            	 		            	 
		            	 InputStream originalStream = fi.getInputStream();
		            	 		            	 
		            	 baos = new ByteArrayOutputStream();
		            	 byte[] bufferData = new byte[(int) sizeInBytes];
					     int read=0;
					     					     
					     while((read = originalStream.read(bufferData))!= -1){
					           baos.write(bufferData, 0, read);
					     }
					     
					     originalStream.close();
					     
		             }
		             else
		             {
		            	 logger.debug("got unsupported file format!");
		            	 		            	 
		            	 response.setStatus(HttpServletResponse.SC_NOT_IMPLEMENTED);
		            	 return;
		             }
		             
		             // now if outputStream is not equal to null, we have a stream with pdf. check it
					if (baos != null) {
						
						// the content is identified by fileTempID
						fileTempID = request.getSession().getId() + "_" + String.valueOf(System.currentTimeMillis());
							
						// create a temp directory for the file
						File tmpFileDirectory = new File(repository, fileTempID);
						
						if(tmpFileDirectory.mkdir())
						{	
							// this is required because  fileName = fi.getName(); returns full path name on windows.						
							fileName = FilenameUtils.getName(fileName);
							 							 
							// Now save pdf rendition of the file
							File tmpPDFFile = new File(tmpFileDirectory, FilenameUtils.removeExtension(fileName) +".pdf");
														
							FileOutputStream fileStream = new FileOutputStream(tmpPDFFile);

							try {
								// Put data
								baos.writeTo(fileStream);

							} catch (IOException ioe) {							
								// close stream and throw exception further
								fileStream.close();	
								baos.close();
								throw new IOException(ioe);
							} 
							
							fileStream.close();							
						}
								
						baos.close();
						
					} else {
						logger.error("failed to get valid outputstream with pdf. can't proceed execution of uploadservlet. HTTP 500 returned.");
						// if we have no content, we have an issue. that is we
						// can't continue.
						response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
						return;
					}
		             
		           					 
					break;
		          }  				
		      }	       
   
		      
	      }
	      catch(Exception ex)
	      {
	    	  logger.error("Exception in UploadServlet:", ex);
	    	  ex.printStackTrace();
	    	  
	    	  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    	  return;
	      }
	      
	    	 // get url for our resource
	    	 String absoluteContextRootURL = request.getRequestURL().toString().replace(request.getRequestURI().substring(1), request.getContextPath());
	    	 // get resource location
	    	 String downloadCommand = "UploadServlet?id=" + fileTempID;
	    	 String resourceLocation = absoluteContextRootURL + downloadCommand;
	    	 logger.debug("created resourceLocation: " + resourceLocation);
	    	  
			 response.setStatus(HttpServletResponse.SC_CREATED);
			 
			 response.setContentType("text/plain");
			 response.setHeader("Location", resourceLocation);
			 response.getWriter().println(resourceLocation);
			 response.flushBuffer();
			
	          
	 }
	 
	 
	 private ByteArrayOutputStream getPDFfromExcel (InputStream inputStream) {
		 
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			
			try{
				//TODO: initialize only once
				String fontPath = getServletContext().getRealPath("/WEB-INF/fonts");
				FontSettings.setFontsFolder(fontPath, true);
				
				// known aspose issue. without it, jvm will crash
				//System.setProperty("java.awt.headless", "true");
				
				System.setProperty("Aspose.Cells.Disable", "SunFontManager");
																
				Workbook workbook = new Workbook(inputStream);
								
				/*
				 * Нормально Aspose.Cell штатно преобразовывать excel в PDF не умеет.
				 * Самый простой способ преобразования Excel в DPF чтобы результат нормально выглядел
				 * нагенерить картинок из каждой закладки, а потом объединить их в одном документе
				 * Документ сохранить в PDF
				 */
				
				
				//Create an object for ImageOptions
		        ImageOrPrintOptions imgOptions = new ImageOrPrintOptions();

		        //Set the image type
		        imgOptions.setImageFormat(ImageFormat.getPng());
		        imgOptions.setOnePagePerSheet(true);
		        imgOptions.setAllColumnsInOnePagePerSheet(true);	
		        imgOptions.setPrintingPage(PrintingPageType.IGNORE_BLANK);
		        		        
		        Document newDoc = new Document();
		        		        
		        DocumentBuilder builder = new DocumentBuilder(newDoc);
		              
		        
		        for(int wj=0; wj < workbook.getWorksheets().getCount(); wj++){
			        
		        	//Get the first worksheet.
		        	Worksheet sheet = workbook.getWorksheets().get(wj);
		        	    			        	        	
		        	// move to next section
		        	builder.moveToSection(wj);
		        	
		        	//Create a SheetRender object for the target sheet
		        	SheetRender sr = new SheetRender(sheet, imgOptions);
			        for (int j = 0; j < sr.getPageCount(); j++) {
			        	// outputstream for image representing a worksheet
			        	ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
			        				        		        	
			        	  //Generate an image for the worksheet
			            sr.toImage(j, imgStream);	            
			        	
			        	ByteArrayInputStream bais = new ByteArrayInputStream(imgStream.toByteArray());
			        	
			            Image image = ImageIO.read(bais);
			            int width = image.getWidth(null);
			            int height = image.getHeight(null);
			                
			            //hack: if dimensions too large we do not set dimensions nothing, otherwise aspose draws nothing :)
			            if((width<2000) && (height<2000)) 
			            {
			                       builder.getCurrentSection().getPageSetup().setPageWidth(width);
			                       builder.getCurrentSection().getPageSetup().setPageHeight(height);
			            }
			            else
			            {
			                 if(width>height)
			                 {
			                	 builder.getCurrentSection().getPageSetup().setOrientation(Orientation.LANDSCAPE);
			                 }
			            }
			                    
			            		            
			           builder.insertImage(imgStream.toByteArray());	
			           
			           bais.close();
			           imgStream.close();
			        }
			        
			       
			      //if it is not the very last sheet, add new section
			        if(wj < (workbook.getWorksheets().getCount()-1))
			        {
				        //now insert next section		  
			            Section section = new Section(builder.getDocument());
			            
			            builder.getDocument().appendChild(section);
			        }
		            
		            
		        }
			
		        //Save the document in PDF format
		        newDoc.save(outputStream, SaveFormat.PDF);
				
		        
			}		
			catch (Exception e) {
				System.out.println("Error with a conversion of the document: " + e.getMessage());
				logger.error(e);
				e.printStackTrace();
				
				return null;
			};
			
			
			return outputStream;
		}

	
	private ByteArrayOutputStream getPDFfromImage(InputStream inputStream) {
				
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try{
			
			String fontPath = getServletContext().getRealPath("/WEB-INF/fonts");
			FontSettings.setFontsFolder(fontPath, true);
		
			Document newDoc = new Document();
	        DocumentBuilder builder = new DocumentBuilder(newDoc);
			
	        // outputstream for image 
	    	ByteArrayOutputStream imgStream = new ByteArrayOutputStream(); 	
	    	
	    	inputStream.mark(500000);
	    		        	        
	        IOUtils.copy(inputStream, imgStream);
	        
	        inputStream.reset(); 
	        
	      //Detect size
	        BufferedImage image = ImageIO.read(inputStream);
	        	       	        	        
	        int width = image.getWidth(null);
            int height = image.getHeight(null);
            
            //set size
            builder.getCurrentSection().getPageSetup().setPageWidth(width);
            builder.getCurrentSection().getPageSetup().setPageHeight(height);
	       	        
	        builder.insertImage(imgStream.toByteArray());		            
	        	        
	        // Save a document as PDF to the destination stream
	        newDoc.save(outputStream, SaveFormat.PDF);
	     	
	     	imgStream.close();
	     	inputStream.close();
		
			
		}		
		catch (Exception e) {
			System.out.println("Error with a conversion of the document: " + e.getMessage());
			logger.error(e);
			e.printStackTrace();
			
			return null;
		};
		
		return outputStream;
	}

	private ByteArrayOutputStream getPDFfromWord(InputStream inputStream) {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try{		
				
			String fontPath = getServletContext().getRealPath("/WEB-INF/fonts");
			FontSettings.setFontsFolder(fontPath, true);
		
			Document doc = new Document(inputStream);
				
			// Save a document as PDF to the destination stream
			doc.save(outputStream, SaveFormat.PDF);
		}		
		catch (Exception e) {
			System.out.println("Error with a conversion of the document: " + e.getMessage());
			logger.error(e);
			e.printStackTrace();
			
			return null;
		};
		
		return outputStream;
	}
	
	
	private ByteArrayOutputStream getPDFfromPPT(InputStream inputStream) {
		
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		
		try{		
			String fontPath = getServletContext().getRealPath("/WEB-INF/fonts");
			FontSettings.setFontsFolder(fontPath, true);
			
			Presentation pres = new Presentation(inputStream);
            
	        Document newDoc = new Document();
	        DocumentBuilder builder = new DocumentBuilder(newDoc);
	        
	        builder.getCurrentSection().getPageSetup().setOrientation(Orientation.LANDSCAPE);
	        
	        for(int sj=0; sj < pres.getSlides().size(); sj++){
	        	
	        	// move to next section
	        	builder.moveToSection(sj);
	        
	        	//Access the first slide
	        	ISlide sld = pres.getSlides().get_Item(sj);
	        
		        //User defined dimension
		        int desiredX = 1200;
		        int desiredY = 800;


		        //Getting scaled value  of X and Y
		        float ScaleX = (float)(1.0 / pres.getSlideSize().getSize().getWidth()) * desiredX;
		        float ScaleY = (float)(1.0 / pres.getSlideSize().getSize().getHeight()) * desiredY;

		        //Create a full scale image
		        BufferedImage image = sld.getThumbnail(ScaleX, ScaleY);
	            // outputstream for image representing a slide
		    	ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
	    	
		    	ImageIO.write(image, "jpeg", imgStream);
		    		    	
		    	builder.getCurrentSection().getPageSetup().setPageWidth(image.getWidth());
	            builder.getCurrentSection().getPageSetup().setPageHeight(image.getHeight());
	            
		    	builder.insertImage(imgStream.toByteArray());	
		    		    	
		    	imgStream.close();
		    	
		    	//if it is not the very last slide, add new section
		        if(sj < (pres.getSlides().size()-1))
		        {
			        //now insert next section		  
		            Section section = new Section(builder.getDocument());
		            
		            builder.getDocument().appendChild(section);
		        }
	        }
	              
	     // Save a document as PDF to the destination stream
	         newDoc.save(outputStream, SaveFormat.PDF);		
		}		
		catch (Exception e) {
			System.out.println("Error with a conversion of the document: " + e.getMessage());
			logger.error(e);
			e.printStackTrace();
			
			return null;
		};
		
		return outputStream;
	}

}
