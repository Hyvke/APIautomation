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
public class UserAPIClass extends APIUtil {

	public void createUserAPI(Map<String, String> data) throws Exception{
		Map<String, String> map = new HashMap<String,String>();
		if(data.get("orgId").equalsIgnoreCase("FromResponse")){
			map.put("orgId", getProperty("clientID"));
		}else {
			map.put("orgId", data.get("orgId"));
		}
		Map<String,Response> responses = methodPOST(APIUri.CREATE_USER,data.get("FileName"),map);
		if(responses.get("Create User Test case 1: CREATE_USER_Default values").getStatusCode()==200) {
			System.out.println("mspid : "+responses.get("Create User Test case 1: CREATE_USER_Default values").jsonPath().getString("uniqueId"));
			saveProperty("userId", responses.get("Create User Test case 1: CREATE_USER_Default values").jsonPath().getString("id"));
			saveProperty("userLoginName", responses.get("Create User Test case 1: CREATE_USER_Default values").jsonPath().getString("loginName"));
		}
	}

	public void getUserAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
		map.put("tenantId", getProperty("clientID"));
		}else
		{
			map.put("tenantId", data.get("tenantId"));
		}
		if(data.get("userId").equalsIgnoreCase("FromResponse")) {
			map.put("userId", getProperty("userId"));
		} else {
			map.put("userId", data.get("userId"));
		}
		methodGET(APIUri.GET_USER, map , data);
		
	}

	public void searchUserAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse"))
		{
		map.put("tenantId", getProperty("clientID"));
		}else {
			map.put("tenantId",data.get("tenantId"));
		}
	/*	if(data.get("userId").equalsIgnoreCase("FromResponse")&&data.get("tenantId").equalsIgnoreCase("FromResponse")){
			//map.put("userId", getProperty("userId"));
			map.put("tenantId", getProperty("clientID"));
		}
		else if(data.get("userId").equalsIgnoreCase("FromResponse")&&!data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			//map.put("userId", getProperty("userId"));
			map.put("tenantId", data.get("tenantId"));
		}
		else if(!data.get("userId").equalsIgnoreCase("FromResponse")&&data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			//map.put("userId", data.get("userId"));
			map.put("tenantId", getProperty("clientID"));
		}
		else {
			//map.put("userId", data.get("userId"));
			map.put("tenantId",data.get("tenantId"));
		} */
		String url = searchFilter(APIUri.SEARCH_USER,data);
		methodGET(url ,map , data );
	}

	public void searchSPUserAPI(Map<String, String> data) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse"))
		{
		map.put("tenantId", getProperty("clientID"));
		}else
		{
			map.put("tenantId", data.get("tenantId"));
		}
		methodGET(APIUri.SEARCH_USER, map , data);	
	}

	public void minimulUserAPI(Map<String, String> data) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("orgId", getProperty("clientID"));
		}
		else {
			map.put("orgId",data.get("tenantId"));
		}
		methodGET(APIUri.GET_MINIMAL_USER,map,data);
	}
	public void updateUserAPI(Map<String,String> data) throws Exception{
		Map<String,String>map= new HashMap<String,String>();
		if(data.get("orgId").equalsIgnoreCase("FromResponse")) {
			map.put("orgId",getProperty("clientID"));
		}else {
			map.put("orgId", data.get("orgId"));
		}
		if(data.get("userId").equalsIgnoreCase("FromResponse")) {
			map.put("userId", getProperty("userId"));
		}else {
			map.put("userId", data.get("userId"));
			}
		methodPOST(APIUri.UPDATE_USER,data.get("FileName"),map);
		
		
	}
}
