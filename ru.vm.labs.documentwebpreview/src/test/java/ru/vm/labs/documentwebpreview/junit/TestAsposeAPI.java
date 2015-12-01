package ru.vm.labs.documentwebpreview.junit;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import com.aspose.cells.Cell;
import com.aspose.cells.ImageFormat;
import com.aspose.cells.ImageOrPrintOptions;
import com.aspose.cells.PrintingPageType;
import com.aspose.cells.SheetRender;
import com.aspose.cells.Workbook;
import com.aspose.cells.Worksheet;
import com.aspose.words.DocumentBuilder;
import com.aspose.words.FontSettings;
import com.aspose.words.License;
import com.aspose.words.Document;
import com.aspose.words.Orientation;
import com.aspose.words.SaveFormat;
import com.aspose.words.Section;
import com.aspose.slides.ISlide;
import com.aspose.slides.Presentation;
import com.aspose.tasks.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestAsposeAPI {
	
	@Before
	public void prepareTestAspose()
	{		
		// Do required initialization

				try {
					License license = new License();
					license.setLicense("Aspose.Total.lic");
					System.out.println("License set successfully.");					
					
				} catch (Exception e) {
					// We do not ship any license with this example, visit the Aspose
					// site to obtain either a temporary or permanent license.
					System.out.println("There was an error setting the license: "
							+ e.getMessage());

				}
	}
	
	/*
	@Test
	public void checkTasksConverion() {
		long startTime = 0;
		long finishTime = 0;
		
		System.out.println((startTime = System.currentTimeMillis()) + ": checkTasksConverion() started.");
		
		try{
		// Read the input Project file
		Project project = new Project(this.getClass().getClassLoader().getResourceAsStream("files/project1.mpp"));
		
		
		// Save the Project as PDF
		project.save("compiled_tasks_document.pdf", SaveFileFormat.PDF);	
		}
		catch(Exception e)
		{
			System.out.println("There was an error with a document: " + e.getMessage());
			e.printStackTrace();
		}
		System.out.println((finishTime = System.currentTimeMillis()) + ": checkTasksConverion() completed.");
		
		// If conversion time took more then 30k ms then warn
		if((finishTime - startTime) > 30000)
		{
				System.out.println("checkTasksConverion() warning! operation took to long: " + (finishTime - startTime) + " millisecs");
		}
	}
	*/	
	
	
	//@Test
	public void checkSlidesConversion() {
		long startTime = 0;
		long finishTime = 0;
		
		System.out.println((startTime = System.currentTimeMillis()) + ": checkSlidesConversion() started");
		
		try
		{
		 //Instantiate a Presentation object that represents a presentation file
        Presentation pres = new Presentation(this.getClass().getClassLoader().getResourceAsStream("files/example1.pptx"));
                
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
              
         // Save now as file
         newDoc.save("compiled_slides_document.pdf", SaveFormat.PDF);
        }
       
		catch(Exception e)
		{
			System.out.println("There was an error with a document: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println((finishTime = System.currentTimeMillis()) + ": checkSlidesConversion() completed");
		
		// If conversion time took more then 30k ms then warn
		if((finishTime - startTime) > 30000)
		{
			System.out.println("checkSlidesConversion() warning! operation took to long: " + (finishTime - startTime) + " millisecs");
		}
	}
	
	
	//@Test
	public void checkCellConversion() {
		long startTime = 0;
		long finishTime = 0;
		
		System.out.println((startTime = System.currentTimeMillis()) + ": checkCellConversion() started");
		
		// known aspose cell issue (must  be set, otherwise aspose cell will crash jvm process)
		//System.setProperty("java.awt.headless", "true");
		System.setProperty("Aspose.Cells.Disable", "SunFontManager");

		try
		{
			Workbook workbook = new Workbook(this.getClass().getClassLoader().getResourceAsStream("files/example2.xlsx"));
			
			System.out.println("excel filename = " + workbook.getFileName());
			System.out.println("excel worksheets count = " + workbook.getWorksheets().getCount());
			
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
	        		        	
	        	/*
	        	System.out.println("max columns = " + sheet.getCells().getMaxColumn() + "; max rows = " + sheet.getCells().getMaxRow());
	        	
	        	for (int n = 0; n< sheet.getCells().getMaxRow(); n++)
	        	{
	        		for (int m = 0; m<sheet.getCells().getMaxColumn(); m++)
	        		{
	        			Cell aCell = sheet.getCells().get(n, m);
	        			
	        			System.out.println("cell value [" + n + "," + m + "]= " + aCell.getStringValue() + "; font = " + aCell.getDisplayStyle().getFont().getName() + "; isColumnHidden = " + sheet.getCells().getColumns().get(aCell.getColumn()).isHidden());
	        			
	        			
	        						
	        		}
	        	}
	        	*/
	        	        	
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
		            //builder.getCurrentSection().getPageSetup().clearFormatting();
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
		          
		            
		            System.out.println("width=" + width + ", height="+height);
		            		            
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
			        
	       	// Save now as file
			newDoc.save("compiled_excel_document.pdf", SaveFormat.PDF);					
		}
		catch(Exception e)
		{
			System.out.println("There was an error with a document: " + e.getMessage());
			e.printStackTrace();
		}

		System.out.println((finishTime = System.currentTimeMillis()) + ": checkCellConversion() completed");
		
		// If conversion time took more then 30k ms then warn
		if((finishTime - startTime) > 30000)
		{
			System.out.println("checkCellConversion() warning! operation took to long: " + (finishTime - startTime) + " millisecs");
		}
	}
	
	@Test
	public void checkImageConversion() {
		long startTime = 0;
		long finishTime = 0;
		
		System.out.println((startTime = System.currentTimeMillis()) + ": checkImageConversion() started");
		
		try
		{
			Document newDoc = new Document();
	        DocumentBuilder builder = new DocumentBuilder(newDoc);
			
	        // outputstream for image representing a worksheet
	    	ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
	    	
	    	InputStream fis = this.getClass().getClassLoader().getResourceAsStream("files/picture1.jpg");
	        		        
	    	fis.mark(500000);
	    	
	        //Detect size
	        BufferedImage image = ImageIO.read(fis);
	        	              
	        if(image==null)
	        {
	        	System.out.println("image is null!");
	        	return;
	        }
	        	        
	        int width = image.getWidth(null);
            int height = image.getHeight(null);
            System.out.println("width=" + width + ", height="+height);
	      
            fis.reset();        
	        IOUtils.copy(fis, imgStream);
	        fis.close();
	        
	        builder.getCurrentSection().getPageSetup().setPageWidth(width);
            builder.getCurrentSection().getPageSetup().setPageHeight(height);
	        
	        builder.insertImage(imgStream.toByteArray());		            
	        	        
	        // Save now as file
	     	newDoc.save("compiled_picture_document.pdf", SaveFormat.PDF);
	     	
	     	imgStream.close();
		}
		catch(Exception e)
		{
			System.out.println("There was an error with a document: " + e.getMessage());
			e.printStackTrace();
		}
		
		System.out.println((finishTime = System.currentTimeMillis()) + ": checkImageConversion() completed");
		
		// If conversion time took more then 30k ms then warn
		if((finishTime - startTime) > 30000)
		{
			System.out.println("checkImageConversion() warning! operation took to long: " + (finishTime - startTime) + " millisecs");
		}
		
	}
	
}
