package com.itom.tenantsmain;

import java.util.HashMap;
import java.util.Map;

import com.itom.api.APIUtil;
import com.itom.rest.APIUri;

import io.restassured.response.Response;

public class PartnerAPIClass extends APIUtil{
	
	public void createPartnerAPI(Map<String,String> data) throws Exception {
		Map<String,Response> responses = methodPOST(APIUri.CREATE_PARTNER,data.get("FileName"),null);
		if(responses.get("Create Partner Test case 1: CREATE_Partner_Default values").getStatusCode()==200) {
			System.out.println("mspid : "+responses.get("Create Partner Test case 1: CREATE_Partner_Default values").jsonPath().getString("uniqueId"));
			saveProperty("mspID", responses.get("Create Partner Test case 1: CREATE_Partner_Default values").jsonPath().getString("uniqueId"));
		}
		System.out.println(getProperty("mspID"));
		}
		
	
	public void getPartner(Map<String,String> data) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		System.out.println("tenantidget : "+data.get("tenantId"));
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			System.out.println("set");
			System.out.println(getProperty("mspID"));
			map.put("tenantId",getProperty("mspID"));
		}
		else {
			System.out.println("set2");
			map.put("tenantId",data.get("tenantId"));
		}
		methodGET(APIUri.GET_PARTNER,map,data);
	}

	public void TerminatePartner(Map<String, String> data) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		if(data.get("id").equalsIgnoreCase("FromResponse")) {
			map.put("id",getProperty("mspID"));
		}
		else {
			map.put("id",data.get("id"));
		}
		map.put("action",data.get("action"));
		methodGET(APIUri.IN_ACTIVE_PARTNER,map,data);
	}


	public void SuspendPartner(Map<String, String> data) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		if(data.get("id").equalsIgnoreCase("FromResponse")) {
			map.put("id",getProperty("mspID"));
		}
		else {
			map.put("id",data.get("id"));
		}
		map.put("action",data.get("action"));
		methodGET(APIUri.IN_ACTIVE_PARTNER,map,data);
		
	}


	public void ActivatePartner(Map<String, String> data) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		if(data.get("id").equalsIgnoreCase("FromResponse")) {
			map.put("id",getProperty("mspID"));
		}
		else {
			map.put("id",data.get("id"));
		}
		map.put("action",data.get("action"));
		methodGET(APIUri.IN_ACTIVE_PARTNER,map,data);
	}


	public void SearchPartner(Map<String, String> data) throws Exception {
	    String url = searchFilter(APIUri.SEARCH_PARTNER,data);
		methodGET(url,null,data);
	}


	public void UpdatePartner(Map<String, String> data) throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		if(data.get("partnerId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("mspID"));
		}
		else {
			map.put("tenantId", data.get("partnerId"));
		}
		methodPOST(APIUri.UPDATE_PARTNER, data.get("FileName"), map);
	}
	

}
