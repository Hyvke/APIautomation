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
public class ClientAPIClass extends APIUtil{
	
	public void createClientAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenantId", getProperty("mspID"));
		Map<String, Response> response = methodPOST(APIUri.CREATE_CLIENT, data.get("FileName"), map);
		saveProperty("clientID", response.get("Create Client Test case 1: Create_CLIENT_Default values").jsonPath().getString("uniqueId"));
	}
	
	public void activateordeactivateClientAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("varId", getProperty("mspID"));
		if(data.get("clientId").equalsIgnoreCase("FromResponse")){
			map.put("clientId", getProperty("clientID"));
		}else {
			map.put("clientId", data.get("clientId"));
		}
			
		map.put("action", data.get("action"));
		methodPOST(APIUri.ACTIVATE_OR_DEACTIVATE_CLIENT ,map, data);
	}
	
	public void getClientAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("varId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("mspID"));
		} else {
			map.put("tenantId", data.get("varId"));
		}
		if(data.get("clientId").equalsIgnoreCase("FromResponse")) {
			map.put("clientId", getProperty("clientID"));
		} else {
			map.put("clientId", data.get("clientId"));
		}
		methodGET(APIUri.GET_CLIENT, map , data);
	}
	
	public void getMinimalClientsAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("varId").equalsIgnoreCase("FromResponse")) {
			map.put("varId", getProperty("mspID"));
		} else {
			map.put("varId", data.get("varId"));
		}	
		methodGET(APIUri.GET_MINIMAL_CLIENTS, map , data);
	}

	
	public void searchClientAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("mspID"));
		}else {
			map.put("tenantId", data.get("tenantId"));
		}
		String url = searchFilter(APIUri.SEARCH_CLIENT,data);
		methodGET(url ,map , data );
	}
	
	public void updateClientAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("varId").equalsIgnoreCase("FromResponse")) {
			map.put("varId", getProperty("mspID"));
		}else {
			map.put("varId", data.get("varId"));
		}
		if(data.get("clientId").equalsIgnoreCase("FromResponse")) {
			map.put("clientId", getProperty("clientID"));
		}else {
			map.put("clientId", data.get("clientId"));
		}
		
		methodPOST(APIUri.UPDATE_CLIENT , data.get("FileName") ,map);
	}
		

}
