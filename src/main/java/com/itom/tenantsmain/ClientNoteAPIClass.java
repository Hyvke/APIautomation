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
public class ClientNoteAPIClass extends APIUtil{
	
	public void createClientNoteAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("clientId", getProperty("clientID"));
		Map<String, Response> response = methodPOST(APIUri.CREATE_NOTE, data.get("FileName"), map);
		if(response.get("Create Client Note Test case 1: Create Client Note with Validity Never Expires").getStatusCode()==200) {
			System.out.print("clientNotes id"+response.get("Create Client Note Test case 1: Create Client Note with Validity Never Expires").jsonPath().getString("id"));
			saveProperty("clientNoteID", response.get("Create Client Note Test case 1: Create Client Note with Validity Never Expires").jsonPath().getString("id"));
		}
		System.out.println(getProperty("clientNoteID"));
	}
	
	public void deleteClientNoteAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("tenantId").equalsIgnoreCase("FromResponse")) {
			map.put("tenantId", getProperty("clientID"));
		}else {
			map.put("tenantId", data.get("tenantId"));
		}
		if(data.get("noteId").equalsIgnoreCase("FromResponse")) {
			map.put("id", getProperty("clientNoteID"));
		}else {
			map.put("id", data.get("noteId"));
		}
		DELETE(APIUri.DELETE_NOTE, data, map);
	}
	
	public void getClientNoteDetailsAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("clientId").equalsIgnoreCase("FromResponse")) {
			map.put("clientId", getProperty("clientID"));
		}else {
			map.put("clientId", data.get("clientId"));
		}
		if(data.get("noteId").equalsIgnoreCase("FromResponse")) {
			map.put("noteId", getProperty("clientNoteID"));
		}else {
			map.put("noteId", data.get("noteId"));
		}
		methodGET(APIUri.GET_NOTE, map, data);
	}
	
	public void searchClientNotesAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("clientId").equalsIgnoreCase("FromResponse")) {
			map.put("clientId", getProperty("clientID"));
		}else {
			map.put("clientId", data.get("clientId"));
		}
		//methodGET(APIUri.SEARCH_NOTES, map, data);
		String url = searchFilter(APIUri.SEARCH_NOTES,data);
		methodGET(url ,map , data );
	}
	
	public void updateClientNoteAPI(Map<String, String> data) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		if(data.get("clientId").equalsIgnoreCase("FromResponse")) {
			map.put("clientId", getProperty("clientID"));
		}else {
			map.put("clientId", data.get("clientId"));
		}
		if(data.get("noteId").equalsIgnoreCase("FromResponse")) {
			map.put("id", getProperty("clientNoteID"));
		}else {
			map.put("id", data.get("noteId"));
		}
		methodPOST(APIUri.UPDATE_NOTE, data.get("FileName"), map);
	}

}
