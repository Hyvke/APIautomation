package com.itom.test.tenants;

import java.util.Map;
import org.testng.annotations.Test;

import com.itom.base.BaseClass;
import com.itom.reports.ExtentTestManager;
import com.itom.tenantsmain.ClientAPIClass;

/**
 * @author Nagendra.Kattung
 *
 */
public class ClientAPITest extends BaseClass {
  
	@Test(dataProvider = "ITOMLoginCredentials")
	  public void createClientAPITest(Map<String, String> data) throws Exception {
		 ClientAPIClass client = new ClientAPIClass();
		 client.createClientAPI(data);
		  
	  }
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void activateordeactivateClientAPITest(Map<String, String> data) throws Exception {
		ClientAPIClass client = new ClientAPIClass();
		client.activateordeactivateClientAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void getClientAPITest(Map<String, String> data) throws Exception {
		ClientAPIClass client = new ClientAPIClass();
		client.getClientAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void getMinimalClientAPITest(Map<String, String> data) throws Exception {
		ClientAPIClass client = new ClientAPIClass();
		client.getMinimalClientsAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void searchClientAPITest(Map<String, String> data) throws Exception {
		ClientAPIClass client = new ClientAPIClass();
		client.searchClientAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void updateClientAPITest(Map<String, String> data) throws Exception {
		ClientAPIClass client = new ClientAPIClass();
		client.updateClientAPI(data);
	}
}
