package com.itom.test.tenants;

import java.util.Map;

import org.testng.annotations.Test;

import com.itom.base.BaseClass;
import com.itom.tenantsmain.UserAPIClass;

/**
 * @author Nagendra.Kattung
 *
 */
public class UserAPITest extends BaseClass{
  

	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void CreateUserAPITest(Map<String, String> data) throws Exception {
		 UserAPIClass prtn = new UserAPIClass();
		 prtn.createUserAPI(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void getUserAPITest(Map<String, String> data) throws Exception {
		 UserAPIClass prtn = new UserAPIClass();
		 prtn.getUserAPI(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void searchUserAPITest(Map<String, String> data) throws Exception {
		 UserAPIClass prtn = new UserAPIClass();
		 prtn.searchUserAPI(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void searchSPUserAPITest(Map<String, String> data) throws Exception {
		 UserAPIClass prtn = new UserAPIClass();
		 prtn.searchSPUserAPI(data);
	  }
	  
	  @Test(dataProvider = "ITOMLoginCredentials")
	  public void getMinimalUserAPITest(Map<String, String> data) throws Exception {
		 UserAPIClass prtn = new UserAPIClass();
		 prtn.minimulUserAPI(data);
	  }
	  @Test(dataProvider="ITOMLoginCredentials")
	  public void updateuserAPITest(Map<String,String>data)throws Exception{
		  UserAPIClass prtn= new UserAPIClass();
		  prtn.updateUserAPI(data);
	  } 
	
	/*
	 * @Test(dataProvider = "ITOMLoginCredentials") public void
	 * getLoginHistoryOfTenantUserAPITest(Map<String, String> data) throws Exception
	 * { UserAPIClass prtn = new UserAPIClass(); prtn.minimulUserAPI(data); }
	 * 
	 * @Test(dataProvider = "ITOMLoginCredentials") public void
	 * getLoginHistoryOfSPAPITest(Map<String, String> data) throws Exception {
	 * UserAPIClass prtn = new UserAPIClass(); prtn.minimulUserAPI(data); }
	 */
}
