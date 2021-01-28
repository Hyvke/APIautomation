package com.itom.test.tenants;

import java.util.Map;

import org.testng.annotations.Test;

import com.itom.base.BaseClass;
import com.itom.tenantsmain.ClientNoteAPIClass;

/**
 * @author Nagendra.Kattung
 *
 */
public class ClientNoteAPITest extends BaseClass{
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void createClientNoteAPITest(Map<String, String> data) throws Exception {
		ClientNoteAPIClass clientNote = new ClientNoteAPIClass();
		clientNote.createClientNoteAPI(data);
	}
	

	@Test(dataProvider = "ITOMLoginCredentials")
	public void deleteClientNoteAPITest(Map<String, String> data) throws Exception {
		ClientNoteAPIClass clientNote = new ClientNoteAPIClass();
		clientNote.deleteClientNoteAPI(data);
	}
	

	@Test(dataProvider = "ITOMLoginCredentials")
	public void getClientNoteDetailsAPITest(Map<String, String> data) throws Exception {
		ClientNoteAPIClass clientNote = new ClientNoteAPIClass();
		clientNote.getClientNoteDetailsAPI(data);
	}
	

	@Test(dataProvider = "ITOMLoginCredentials")
	public void searchClientNotesAPITest(Map<String, String> data) throws Exception {
		ClientNoteAPIClass clientNote = new ClientNoteAPIClass();
		clientNote.searchClientNotesAPI(data);
	}
	

	@Test(dataProvider = "ITOMLoginCredentials")
	public void updateClientNoteAPITest(Map<String, String> data) throws Exception {
		ClientNoteAPIClass clientNote = new ClientNoteAPIClass();
		clientNote.updateClientNoteAPI(data);
	}

}
