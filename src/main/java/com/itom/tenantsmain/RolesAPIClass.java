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
public class RolesAPIClass extends APIUtil{
	
	public void createRoleAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenantId", getProperty("clientID"));
		Map<String, Response> response = methodPOST(APIUri.CREATE_ROLE, data.get("FileName"), map);
		if(response.get("Create Role Test case 1: Create_Role Default values").getStatusCode()==200) {
			System.out.print("role id"+response.get("Create Role Test case 1: Create_Role Default values").jsonPath().getString("uniqueId"));
			saveProperty("roleID", response.get("Create Role Test case 1: Create_Role Default values").jsonPath().getString("uniqueId"));
			saveProperty("roleName", response.get("Create Role Test case 1: Create_Role Default values").jsonPath().getString("name"));
		}
	}
	
	public void deleteRoleAPI(Map<String, String> data) {
		Map<String, String> map = new HashMap<String, String>();
		
	}
	
	public void getRoleAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("clientID"));
		}else {
			map.put("tenantId", data.get("tenantId"));
		}
		if(data.get("roleId").equalsIgnoreCase("FromResponse")) {
			map.put("roleId", getProperty("roleID"));
		}
		else {
			map.put("roleId", data.get("roleId"));
		}
		methodGET(APIUri.GET_ROLE, map, data);
	}
	
	public void searchRolesAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String query = "?pageNo="+data.get("pageNo")+"&pageSize="+data.get("pageSize")+"&isDescendingOrder="+data.get("isDescendingOrder")+"&sortName="+data.get("sortName");
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("clientID"));
		}
		else {
			map.put("tenantId", data.get("tenantId"));
		}
		methodGET(APIUri.SEARCH_ROLE+query, map, data);
	}
	
	public void updateRoleAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
			map.put("tenantId", getProperty("clientID"));
		if(data.get("roleId").equalsIgnoreCase("FromResponse")) {
			map.put("roleId", getProperty("roleID"));
		}else {
			map.put("roleId", data.get("roleId"));
		}
		methodPOST(APIUri.UPDATE_ROLE, data.get("FileName"), map);
	}

}
