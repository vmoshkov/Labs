package ru.vm.labs.digitalsignaturedemo.junit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.cert.Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.UnrecoverableKeyException;
import java.util.Enumeration;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDigitalSignatureAPI {
	
	// As it is a pain to export private key from key store, it's more easy to create and save private key yourself
	@Test
	public void generateKeyPair() {
		// Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider(..) - for future ...
		
		System.out.println("generateKeyPair() works...");
		
		try {
			// Create the public and private keys
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(1024);
			
			KeyPair keyPair = generator.genKeyPair();
			
			PrivateKey privateKey = keyPair.getPrivate();
		    PublicKey publicKey = keyPair.getPublic();
		    
		    System.out.println("private key format = " + privateKey.getFormat());
		    System.out.println("public key format = " + publicKey.getFormat());
		    
		    
		    // Store Public Key.
			X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
			FileOutputStream fos = new FileOutputStream("test_public_key.cer");
			fos.write(x509EncodedKeySpec.getEncoded());
			fos.close();
	 
			// Store Private Key.
			PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
			fos = new FileOutputStream("test_private_key.key");
			fos.write(pkcs8EncodedKeySpec.getEncoded());
			fos.close();	    
		    
		}
		catch (Exception e) {
            System.out.println(e);
        }
		
	}
	
	@Test
	public void generateSignature() {	
		
		System.out.println("generateSignature() works...");
		
		try{		
	 
			// Read Private Key.
			File filePrivateKey = new File("test_private_key.key");
			FileInputStream  fis = new FileInputStream("test_private_key.key");
			byte[] encodedPrivateKey = new byte[(int) filePrivateKey.length()];
			fis.read(encodedPrivateKey);
			fis.close();
	 
			// Generate KeyPair.
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");	
	 
			PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
			PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);
			
			
			//CertificateFactory cf = CertificateFactory.getInstance("X.509");
		    
		    System.out.println("default store type=" + KeyStore.getDefaultType()); 
				        	                
	        if(privateKey==null)
	        {
	        	System.out.println("Privatekey == null!!!");
	        }
	        else
	        	//DEBUG
	            System.out.println("private key = " + Base64.encodeBase64String(privateKey.getEncoded()));
            
            //
            // Get an instance of Signature object and initialize it.
            //
            Signature signature = Signature.getInstance("SHA256withRSA", "SunRsaSign");
            signature.initSign(privateKey);
                        
            
            //
            // Supply the data to be signed to the Signature object
            // using the update() method and generate the digital
            // signature.
            //
            byte[] bytes = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream("files/demo.docx"));
            signature.update(bytes);
            byte[] digitalSignature = signature.sign();
            
            String strSignature = Base64.encodeBase64String(digitalSignature);
            
            System.out.println("digital signature = " + strSignature);
            
            //save digital signature
            FileOutputStream fos = new FileOutputStream("signature");
			fos.write(digitalSignature);
			fos.close();
        
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void validateSignature() {
		
		System.out.println("validateSignature() works...");
		
		try{
			// Read Public Key.
			File filePublicKey = new File("test_public_key.cer");
			FileInputStream fis = new FileInputStream("test_public_key.cer");
			byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
			fis.read(encodedPublicKey);
			fis.close();
			
			//Reas Digital Signature
			File fileDigitalSignature = new File("signature");
			fis = new FileInputStream("signature");
			byte[] encodedDigitalSignature = new byte[(int) fileDigitalSignature.length()];
			fis.read(encodedDigitalSignature);
			fis.close();
			
			// Generate KeyPair.
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
			PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);
			
			//
            // Get an instance of Signature object and initialize it.
            //
            Signature signature = Signature.getInstance("SHA256withRSA", "SunRsaSign");
            signature.initVerify(publicKey);
            
            byte[] bytes = IOUtils.toByteArray(this.getClass().getClassLoader().getResourceAsStream("files/demo.docx"));
            signature.update(bytes);
			
            boolean verified = signature.verify(encodedDigitalSignature);
            if (verified) {
                System.out.println("Data verified.");
            } else {
                System.out.println("Cannot verify data.");
            }
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	 
	
	//@Test
	public void getTrustedCertificatesList() {
		
		FileInputStream is = null;
		
		try {
								
			String cacertsPath = System.getProperty("java.home") + "/lib/security/cacerts";
			
			System.out.println(cacertsPath);
			
	        File file = new File(cacertsPath);
	        is = new FileInputStream(file);
	        
	        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
	        String password = "changeit"; //default password for cacerts
	        keystore.load(is, password.toCharArray());


	        Enumeration<String> enumeration = keystore.aliases();
	        while(enumeration.hasMoreElements()) {
	            String alias = (String)enumeration.nextElement();
	            //System.out.println("alias name: " + alias);
	            Certificate certificate = keystore.getCertificate(alias);
	            
	            Key Privatekey = keystore.getKey(alias, password.toCharArray());
	            String encodedKey = Base64.encodeBase64String(Privatekey!=null? Privatekey.getEncoded() : new byte[1]);
	            
	            Key Publickey = certificate.getPublicKey();
	            String strKey = Base64.encodeBase64String(Publickey!=null? Publickey.getEncoded() : new byte[1]);
	            
	            //if (alias.equals("vmtestcert"))
	            System.out.println("alias name: " + alias +"; Privatekey = " + encodedKey + "; Public Key = " + strKey);
	            
	            

	        }

	    }catch(UnrecoverableKeyException e){
	    			
		}catch (java.security.cert.CertificateException e) {
	        e.printStackTrace();
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (KeyStoreException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }finally {
	        if(null != is)
	            try {
	                is.close();
	            } catch (IOException e) {
	                // TODO Auto-generated catch block
	                e.printStackTrace();
	            }
	    }
		
	}

}
