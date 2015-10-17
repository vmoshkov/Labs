package ru.vm.labs.mq.test;


import java.io.File;
import java.util.Enumeration;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQSession;
import org.apache.activemq.BlobMessage;
import org.apache.activemq.blob.BlobTransferPolicy;

import javax.jms.Connection;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;
import javax.jms.TextMessage;

public class TestWorkWithMQ {
	
	private static String brokerURL = "tcp://localhost:61616";
	private static String fileName = "files/example1.xls";
	
	
	@Test
	public void testAcreateSimpleMessage()
	{
		System.out.println((System.currentTimeMillis()) + ": createSimpleMessage() started.");
		
		// Create a connection factory referring to the broker host and port
	    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
	    
	    try {
	    
	    // Create a Connection
        Connection connection = factory.createConnection();
        connection.start();
        
        // Create a non-transactional session with automatic acknowledgement
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
     // Create a reference to the queue test_queue in this session. Note
        //  that ActiveMQ has auto-creation enabled by default, so this JMS
        //  destination will be created on the broker automatically
        Queue queue = session.createQueue("test_queue");
        
     // Create a producer for test_queue
        MessageProducer producer = session.createProducer(queue);
        
        // Create a simple text message and send it
        TextMessage message = session.createTextMessage ("Hello world! From: " + Thread.currentThread().getName() + " : " + this.hashCode());
        message.setJMSType("text message");
        producer.send(message);
        
        // Clean up
        session.close();
        connection.close();
        
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    	//System.out.println(ex.getMessage());
	    }
		
		System.out.println((System.currentTimeMillis()) + ": createSimpleMessage() completed.");
	}
	
	@Test
	public void testBcreateBlobMessage()
	{
		System.out.println((System.currentTimeMillis()) + ": createBlobMessage() started.");
		
		// Create a connection factory referring to the broker host and port
	    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
	    
	    try {
	    
	    // Create a Connection
        ActiveMQConnection connection = (ActiveMQConnection) factory.createConnection();
        connection.start();
        
        BlobTransferPolicy btp = new BlobTransferPolicy();
        
        //jms.blobTransferPolicy.UploadUrl=http://admin:admin@localhost:8161/fileserver/
        btp.setUploadUrl("http://admin:admin@localhost:8161/fileserver/");
        
        connection.setBlobTransferPolicy(btp);
        
        // Create a non-transactional session with automatic acknowledgement
        ActiveMQSession session = (ActiveMQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        
        
        // Create a reference to the queue test_queue in this session. Note
        //  that ActiveMQ has auto-creation enabled by default, so this JMS
        //  destination will be created on the broker automatically
        Queue queue = session.createQueue("test_queue");
        
        
        // Create a producer for test_queue
        MessageProducer producer = session.createProducer(queue);
        
        //Get file from resources folder
    	ClassLoader classLoader = getClass().getClassLoader();
    	File file = new File(classLoader.getResource(fileName).getFile());
                
    	// lets use a local file
    	BlobMessage message = session.createBlobMessage(file);
    	message.setJMSType("blob");
    	    	
        producer.send(message);
        
        // Clean up
        session.close();
        connection.close();
        
	    }
	    catch(Exception ex)
	    {
	    	ex.printStackTrace();
	    	//System.out.println(ex.getMessage());
	    }
		
		System.out.println((System.currentTimeMillis()) + ": createBlobMessage() completed.");
	}
	
	@Test
	public void testCreadSimpleMessage()
	{
		System.out.println((System.currentTimeMillis()) + ": readSimpleMessage() started.");
		
		// Create a connection factory referring to the broker host and port
	    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
	    
	    try {
		    
		    // Create a Connection
	        Connection connection = factory.createConnection();
	        connection.start();
	        
	        // Create a non-transactional session with automatic acknowledgement
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        
	     // Create a reference to the queue test_queue in this session. Note
	        //  that ActiveMQ has auto-creation enabled by default, so this JMS
	        //  destination will be created on the broker automatically
	        Queue queue = session.createQueue("test_queue");
	        
	        // Create a MessageConsumer from the Session to the Topic or Queue
            MessageConsumer consumer = session.createConsumer(queue);
          
            // Wait for a message
            Message message = consumer.receive(1000);

            if (message instanceof TextMessage) {
                TextMessage textMessage = (TextMessage) message;
                String text = textMessage.getText();
                System.out.println("Received: " + text);
            } else {
                System.out.println("Received: " + message);
            }

            consumer.close();
            session.close();
            connection.close();
	        
		    }
		    catch(Exception ex)
		    {
		    	ex.printStackTrace();
		    	//System.out.println(ex.getMessage());
		    }
	}
	
	@Test
	public void testDreadAllMessages()
	{
		System.out.println((System.currentTimeMillis()) + ": readAllMessages() started.");
		
		// Create a connection factory referring to the broker host and port
	    ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
	    
	    try {
		    
		    // Create a Connection
	        Connection connection = factory.createConnection();
	        connection.start();
	        
	        // Create a non-transactional session with automatic acknowledgement
	        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
	        
	     // Create a reference to the queue test_queue in this session. Note
	        //  that ActiveMQ has auto-creation enabled by default, so this JMS
	        //  destination will be created on the broker automatically
	        Queue queue = session.createQueue("test_queue");
	        
	        // Create a QueueBrowser from the Session to the Topic or Queue
            QueueBrowser browser = session.createBrowser(queue);
            
            Enumeration<?> messagesInQueue = browser.getEnumeration();
            
                        
            while (messagesInQueue.hasMoreElements()) {
                Message queueMessage = (Message) messagesInQueue.nextElement();
                System.out.println(queueMessage);
            }
                    

            browser.close();
            session.close();
            connection.close();
	        
		    }
		    catch(Exception ex)
		    {
		    	ex.printStackTrace();
		    	//System.out.println(ex.getMessage());
		    }
	}

}
