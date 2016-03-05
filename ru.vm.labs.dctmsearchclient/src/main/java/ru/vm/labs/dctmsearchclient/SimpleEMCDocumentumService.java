package ru.vm.labs.dctmsearchclient;


import com.documentum.com.DfClientX;
import com.documentum.fc.client.*;
import com.documentum.fc.client.search.IDfExpressionSet;
import com.documentum.fc.client.search.IDfQueryBuilder;
import com.documentum.fc.client.search.IDfQueryManager;
import com.documentum.fc.client.search.IDfQueryProcessor;
import com.documentum.fc.client.search.IDfResultEntry;
import com.documentum.fc.client.search.IDfResultsSet;
import com.documentum.fc.client.search.IDfSearchService;
import com.documentum.fc.client.search.IDfSimpleAttrExpression;
import com.documentum.fc.common.*;

import java.io.InputStream;
import java.util.*;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

public class SimpleEMCDocumentumService {
	
	private static final Logger log = Logger.getLogger(SimpleEMCDocumentumService.class);
	
	protected static IDfSessionManager sManager = null;
	
	/*
	 * !!!!!!!!!!!!HARDCODE. Login/Password/Docbase!!!!!!!!!!!!!!!!!!
	 */
	protected String userName = "administrator";
	protected String userPwd = "1";
	protected String strDocbaseName = "rosstat";
		
			
	/*
	 * In the constructor we take several simple experiments
	 */
	public SimpleEMCDocumentumService() {
		log.info("SimpleEMCDocumentumService initializing..");
		
				
	}
		
	/*
	 * 
	 */
	protected IDfSession getDCTMSession() {
		log.info("log4j - getSession() is invoked..");
		
		IDfClient client = null;
		IDfLoginInfo loginInfo = null;
		IDfSession dctmSession = null;	

		try {
			DfClientX clientx = new DfClientX();
			client = clientx.getLocalClient();
			loginInfo = new DfLoginInfo();
			if (SimpleEMCDocumentumService.sManager == null)
			{
				SimpleEMCDocumentumService.sManager = client.newSessionManager();
			
				loginInfo.setUser(userName);
				loginInfo.setPassword(userPwd);
				loginInfo.setDomain("");

				SimpleEMCDocumentumService.sManager.setIdentity(strDocbaseName, loginInfo);
			}
			

			dctmSession = SimpleEMCDocumentumService.sManager.getSession(strDocbaseName);			
			
		} catch (Exception e) {
			log.error("log4j getDCTMSession(); failed:", e);			
		}

		return dctmSession;
	}
	
	
	/*
	 * Returns an array of JSON objects. Each object represents a single row in a table of KSED outcome docs.
	 */
	public JSONArray getAllDocumentObjectsWithText(String fullsearchText)
	{
		log.info("getAllDocumentObjectsWithText() is invoked with text =  " + fullsearchText);
				
		
		IDfSession localDCTMSession = getDCTMSession();
		JSONArray ja = new JSONArray();
		
		if (localDCTMSession == null) return null;
		
		try {
			
			IDfSearchService searchService = DfClient.getLocalClient().newSearchService(SimpleEMCDocumentumService.sManager, strDocbaseName);
			IDfQueryManager queryManager = searchService.newQueryMgr();
			IDfQueryBuilder queryBuilder = queryManager.newQueryBuilder("dm_document");
			
			
			//queryBuilder.addResultAttribute("score");
			
			queryBuilder.addSelectedSource(strDocbaseName);
			
			IDfExpressionSet rootExpressionSet = queryBuilder.getRootExpressionSet();
			
			//rootExpressionSet.addFullTextExpression("document");
			
			rootExpressionSet.addSimpleAttrExpression("object_name", IDfValue.DF_STRING, 
												IDfSimpleAttrExpression.SEARCH_OP_CONTAINS, false, false, fullsearchText);
			//rootExpressionSet.addSimpleAttrExpression("summary", IDfValue.DF_STRING, 
			//		IDfSimpleAttrExpression.SEARCH_OP_CONTAINS, false, false, fullsearchText);
			
			
			queryBuilder.addResultAttribute("object_name");
			queryBuilder.addResultAttribute("summary");
			queryBuilder.addResultAttribute("r_creator_name");
			queryBuilder.addResultAttribute("r_creation_date");
			queryBuilder.addResultAttribute("r_object_id");
			
			// we limit max result count to 100 
			queryBuilder.setMaxResultCount(100);
			
			IDfQueryProcessor queryProcessor = searchService.newQueryProcessor(queryBuilder, true);
			// we limit timeout to 6 sec
			queryProcessor.blockingSearch(60000); //60k millisec.
			
			IDfResultsSet resultSet = queryProcessor.getResults();
			
			// it results every time 0. probably because
			log.info("resultSet size =  " + resultSet.size());
			
			for(int j = 0; j < resultSet.size(); j++)
			{
				IDfResultEntry entry =  resultSet.getResultAt(j);
				
				log.debug(entry.getString("object_name") + " " + entry.getString("summary") + " " + entry.getTime("r_creation_date"));
				
				JSONObject singleClub = new JSONObject();
				singleClub.put("object_name", entry.getString("object_name"));
				singleClub.put("summary", entry.getString("summary"));
				singleClub.put("author", entry.getString("r_creator_name"));
				singleClub.put("creation_date", entry.getTime("r_creation_date"));
				singleClub.put("doc_id", entry.getString("r_object_id"));	
				

				ja.put(singleClub);
								
			}
		
						
		} catch (Exception e) {			
			log.info("log4j - getAllDMDocumentObjects() error.. : ", e);				
		}
		finally{
			if(SimpleEMCDocumentumService.sManager!=null)
				SimpleEMCDocumentumService.sManager.release(localDCTMSession);
			localDCTMSession = null;
		}
		
		if(ja.length() == 0) 
			return null;
		else
			return ja;
	}
	
		
}
