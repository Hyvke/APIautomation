package com.itom.test.tenants;

import java.util.Map;

import org.testng.annotations.Test;

import com.itom.base.BaseClass;
import com.itom.tenantsmain.RolesAPIClass;

/**
 * @author Nagendra.Kattung
 *
 */
public class RolesAPITest extends BaseClass{
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void createRoleAPITest(Map<String, String> data) throws Exception {
		RolesAPIClass role = new RolesAPIClass();
		role.createRoleAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void deleteRoleAPITest(Map<String, String> data) throws Exception {
		RolesAPIClass role = new RolesAPIClass();
		role.deleteRoleAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void getRoleAPITest(Map<String, String> data) throws Exception {
		RolesAPIClass role = new RolesAPIClass();
		role.getRoleAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void searchRoleAPITest(Map<String, String> data) throws Exception {
		RolesAPIClass role = new RolesAPIClass();
		role.searchRolesAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void updateRoleAPITest(Map<String, String> data) throws Exception {
		RolesAPIClass role = new RolesAPIClass();
		role.updateRoleAPI(data);
	}
}
