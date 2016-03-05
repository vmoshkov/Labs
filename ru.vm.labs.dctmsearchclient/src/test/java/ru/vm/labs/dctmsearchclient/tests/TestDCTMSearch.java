package ru.vm.labs.dctmsearchclient.tests;

import org.json.JSONArray;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import ru.vm.labs.dctmsearchclient.SimpleEMCDocumentumService;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDCTMSearch {

	@Test
	public void TestSearchService() {
		SimpleEMCDocumentumService emcClient = new SimpleEMCDocumentumService();

		String fullsearchText = "путин";

		JSONArray ja = emcClient.getAllDocumentObjectsWithText(fullsearchText);

		System.out.println(ja);
	}

}
