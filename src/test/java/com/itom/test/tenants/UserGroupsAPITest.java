package com.itom.test.tenants;

import java.util.Map;

import org.testng.annotations.Test;

import com.itom.base.BaseClass;
import com.itom.tenantsmain.UserGroupAPIClass;

/**
 * @author Nagendra.Kattung
 *
 */
public class UserGroupsAPITest extends BaseClass{
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void createUserGroupAPITest(Map<String, String> data) throws Exception {
		UserGroupAPIClass userGroup = new UserGroupAPIClass();
		userGroup.createUserGroupAPI(data);
		
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void addUserToUserGroupAPITest(Map<String, String> data) throws Exception {
		UserGroupAPIClass userGroup = new UserGroupAPIClass();
		userGroup.addUserToUserGroupAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void deleteUserGroupAPITest(Map<String, String> data) throws Exception {
		UserGroupAPIClass userGroup = new UserGroupAPIClass();
		userGroup.deleteUserGroupAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void getUserGroupIdAPITest(Map<String, String> data) throws Exception {
		UserGroupAPIClass userGroup = new UserGroupAPIClass();
		userGroup.getUserGroupAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void getUsersOfUserGroupAPITest(Map<String, String> data) throws Exception {
		UserGroupAPIClass userGroup = new UserGroupAPIClass();
		userGroup.getUsersOfUserGroupAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void removeUsersFromUserGroupAPITest(Map<String, String> data) throws Exception {
		UserGroupAPIClass userGroup = new UserGroupAPIClass();
		userGroup.removeUsersFromUserGroupAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void searchUserGroupsAPITest(Map<String, String> data) throws Exception {
		UserGroupAPIClass userGroup = new UserGroupAPIClass();
		userGroup.searchUserGroupsAPI(data);
	}
	
	@Test(dataProvider = "ITOMLoginCredentials")
	public void updateUserGroupAPITest(Map<String, String> data) throws Exception {
		UserGroupAPIClass userGroup = new UserGroupAPIClass();
		userGroup.updateUserGroupAPI(data);
	}
}
