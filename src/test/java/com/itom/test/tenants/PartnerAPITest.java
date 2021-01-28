package com.itom.test.tenants;

import org.testng.annotations.Test;
import java.util.Map;
import com.itom.base.BaseClass;
import com.itom.reports.ExtentTestManager;
import com.itom.tenantsmain.PartnerAPIClass;


public class PartnerAPITest extends BaseClass {

	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void CreatePartnerAPITest(Map<String, String> data) throws Exception {
		 PartnerAPIClass prtn = new PartnerAPIClass();
		 prtn.createPartnerAPI(data);
		  //ExtentTestManager.passResults("alert creation is successfully passed", "");
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void getPartnerAPITest(Map<String,String> data) throws Exception {
		  PartnerAPIClass prtn = new PartnerAPIClass();
			 prtn.getPartner(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void TerminatePartnerAPITest(Map<String,String> data) throws Exception {
		  PartnerAPIClass prtn = new PartnerAPIClass();
			 prtn.TerminatePartner(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void SuspendPartnerAPITest(Map<String,String> data) throws Exception {
		  PartnerAPIClass prtn = new PartnerAPIClass();
			 prtn.SuspendPartner(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void ActivatePartnerAPITest(Map<String,String> data) throws Exception {
		  PartnerAPIClass prtn = new PartnerAPIClass();
			 prtn.ActivatePartner(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void SearchPartnerAPITest(Map<String,String> data) throws Exception {
		  PartnerAPIClass prtn = new PartnerAPIClass();
			 prtn.SearchPartner(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void UpdatePartnerAPITest(Map<String,String> data) throws Exception {
		  PartnerAPIClass prtn = new PartnerAPIClass();
			 prtn.UpdatePartner(data);
	  }
	  
	  
}
