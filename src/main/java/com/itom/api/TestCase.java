package com.itom.api;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


public class TestCase {
	
	String testCase;
	String description;
	JsonObject jsonObject;
	List<JsonObject> jsonObjects;
	private String module;
	String ExpectedStatusCode;
	String ExpectedMessage;
	JsonArray jsonarray;
	
	public JsonArray getjsonarray() {
		return jsonarray;
	}
	
	public void setjsonarray(JsonArray array) {
		this.jsonarray=array;
	}
	
	
	public String getExpectedMessage() {
		return ExpectedMessage;
	}
	
	public String getExpectedStatusCode() {
		return ExpectedStatusCode;
	}
	
	public String getTestCase() {
		return testCase;
	}
	
	public void setExpectedMessage(String message) {
		this.ExpectedMessage=message;
	}
	
	public void setExpectedStatusCode(String statuscode) {
		this.ExpectedStatusCode=statuscode;
	}
	public void setTestCase(String testCase) {
		this.testCase = testCase;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getResponse() {
		return module;
	}
	public JsonObject getJsonObject() {
		return jsonObject;
	}
	public void setJsonObject(JsonObject jsonObject) {
		this.jsonObject = jsonObject;
	}
	public List<JsonObject> getJsonObjects() {
		return jsonObjects;
	}
	public void setJsonObjects(List<JsonObject> jsonObjects) {
		this.jsonObjects = jsonObjects;
	}
}