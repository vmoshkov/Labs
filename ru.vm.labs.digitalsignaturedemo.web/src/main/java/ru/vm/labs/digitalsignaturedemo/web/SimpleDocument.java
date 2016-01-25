package ru.vm.labs.digitalsignaturedemo.web;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

import org.apache.commons.lang3.ArrayUtils;

import com.sleepycat.persist.model.Entity;
import com.sleepycat.persist.model.PrimaryKey;

@Entity
public class SimpleDocument {
	
	 // The primary key must be unique in the database.
    @PrimaryKey
	private String id;
	private String description;
	private Date dateOfcreation;
	private String author;
	private byte[] filebytes;
	private String fileName;
	byte[] hashbytes = new byte[1];
	private String signature;
	
	//for some reason it should exist for berkeley db
	public SimpleDocument(){
		
	}
	
	
	public SimpleDocument(String id, String description, Date dateOfcreation,
			String author, byte[] filebytes, String fileName) {
		super();
		this.id = id;
		this.description = description;
		this.dateOfcreation = dateOfcreation;
		this.author = author;
		this.filebytes = filebytes;
		this.fileName = fileName;
		
		//generateDocumentHash();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateOfcreation() {
		return dateOfcreation;
	}

	public void setDateOfcreation(Date dateOfcreation) {
		this.dateOfcreation = dateOfcreation;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public byte[] getFilebytes() {
		return filebytes;
	}

	public void setFilebytes(byte[] filebytes) {
		this.filebytes = filebytes;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	private void generateDocumentHash()
	{		
		// TODO calculate hash code (http://www.mkyong.com/java/java-sha-hashing-example/)
		byte[] idbytes = this.id.getBytes();
		byte[] descriptionbytes = this.description.getBytes();
		byte[] datebytes = this.dateOfcreation.toGMTString().getBytes();
		byte[] authorbytes = this.author.getBytes();
		//filebytes;
		
		byte[] sumOne = ArrayUtils.addAll(idbytes, descriptionbytes);
		byte[] sumTwo = ArrayUtils.addAll(sumOne, datebytes);
		byte[] sumThree = ArrayUtils.addAll(sumTwo, authorbytes);
		byte[] sumFour = ArrayUtils.addAll(sumThree, filebytes);
		
		
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			
			hashbytes = md.digest(sumFour);		
			
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public byte[] getDocumentHash()
	{
		return hashbytes;
	}
	
	public String getDocumentHashAsHexString()
	{
		generateDocumentHash();
		
		//convert the byte to hex format for debug purposes
        StringBuffer hexString = new StringBuffer();
    	for (int i=0;i<hashbytes.length;i++) {
    	  hexString.append(Integer.toHexString(0xFF & hashbytes[i]));
    	} 	
    	
		return hexString.toString();
	}
	
	public String getSignature() {
		return signature;
	}


	public void setSignature(String signature) {
		this.signature = signature;
	}
}
