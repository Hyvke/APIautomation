package com.itom.tenantsmain;

import java.util.HashMap;
import java.util.Map;

import com.itom.api.APIUtil;
import com.itom.rest.APIUri;

import io.restassured.response.Response;

/**
 * @author Nagendra.Kattung
 *
 */
public class UserGroupAPIClass extends APIUtil{
	
	public void createUserGroupAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenantId", getProperty("clientID"));
		Map<String,Response> response = methodPOST(APIUri.CREATE_USER_GROUPS, data.get("FileName"), map);
		saveProperty("userGroupID", response.get("Create UserGroup Test case 1: Create_User_Group values").jsonPath().getString("uniqueId"));
		saveProperty("userGroupName", response.get("Create UserGroup Test case 1: Create_User_Group values").jsonPath().getString("name"));
	}
	
	public void addUserToUserGroupAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
		map.put("tenantId", getProperty("clientID"));
		}else {
			map.put("tenantId",data.get("tenantId"));
		}
		if(data.get("userGroupId").equalsIgnoreCase("FromResponse")) {
		map.put("userGroupId", getProperty("userGroupID"));
		}else {
			map.put("userGroupId",data.get("userGroupId"));
		}
		methodPOST(APIUri.ADD_USER_TO_USER_GROUPS, data.get("FileName"), map);
		
	}
	
	public void deleteUserGroupAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
		  map.put("tenantId", getProperty("clientID"));
		}else {
			map.put("tenantId", data.get("tenantId"));
		}
		if(data.get("userGroupId").equalsIgnoreCase("FromResponse")) {
			map.put("userGroupId", getProperty("userGroupID"));
		}else {
			map.put("userGroupId", data.get("userGroupId"));
		}
		
		DELETE(APIUri.DELETE_USER_GROUP, data ,map);
	}
	
	public void getUserGroupAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("orgId").equalsIgnoreCase("FromResponse")) {
			map.put("orgId", getProperty("clientID"));
		}else {
			map.put("orgId", data.get("orgId"));
		}
		if(data.get("userGroupId").equals("FromResponse")) {
			map.put("userGroupId", getProperty("userGroupID"));
		}else {
			map.put("userGroupId", data.get("userGroupId"));
		}
		methodGET(APIUri.GET_USER_GROUPS, map , data);
	}
	
	public void getUsersOfUserGroupAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("clientID"));
		}else {
			map.put("tenantId",data.get("tenantId"));
		}
		
		if(data.get("userGroupId").equals("FromResponse")) {
			map.put("userGroupId", getProperty("userGroupID"));
		}else {
			map.put("userGroupId", data.get("userGroupId"));
		}
		 String Url=searchFilter(APIUri.GET_USERS_OF_USERGROUP, data);
		methodGET(Url, map ,data);
		
	}
	
	public void removeUsersFromUserGroupAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("clientID"));
		}else {
			map.put("tenantId", data.get("tenantId"));
		}
		if(data.get("userGroupId").equals("FromResponse")) {
			map.put("userGroupId", getProperty("userGroupID"));
		}else {
			map.put("userGroupId", data.get("userGroupId"));
		}
		methodDELETE(APIUri.REMOVE_USERS_FROM_USERGROUP, data.get("FileName"), map);
		//methodDELETE(APIUri.REMOVE_USERS_FROM_USERGROUP, data ,map);
	}
	
	public void searchUserGroupsAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String query = "?pageNo="+data.get("pageNo")+"&pageSize="+data.get("pageSize")+"&isDescendingOrder="+data.get("isDescendingOrder");
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("clientID"));
		}else {
			map.put("tenantId", data.get("tenantId"));
		}
		methodGET(APIUri.SEARCH_USERGROUPS+query, map ,data);
	}
	
	public void updateUserGroupAPI(Map<String, String> data) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("clientID"));
		}
		else {
			map.put("tenantId", data.get("tenantId"));
		}
		if(data.get("userGroupId").equalsIgnoreCase("FromResponse")) {
			map.put("userGroupId", getProperty("userGroupID"));
		}
		else {
			map.put("userGroupId", data.get("userGroupId"));
		}		
		methodPOST(APIUri.UPDATE_USERGROUP, data.get("FileName"), map);
	}

}
