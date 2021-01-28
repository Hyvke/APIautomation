package com.itom.api;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import java.util.Set;
import java.util.TimeZone;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.jmeter.gui.SavePropertyDialog;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.itom.base.BaseClass;
import com.itom.reports.ExtentManager;
import com.itom.reports.ExtentTestManager;
import com.itom.rest.APIUri;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import io.restassured.response.Response;
import ucar.nc2.stream.NcStreamProto.Data;


public class APIUtil extends APIPage{
	
	private static String urlmethod = "";
	private static String testdata = "";
	private static String Error="";
	
	public static Response methodPOST(String url,Map<String,String> map, Map<String,String> data) throws Exception{
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		ExtentTestManager.startTest(data.get("TestCaseName"));
		testdata="";urlmethod = "URL : "+url + "<br>Method : POST ";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
		}
		Response response = post(url, null,map);
		System.out.println("Response : "+response.statusCode());
		ExtentTestManager.info("Expected status code : "+data.get("ExpectedStatusCode")+" and Expected Message : "+data.get("ExpectedMessage"));
		if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))) {
			String[] fieldlist = data.get("ExpectedMessage").split(",");
			checkAttributes(response,fieldlist);
		}
		else {
			if((response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))&&response.asString().toLowerCase().contains(data.get("ExpectedMessage").toLowerCase().trim()))) {
				
				ExtentTestManager.passResults(data.get("TestCaseName")+" is passed", "ResponseCode is "+response.getStatusCode());
			}
			else {
				ExtentTestManager.failResults(data.get("TestCaseName")+" is failed","ResponseCode is "+response.getStatusCode());
				checkFailure(response.asString());
			}
		}
		ExtentTestManager.endTest();
		 return response;
	}

	private static void checkAttributes(Response response,String[] attrlist) throws Exception {
		
		String res = response.getBody().asString();
		ExtentTestManager.info("ResponseBody = "+res);
		if(res.isBlank()||res.isEmpty()) {
			ExtentTestManager.passResults("TestCaseis Passed", "");
			return ;
		}
		Object obj = (Object)new JSONParser().parse(res); 
		System.out.println(obj.getClass());
		if(obj==null||obj.equals("")) {
			ExtentTestManager.failResults("response is null"+response.asString(),"");
			writeUsingFiles(ExtentTestManager.getTest().getModel().getName(),urlmethod,testdata,"Response is empty","genuine.txt");
		}
		
		if(obj instanceof JSONArray) {
			System.out.println("jsonarray-------");
			JSONArray arr = (JSONArray)obj;
			if(arr.size()==0&&(attrlist[0].equalsIgnoreCase("none")||attrlist[0].equalsIgnoreCase("nothing")||attrlist[0].equalsIgnoreCase("nthg")||attrlist[0].equalsIgnoreCase("ntng")||attrlist[0].isEmpty()))
			{
				ExtentTestManager.passResults("TestCase is passed", "");
				return ;
			}
				for(int a=0;a<arr.size();a++) {
				for(int c=0;c<attrlist.length;c++) {
					if(attrlist[c].equalsIgnoreCase("none")||attrlist[c].equalsIgnoreCase("nothing")||attrlist[0].equalsIgnoreCase("nthg")||attrlist[c].equalsIgnoreCase("ntng")||attrlist[c].isEmpty()) {
						ExtentTestManager.passResults("testcase is passed","");
						return ;
					}
					String str = getStringFromResponse((JSONObject)arr.get(0),attrlist[c]);
					if (str.isEmpty()||str.equals("")||str.equals(" ")) {
			        	ExtentTestManager.failResults(attrlist[c]+" : "+str,"");
			        	writeUsingFiles(ExtentTestManager.getTest().getModel().getName(),urlmethod,testdata,attrlist[c]+" field is empty","genuine.txt");
			        }
				}
			}
		}
		else if(obj instanceof JSONObject) {
			System.out.println("JSONObject----------");
			for(int c=0;c<attrlist.length;c++) {
				if(attrlist[c].equalsIgnoreCase("none")||attrlist[0].equalsIgnoreCase("nthg")||attrlist[c].equalsIgnoreCase("nothing")||attrlist[c].equalsIgnoreCase("ntng")||attrlist[c].isEmpty()) {
					ExtentTestManager.passResults("testcase is passed","");
					return ;
				}
				String str=getStringFromResponse((JSONObject)obj,attrlist[c]);
				
				if (str.isEmpty()||str.equals("")||str.equals(" ")) {
					if(!attrlist[c].equalsIgnoreCase("nothing")||!attrlist[0].equalsIgnoreCase("nthg")||!attrlist[c].equalsIgnoreCase("ntng")||!attrlist[c].isEmpty())
		        	ExtentTestManager.failResults(attrlist[c]+" : "+str,"");
					writeUsingFiles(ExtentTestManager.getTest().getModel().getName(),urlmethod,testdata,attrlist[c]+" field is empty","genuine.txt");
		        }
				else {
					ExtentTestManager.passResults("testcase is passed","");
				}
			}
		}
	}

	private static String getStringFromResponse(JSONObject jObj,String findkey) {
		 String finalValue = "";
		    if (jObj == null) {
		    	ExtentTestManager.failResults("Response is null", "");
		        return "";
		    }
		    
		    Iterator<String> keyItr = jObj.keySet().iterator();
		    Map<String, String> map = new HashMap<>();

		    while(keyItr.hasNext()) {
		        String key = keyItr.next();
		        map.put(key, jObj.get(key).toString());
		       /* if (jObj.get(key).toString().isBlank()||jObj.get(key).toString().equals("")||jObj.get(key).toString().equals(" ")) {
		        	ExtentTestManager.info(key+" : "+jObj.get(key).toString());
		        }*/
		    }

		    for (Map.Entry<String, String> e : (map).entrySet()) {
		        String key = e.getKey();
		        if (key.equalsIgnoreCase(findkey)) {
		            return jObj.get(key).toString();
		        }
		        

		        // read value
		        Object value = jObj.get(key);
		        if(value instanceof JSONArray) {
		        	JSONArray array = (JSONArray)value;
		        	for(int i=0;i<array.size();i++) {
		        		getStringFromResponse((JSONObject)array.get(i),findkey);
		        	}
		        }

		        if (value instanceof JSONObject) {
		            finalValue = getStringFromResponse((JSONObject)value,findkey);
		        }
		    }

		    // key is not found
		    return finalValue;
		}

private static void checkFields(JSONObject jObj) {
		 String finalValue = "";
		    if (jObj == null) {
		    	ExtentTestManager.failResults("Response is null", "");
		        return ;
		    }
		    
		    Iterator<String> keyItr = jObj.keySet().iterator();
		    Map<String, String> map = new HashMap<>();

		    while(keyItr.hasNext()) {
		        String key = keyItr.next();
		        map.put(key, jObj.get(key).toString());
		        if (jObj.get(key).toString().isBlank()||jObj.get(key).toString().equals("")||jObj.get(key).toString().equals(" ")) {
		        	ExtentTestManager.warning(key+" : "+jObj.get(key).toString());
		        }
		        // read value
		        Object value = jObj.get(key);
		        if(value instanceof JSONArray) {
		        	JSONArray array = (JSONArray)value;
		        	for(int i=0;i<array.size();i++) {
		        		checkFields((JSONObject)array.get(i));
		        	}
		        }

		        if (value instanceof JSONObject) {
		            checkFields((JSONObject)value);
		        }
		    }
		}

	public Map<String, Response> methodPOST(String url,String jsonFName,Map<String,String> map) throws Exception 
	{
		Response response;
		Map<String,Response> responses = new HashMap<String,Response>();
		List<TestCase> listOfTestCase = getPayloads(jsonFName);
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		for(TestCase t : listOfTestCase) 
		{
			ExtentTestManager.startTest(t.getTestCase());
			System.out.println(t.getTestCase());
			String jsonPayload = null;
			if(t.getJsonObject() != null) 
			{
				jsonPayload = replaceVariables(t.getJsonObject().toString());
			} 
			else if(t.getJsonObjects() != null) 
			{
				jsonPayload = t.getJsonObjects().toString();
			}
			else if(t.getjsonarray()!=null) {
				jsonPayload = replaceVariables(t.getjsonarray().toString());
			}
			else {
				jsonPayload="null";
			}
			//System.out.println(jsonPayload);
			//RestAPIUtil.getInstance().invokePOST(URI, jsonPayload);
			testdata="";testdata="";urlmethod = "URL : "+url + "<br>Method : POST ";
			if(map!=null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			}
			else {
				testdata=testdata+"No inputs";
			}
			response = post(url, jsonPayload, map);
			System.out.println("response code :"+response.getStatusCode());
			System.out.println("payload status :"+t.getExpectedStatusCode());
			ExtentTestManager.info("Expected status code : "+t.getExpectedStatusCode()+" and Expected Message : "+t.getExpectedMessage());
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())) {
				String[] fieldlist = t.getExpectedMessage().split(",");
				checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())&&response.asString().toLowerCase().contains(t.getExpectedMessage().toLowerCase().trim()))) {
					ExtentTestManager.passResults(t.testCase+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(t.testCase+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(t.testCase,response);
			ExtentTestManager.endTest();
		}
		return responses;
	}
	
	public static SimpleDateFormat getFormatter(String format) {
		return new SimpleDateFormat(format);
	}
	
	public  static String replaceVariables(String element) throws Exception{
		Calendar calendar = Calendar.getInstance();
		getFormatter("yyyy-MM-dd'T'HH:mm:ss z").setTimeZone(TimeZone.getTimeZone("GMT"));
		System.out.println(element.toString());
		System.out.println("testing : "+(element.contains("<RANDOM>")));
		if(element.contains("<RANDOM>")) {
		String randstr = getRandomString();
			element = element.replaceAll("<RANDOM>", randstr);
			System.out.println("payload :"+element);
			ExtentTestManager.info("replaced Random String is : "+ randstr);
		}
		if(element.contains("<DOMAIN>")) {
			element = element.replaceAll("<DOMAIN>", (String)BaseClass.getConstant("domain"));
			System.out.println("payload :"+element);
		}
		if(element.contains("<ACCESSPOINT>")) {
			element = element.replaceAll("<ACCESSPOINT>", getRandomString()+(String)BaseClass.getConstant("Accesspoint"));
			System.out.println("payload :"+element);
		}
		if(element.contains("<CHANNEL>")) {
			element = element.replaceAll("<CHANNEL>", (String)BaseClass.getConstant("channel"));
			System.out.println("payload :"+element);
		}
		if(element.contains("<RANDOM2>")) {
			element = element.replaceAll("<RANDOM2>", getRandomString(2));
			System.out.println("payload :"+element);
		}
		if(element.contains("<RANDOMNUMBER>")) {
			element = element.replaceAll("<RANDOMNUMBER>", ""+randomRange(111111,999999));
			System.out.println("payload :"+element);
		}
		if(element.contains("<USERID>")) {
			element = element.replaceAll("<USERID>",""+getProperty("userId"));
		}
		if(element.contains("<USERLOGINNAME>")) {
			element = element.replaceAll("<USERLOGINNAME>",""+getProperty("userLoginName"));
		}
		if(element.contains("<ROLEID>")) {
			element = element.replaceAll("<ROLEID>",""+getProperty("roleID"));
		}
		if(element.contains("<ROLENAME>")) {
			element = element.replaceAll("<ROLENAME>",""+getProperty("roleName"));
		}
		if(element.contains("<USERGROUPID>")) {
			element = element.replaceAll("<USERGROUPID>",""+getProperty("userGroupID"));
		}
		if(element.contains("<USERGROUPNAME>")) {
			element = element.replaceAll("<USERGROUPNAME>",""+getProperty("userGroupName"));
		}
		if(element.contains("<MSPID>")) {
			element = element.replaceAll("<MSPID>",""+getProperty("mspID"));
		}
		if(element.contains("<CLIENTID>")) {
			element = element.replaceAll("<CLIENTID>",""+getProperty("clientID"));
		}
		if(element.contains("<RESOURCEID>")) {
			element = element.replaceAll("<RESOURCEID>",""+getProperty("resourceID"));
		}
		if(element.contains("<RESOURCEID1>")) {
			element = element.replaceAll("<RESOURCEID1>",""+getProperty("resourceID1"));
		}
		if(element.contains("<DEVICEID>")) {
			element = element.replaceAll("<DEVICEID>",""+getProperty("deviceID"));
		}
		if(element.contains("<DEVICEID1>")) {
			element = element.replaceAll("<DEVICEID1>",""+getProperty("deviceID1"));
		}
		if(element.contains("<CHILDSERVICEGROUPID>")) {
			element = element.replaceAll("<CHILDSERVICEGROUPID>",""+getProperty("ChildServiceGroupID"));
		}
		if(element.contains("<SERVICEGROUPID>")) {
			element = element.replaceAll("<SERVICEGROUPID>",""+getProperty("ServiceGroupID"));
		}
		if(element.contains("<FOLDERID>")) {
			element = element.replaceAll("<FOLDERID>",""+getProperty("folderID"));
		}
		if(element.contains("<INCIDENT_BUSINESSIMPACT_ID>")) {
			element = element.replaceAll("<INCIDENT_BUSINESSIMPACT_ID>", ""+getProperty("incidentBusinessImpactID"));
		}
		if(element.contains("<INCIDENT_URGENCY_ID>")) {
			element = element.replaceAll("<INCIDENT_URGENCY_ID>", ""+getProperty("incidentUrgencyID"));
		}
		if(element.contains("<KBCATEGORYID>")) {
			element = element.replaceAll("<KBCATEGORYID>", ""+getProperty("KB_CategoryID"));
		}
		if(element.contains("<KBCATEGORYNAME>")) {
			element = element.replaceAll("<KBCATEGORYNAME>", ""+getProperty("KB_CategoryName"));
		}
		if(element.contains("<INSTALLED_INTEGRATIONID>")) {
			element = element.replaceAll("<INSTALLED_INTEGRATIONID>", ""+getProperty("installedIntegrationID"));
		}
		if(element.contains("<PATCH_ID>")) {
			element = element.replaceAll("<PATCH_ID>", ""+getProperty("patchID"));
		}
		if(element.contains("<PATCH_NAME>")) {
			element = element.replaceAll("<PATCH_ID>", ""+getProperty("patchName"));
		}
		if(element.contains("<JOBID>")) {
			element = element.replaceAll("<JOBID>", ""+getProperty("jobID"));
		}
		if(element.contains("<PATCH_CONFIG_ID>")) {
			element = element.replaceAll("<PATCH_CONFIG_ID>", ""+getProperty("patchConfigID"));
		}
		if(element.contains("<INTEGRATION_EVENT_ID>")) {
			element = element.replaceAll("<INTEGRATION_EVENT_ID>", ""+getProperty("integrationEventID"));
		}
		if(element.contains("<TODAY_DATE>")) {
			Date today = calendar.getTime();
			element = element.replaceAll("<TODAY_DATE>", ""+getFormatter("yyyy-MM-dd'T'HH:mm:ss z").format(today));
		}
		if(element.contains("<CURRENT_TIME>")) {
			Date today = calendar.getTime();
			element = element.replaceAll("<CURRENT_TIME>", ""+getFormatter("HH:mm:ss").format(today));
		}
		if(element.contains("<AFTER_15_MINUTES>")) {
			Date todayDate = calendar.getTime();
			calendar.setTime(todayDate);
			calendar.add(Calendar.MINUTE, 15);
			Date today_time = calendar.getTime();
			element = element.replaceAll("<AFTER_15_MINUTES>", ""+getFormatter("HH:mm:ss").format(today_time));
		}
		if(element.contains("<AFTER_10_MIN_TIME>")) {
			Date todayDate = calendar.getTime();
			calendar.setTime(todayDate);
			calendar.add(Calendar.MINUTE, 10);
			Date today_time = calendar.getTime();
			element = element.replaceAll("<AFTER_10_MIN_TIME>", ""+getFormatter("yyyy-MM-dd'T'HH:mm:ss z").format(today_time));
		}
		if(element.contains("<TOMORROW_DATE>")) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			Date tommorrow = calendar.getTime();
			element = element.replaceAll("<TOMORROW_DATE>", ""+getFormatter("yyyy-MM-dd'T'HH:mm:ss z").format(tommorrow));
		}
		
		if(element.contains("<AFTER_ONE_WEEK_DATE>")) {
			calendar.add(Calendar.DAY_OF_YEAR, 7);
			Date tommorrow = calendar.getTime();
			element = element.replaceAll("<AFTER_ONE_WEEK_DATE>", ""+getFormatter("yyyy-MM-dd'T'HH:mm:ss z").format(tommorrow));
		}
		if(element.contains("<ONE_WEEK_BACK_DATE>")) {
			calendar.add(Calendar.DAY_OF_YEAR, -7);
			Date one_week_back_date = calendar.getTime();
			element = element.replaceAll("<ONE_WEEK_BACK_DATE>", ""+getFormatter("yyyy-MM-dd'T'HH:mm:ss z").format(one_week_back_date));
		}
		if(element.contains("<DAY_AFTER_TOMORROW>")) {
			calendar.add(Calendar.DAY_OF_YEAR, 2);
			Date day_after_tommorrow = calendar.getTime();
			element = element.replaceAll("<AFTER_ONE_WEEK_DATE>", ""+getFormatter("yyyy-MM-dd'T'HH:mm:ss z").format(day_after_tommorrow));
		}
		if(element.contains("<YESTERDAY_DATE>")) {
			calendar.add(Calendar.DAY_OF_YEAR, -1);
			Date day_after_tommorrow = calendar.getTime();
			element = element.replaceAll("<YESTERDAY_DATE>", ""+getFormatter("yyyy-MM-dd'T'HH:mm:ss 0000").format(day_after_tommorrow));
		}
		if(element.contains("<CURRENT_DATE>")) {
			Date day_after_tommorrow = calendar.getTime();
			element = element.replaceAll("<CURRENT_DATE>", ""+getFormatter("yyyy-MM-dd'T'HH:mm:ss 0000").format(day_after_tommorrow));
		}
		
		if(element.contains("<SHIFTID>")) {
			element = element.replaceAll("<SHIFTID>", ""+getProperty("ShiftID"));
		}
		if(element.contains("<DEVICEGROUPID>")) {
			element = element.replaceAll("<DEVICEGROUPID>",""+getProperty("deviceGroupID"));
			}
		if(element.contains("<SR_CATEGORY_ID>")) {
			element = element.replaceAll("<SR_CATEGORY_ID>",""+getProperty("SR_CategoryID"));
		}
		if(element.contains("<SR_CATEGORY_NAME>")) {
			element = element.replaceAll("<SR_CATEGORY_NAME>",""+getProperty("SR_Category_Name"));
		}
		if(element.contains("<SR_STATUSCHANGE_REASON_NAME>")) {
			element = element.replaceAll("<SR_STATUSCHANGE_REASON_NAME>",""+getProperty("serviceDeskStatusChangeReason_Name"));
		}
		if(element.contains("<IN_CATEGORY_ID>")) {
			element = element.replaceAll("<IN_CATEGORY_ID>",""+getProperty("IN_CategoryID"));
		}
		if(element.contains("<PR_CATEGORY_ID>")) {
			element = element.replaceAll("<PR_CATEGORY_ID>",""+getProperty("PR_CatgoryID"));
		}
		if(element.contains("<CR_CATEGORY_ID>")) {
			element = element.replaceAll("<CR_CATEGORY_ID>",""+getProperty("CR_CategoryID"));
		}
		if(element.contains("<KB_ARTICLE_ID>")) {
			element = element.replaceAll("<KB_ARTICLE_ID>",""+getProperty("articleID"));
		}
		if(element.contains("<LONGARTICLEID>")) {
			element = element.replaceAll("<LONGARTICLEID>",""+getProperty("longArticleId"));
		}
		if(element.contains("<ROSTERID>")) {
			element = element.replaceAll("<ROSTERID>",""+getProperty("RosterID"));
		}
		if(element.contains("<NULL>")) {
			element = element.replaceAll("<NULL>",""+getProperty("khkjhlkj"));
		}
		if(element.contains("<IP>")) {
			while(element.contains("<IP>")) {
				element = element.replaceFirst("<IP>", ""+getIP());
			}
			
		}
		if(element.contains("<SCIMUSERID>")) {
			element = element.replaceAll("<SCIMUSERID>",""+getProperty("scimUserId"));
		}
		if(element.contains("<MANAGEMENTPROFILENAME>")) {
			element = element.replaceAll("<MANAGEMENTPROFILENAME>",""+getProperty("managementProfileName"));
		}
		if(element.contains("<CLIENTNOTEID>")) {
			element = element.replaceAll("<CLIENTNOTEID>",""+getProperty("clientNoteID"));
		}
		if(element.contains("<SITEID>")) {
			element = element.replaceAll("<SITEID>",""+getProperty("siteID"));
		}
		System.out.println("payload :"+element);
		return element;
	}
	
	public static int getIP() {
		return randomRange(1,255);
	}
	
	public static String getRandomString() {
		String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String string = ""+ alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51));
		return string;
	}
	
	public static String getRandomString(int number) {
		String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890!@#$%^&*()_+<>?:{}|[];',./\\";
		String string = ""+ alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51));
		return string;
	}

	public static Map<String, Response> methodGET(String url,Map<String,String> map,Map<String,String> data) throws Exception 
	{		Map<String,Response> responses = new HashMap<String,Response>();
			//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
			System.out.println("tstcasename : "+data.get("TestCaseName"));
			ExtentTestManager.startTest(data.get("TestCaseName"));
			testdata="";urlmethod = "URL : "+url + "<br>Method : GET ";
			if(map != null) {
				for (Map.Entry<String, String> entry : map.entrySet()) {
					testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
				}
			}
			
			Response response = get(url, map);
			ExtentTestManager.info("Response Code : "+response.getStatusCode()+", response : "+response.asString());
			System.out.println("Response : "+response.asString());
			ExtentTestManager.info("ExpectedStatusCode : "+ data.get("ExpectedStatusCode")+", Expected response contains : "+data.get("ExpectedMessage"));
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))) {
				String[] fieldlist = data.get("ExpectedMessage").split(",");
				checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))&&response.asString().toLowerCase().contains(data.get("ExpectedMessage").toLowerCase().trim()))) {
						ExtentTestManager.passResults(data.get("TestCaseName")+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(data.get("TestCaseName")+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(data.get("TestCaseName"),response);
			ExtentTestManager.endTest();
			 return responses;
	}
	
	public static Map<String, Response> methodGETforFileDownload(String url,Map<String,String> map,Map<String,String> data) throws Exception 
	{		Map<String,Response> responses = new HashMap<String,Response>();
			//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
			System.out.println("tstcasename : "+data.get("TestCaseName"));
			ExtentTestManager.startTest(data.get("TestCaseName"));
			testdata="";urlmethod = "URL : "+url + "<br>Method : GET ";
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			Response response = getforFileDownload(url, map);
			ExtentTestManager.info("Response Code : "+response.getStatusCode());
			//System.out.println("Response : "+response.asString());
			ExtentTestManager.info("ExpectedStatusCode : "+ data.get("ExpectedStatusCode")+", Expected response contains : "+data.get("ExpectedMessage"));
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))) {
				String[] fieldlist = data.get("ExpectedMessage").split(",");
				//checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))&&response.asString().toLowerCase().contains(data.get("ExpectedMessage").toLowerCase().trim()))) {
						ExtentTestManager.passResults(data.get("TestCaseName")+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(data.get("TestCaseName")+" is failed","ResponseCode is "+response.getStatusCode());
					ExtentTestManager.failResults("Response message is :", response.asString());
					//checkFailure(response.asString());
				}
			}
			responses.put(data.get("TestCaseName"),response);
			ExtentTestManager.endTest();
			 return responses;
	}
	
	public Response DELETE(String url, Map<String, String> data, Map<String, String> map) throws Exception {
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		System.out.println("tstcasename : "+data.get("TestCaseName"));
		ExtentTestManager.startTest(data.get("TestCaseName"));
		testdata="";urlmethod = "URL : "+url + "<br>Method : DELETE ";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
		}
		Response response = delete(url,null, map);
		ExtentTestManager.info("Response Code : "+response.getStatusCode()+", response : "+response.asString());
		System.out.println("Response : "+response.asString());
		ExtentTestManager.info("ExpectedStatusCode : "+ data.get("ExpectedStatusCode")+", Expected response contains : "+data.get("ExpectedMessage"));
		if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))) {
			String[] fieldlist = data.get("ExpectedMessage").split(",");
			checkAttributes(response,fieldlist);
		}
		else {
			if((response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))&&response.asString().toLowerCase().contains(data.get("ExpectedMessage").toLowerCase().trim()))) {
					ExtentTestManager.passResults(data.get("TestCaseName")+" is passed", "ResponseCode is "+response.getStatusCode());
			}
			else {
				ExtentTestManager.failResults(data.get("TestCaseName")+" is failed","ResponseCode is "+response.getStatusCode());
				checkFailure(response.asString());
			}
		}
		ExtentTestManager.endTest();
		 return response;

	}
	
	
	public Response methodDELETE(String url, String jsonFname, Map<String, String> map) throws Exception{
		String jsonPayload = null;
		Response response;
		Map<String,Response> responses = new HashMap<String,Response>();
		List<TestCase> listOfTestCase = getPayloads(jsonFname);
		
		for(TestCase t:listOfTestCase) {
		if(t.getJsonObject() != null) 
		{
			jsonPayload = replaceVariables(t.getJsonObject().toString());
		} 
		else if(t.getJsonObjects() != null) 
		{
			jsonPayload = t.getJsonObjects().toString();
		}
		else if(t.getjsonarray()!=null) {
			jsonPayload = replaceVariables(t.getjsonarray().toString());
		}
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		//System.out.println("tstcasename : "+data.get("TestCaseName"));
		ExtentTestManager.startTest(t.getTestCase());
		testdata="";urlmethod = "URL : "+url + "<br>Method : DELETE ";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
		}
		response = delete(url,jsonPayload, map);
		ExtentTestManager.info("Response Code : "+response.getStatusCode()+", response : "+response.asString());
		System.out.println("Response : "+response.asString());
		ExtentTestManager.info("ExpectedStatusCode :"+ t.getExpectedStatusCode()+ "and Expected response contains : "+t.getExpectedMessage());
		if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())) {
			String[] fieldlist = t.getExpectedMessage().split(",");
			checkAttributes(response,fieldlist);
		}
		else {
			if((response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())&&response.asString().toLowerCase().contains(t.getExpectedMessage().toLowerCase().trim()))) {
					ExtentTestManager.passResults(t.testCase+" is passed", "ResponseCode is "+response.getStatusCode());
			}
			else {
				ExtentTestManager.failResults(t.testCase+" is failed","ResponseCode is "+response.getStatusCode());
				checkFailure(response.asString());
			}
		}
		responses.put(t.testCase,response);
		}
		
		ExtentTestManager.endTest();
		 return responses.get(0);
		
	}
	
	public String RandomNumber() {
		return ""+randomRange(12314, 14214);
	}
	
	
	
	public List<TestCase> getPayloads(String jsonFName) throws Exception{
		List<TestCase> listOfTestCase = new ArrayList<TestCase>();
		String fileName = System.getProperty("user.dir")+BaseClass.getConstant("jsonFilesPath")+"/"+jsonFName+".json";
		File file =	new File(fileName);
		Scanner sc =  new Scanner(file);
		String testCasePayload = sc.useDelimiter("\\Z").next();
		sc.close();
		JsonParser parser = new JsonParser();
		com.google.gson.JsonArray o = (com.google.gson.JsonArray)parser.parse(testCasePayload);
		Gson gson = new Gson();
		Iterator<JsonElement> keysItr = o.iterator();
		while(keysItr.hasNext()) {
			TestCase testCase = gson.fromJson(keysItr.next(), TestCase.class);
			listOfTestCase.add(testCase);
		}
		return listOfTestCase;
	}
	
	public static String getProperty(String key) throws Exception {
		FileReader reader=new FileReader(System.getProperty("user.dir")+"/src/test/resources/PropertiesFromResponse.properties");  
	    Properties p=new Properties();  
	    p.load(reader); 
	    if((String)p.get(key)==null) {
	    	if(checkInnotsaved(key)) {
	    		Error = ""+key+" variable not saved in "+BaseClass.getMethodName();
	    	}
	    }
	    return (String) p.get(key);
	}
	
	private static boolean checkInnotsaved(String key) {
		File file = new File(System.getProperty("user.dir")+"/src/test/resources/notsaved.txt"); 
	      try {
	      BufferedReader br = new BufferedReader(new FileReader(file)); 
	      
	      String st; 
	      while ((st = br.readLine()) != null) {
	        String[] list = st.split("\t\t");
	        System.out.println("size is : "+list.length);
	        if(list[0].equalsIgnoreCase(key)) {
	        	return true;
	        }
	      }}catch(Exception e) {
	    	  return false;
	      }
	      return false;
		
	}

	public void saveProperty(String key,String value) throws Exception {
		String file = System.getProperty("user.dir")+"/src/test/resources/PropertiesFromResponse.properties";
		
		 try (OutputStream output = new FileOutputStream(file,true)) {
	            Properties prop = new Properties();
	            // set the properties value
	            prop.setProperty(key, value);
	            // save properties to project root folder
	            prop.store(output, null);
	            System.out.println(prop);
	            BaseClass.context = prop;
	        } catch (Exception io) {
	        	ExtentTestManager.info("can't update property "+key+" : "+ value+" in Property file ->"+file);
	        	System.out.println("can't update properties "+io.getMessage());
	            io.printStackTrace();
	            writeNotSavedProperties(key,BaseClass.getMethodName());
	        }
	}
	
	public Map<String, Response> methodPUT(String url,String jsonFName,Map<String,String> map) throws Exception 
	{
		Response response;
		Map<String,Response> responses = new HashMap<String,Response>();
		List<TestCase> listOfTestCase = getPayloads(jsonFName);
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		for(TestCase t : listOfTestCase) 
		{
			ExtentTestManager.startTest(t.getTestCase());
			System.out.println(t.getTestCase());
			String jsonPayload = null;
			if(t.getJsonObject() != null) 
			{
				jsonPayload = replaceVariables(t.getJsonObject().toString());
			} 
			else if(t.getJsonObjects() != null) 
			{
				jsonPayload = t.getJsonObjects().toString();
			}
			else if(t.getjsonarray()!=null) {
				jsonPayload = replaceVariables(t.getjsonarray().toString());
			}
			//System.out.println(jsonPayload);
			//RestAPIUtil.getInstance().invokePOST(URI, jsonPayload);
			testdata="";urlmethod = "URL : "+url + "<br>Method : PUT ";
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			response = put(url, jsonPayload, map);
			System.out.println("response code :"+response.getStatusCode());
			System.out.println("payload status :"+t.getExpectedStatusCode());
			ExtentTestManager.info("Expected status code : "+t.getExpectedStatusCode()+" and Expected Message : "+t.getExpectedMessage());
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())) {
				String[] fieldlist = t.getExpectedMessage().split(",");
				checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())&&response.asString().toLowerCase().contains(t.getExpectedMessage().toLowerCase().trim()))) {
					ExtentTestManager.passResults(t.testCase+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(t.testCase+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(t.testCase,response);
			ExtentTestManager.endTest();
		}
		return responses;
	}
	
	public static Response PUT(String url,Map<String,String> map, Map<String,String> data) throws Exception{
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		ExtentTestManager.startTest(data.get("TestCaseName"));
		//Response response = post(url, null,map);
		testdata="";urlmethod = "URL : "+url + "<br>Method : PUT ";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
		}
		Response response = put(url, null, map);
		System.out.println("Response : "+response.statusCode());
		ExtentTestManager.info("Expected status code : "+data.get("ExpectedStatusCode")+" and Expected Message : "+data.get("ExpectedMessage"));
		if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))) {
			String[] fieldlist = data.get("ExpectedMessage").split(",");
			checkAttributes(response,fieldlist);
		}
		else {
			if((response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))&&response.asString().toLowerCase().contains(data.get("ExpectedMessage").toLowerCase().trim()))) {
				
				ExtentTestManager.passResults(data.get("TestCaseName")+" is passed", "ResponseCode is "+response.getStatusCode());
			}
			else {
				ExtentTestManager.failResults(data.get("TestCaseName")+" is failed","ResponseCode is "+response.getStatusCode());
				checkFailure(response.asString());
			}
		}
		ExtentTestManager.endTest();
		 return response;
	}
	
	public Map<String,Response> methodPOSTforWebHooks(String url,String jsonFName,Map<String,String> map) throws Exception{
		Response response;
		Map<String,Response> responses = new HashMap<String,Response>();
		List<TestCase> listOfTestCase = getPayloads(jsonFName);
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		for(TestCase t : listOfTestCase) 
		{
			ExtentTestManager.startTest(t.getTestCase());
			System.out.println(t.getTestCase());
			String jsonPayload = null;
			if(t.getJsonObject() != null) 
			{
				jsonPayload = replaceVariables(t.getJsonObject().toString());
			} 
			else if(t.getJsonObjects() != null) 
			{
				jsonPayload = t.getJsonObjects().toString();
			}
			else if(t.getjsonarray()!=null) {
				jsonPayload = replaceVariables(t.getjsonarray().toString());
			}
			//System.out.println(jsonPayload);
			//RestAPIUtil.getInstance().invokePOST(URI, jsonPayload);
			testdata="";urlmethod = "URL : "+url + "<br>Method : POST ";
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			response = postforwebhooks(url, jsonPayload, map);
			System.out.println("response code :"+response.getStatusCode());
			System.out.println("payload status :"+t.getExpectedStatusCode());
			ExtentTestManager.info("Expected status code : "+t.getExpectedStatusCode()+" and Expected Message : "+t.getExpectedMessage());
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())) {
				String[] fieldlist = t.getExpectedMessage().split(",");
				checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())&&response.asString().toLowerCase().contains(t.getExpectedMessage().toLowerCase().trim()))) {
					ExtentTestManager.passResults(t.testCase+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(t.testCase+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(t.testCase,response);
			ExtentTestManager.endTest();
		}
		return responses;
	}
	
	public static void FileTransferInSSH(Session session,String localFile,String remoteDir,String FilenameinremoteDir) throws Exception {
		ChannelSftp filetransferChannel = (ChannelSftp) session.openChannel("sftp");
		UploadFileUsingJsch(filetransferChannel,localFile,remoteDir,FilenameinremoteDir);	
        System.out.println("DONE");
	}
	
	public static Session setupJsch(String username,String password,String remoteHost) throws Exception {
		java.util.Properties config = new java.util.Properties(); 
		config.put("StrictHostKeyChecking", "no");
	    JSch jsch = new JSch();
	    Session jschSession = jsch.getSession(username, remoteHost);
	    jschSession.setPassword(password);
	    jschSession.setConfig(config);
	    jschSession.connect();
	    return jschSession;
	}
	
	public static String runCommandInssh(Session session,String command) {
		 String lastline="";
		try{
	    	Channel channel=session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        channel.setInputStream(null);
	        ((ChannelExec)channel).setErrStream(System.err);
	        
	        InputStream in=channel.getInputStream();
	        OutputStream out=channel.getOutputStream();
	        channel.connect();
	        
	        out.write(("y"+"\n").getBytes());
	        out.flush();
	        byte[] tmp=new byte[1024];
	        while(true){
	          while(in.available()>0){
	            int i=in.read(tmp, 0, 1024);
	            if(i<0)break;
	            lastline=new String(tmp, 0, i);
	            System.out.print(lastline);
	          }
	          if(channel.isClosed()){
	            System.out.println("exit-status: "+channel.getExitStatus());
	            break;
	          }
	          try{Thread.sleep(1000);}catch(Exception ee){}
	        }
	        System.out.println("DONE");
	        return lastline;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
		return lastline;

	}
	
	public static void UploadFileUsingJsch(ChannelSftp sftp,String localFile,String remoteDir, String filenameinremoteDir) throws Exception{
	    sftp.connect();
	    sftp.put(localFile, remoteDir + filenameinremoteDir);
	  
	    sftp.exit();
	}
	
	public static void disconnectSSH(Session session) {
		session.disconnect();
	}
	
	public Map<String, Response> integrationPOST(String url,String jsonFName,Map<String,String> map) throws Exception 
	{
		Response response;
		Map<String,Response> responses = new HashMap<String,Response>();
		List<TestCase> listOfTestCase = getPayloads(jsonFName);
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		for(TestCase t : listOfTestCase) 
		{
			ExtentTestManager.startTest(t.getTestCase());
			System.out.println(t.getTestCase());
			String jsonPayload = null;
			if(t.getJsonObject() != null) 
			{
				jsonPayload = replaceVariables(t.getJsonObject().toString());
			} 
			else if(t.getJsonObjects() != null) 
			{
				jsonPayload = t.getJsonObjects().toString();
			}
			else if(t.getjsonarray()!=null) {
				jsonPayload = replaceVariables(t.getjsonarray().toString());
			}
			//System.out.println(jsonPayload);
			//RestAPIUtil.getInstance().invokePOST(URI, jsonPayload);
			testdata="";urlmethod = "URL : "+url + "<br>Method : POST ";
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			response = postForIntegrations(url, jsonPayload, map);
			System.out.println("response code :"+response.getStatusCode());
			System.out.println("payload status :"+t.getExpectedStatusCode());
			ExtentTestManager.info("Expected status code : "+t.getExpectedStatusCode()+" and Expected Message : "+t.getExpectedMessage());
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())) {
				String[] fieldlist = t.getExpectedMessage().split(",");
				checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())&&response.asString().toLowerCase().contains(t.getExpectedMessage().toLowerCase().trim()))) {
					ExtentTestManager.passResults(t.testCase+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(t.testCase+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(t.testCase,response);
			ExtentTestManager.endTest();
		}
		return responses;
	}
	
	public static Map<String, Response> integrationsGET(String url,Map<String,String> map,Map<String,String> data) throws Exception 
	{		Map<String,Response> responses = new HashMap<String,Response>();
			//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
			System.out.println("tstcasename : "+data.get("TestCaseName"));
			ExtentTestManager.startTest(data.get("TestCaseName"));
			testdata="";urlmethod = "URL : "+url + "<br>Method : GET ";
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			Response response = getForIntegrations(url, map);
			ExtentTestManager.info("Response Code : "+response.getStatusCode()+", response : "+response.asString());
			System.out.println("Response : "+response.asString());
			ExtentTestManager.info("ExpectedStatusCode : "+ data.get("ExpectedStatusCode")+", Expected response contains : "+data.get("ExpectedMessage"));
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))) {
				String[] fieldlist = data.get("ExpectedMessage").split(",");
				checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))&&response.asString().toLowerCase().contains(data.get("ExpectedMessage").toLowerCase().trim()))) {
						ExtentTestManager.passResults(data.get("TestCaseName")+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(data.get("TestCaseName")+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(data.get("TestCaseName"),response);
			ExtentTestManager.endTest();
			 return responses;
	}
	
	public Response integrationDELETE(String url, Map<String, String> data, Map<String, String> map) throws Exception {
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		System.out.println("tstcasename : "+data.get("TestCaseName"));
		ExtentTestManager.startTest(data.get("TestCaseName"));
		testdata="";urlmethod = "URL : "+url + "<br>Method : DELETE ";
		for (Map.Entry<String, String> entry : map.entrySet()) {
			testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
		}
		Response response = deleteForIntegrations(url,null, map);
		ExtentTestManager.info("Response Code : "+response.getStatusCode()+", response : "+response.asString());
		System.out.println("Response : "+response.asString());
		ExtentTestManager.info("ExpectedStatusCode : "+ data.get("ExpectedStatusCode")+", Expected response contains : "+data.get("ExpectedMessage"));
		if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))) {
			String[] fieldlist = data.get("ExpectedMessage").split(",");
			checkAttributes(response,fieldlist);
		}
		else {
			if((response.getStatusCode()==Integer.parseInt(data.get("ExpectedStatusCode"))&&response.asString().toLowerCase().contains(data.get("ExpectedMessage").toLowerCase().trim()))) {
					ExtentTestManager.passResults(data.get("TestCaseName")+" is passed", "ResponseCode is "+response.getStatusCode());
			}
			else {
				ExtentTestManager.failResults(data.get("TestCaseName")+" is failed","ResponseCode is "+response.getStatusCode());
				checkFailure(response.asString());
			}
		}
		ExtentTestManager.endTest();
		 return response;

	}
	
	public Map<String, Response> integrationsPUT(String url,String jsonFName,Map<String,String> map) throws Exception 
	{
		Response response;
		Map<String,Response> responses = new HashMap<String,Response>();
		List<TestCase> listOfTestCase = getPayloads(jsonFName);
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		for(TestCase t : listOfTestCase) 
		{
			ExtentTestManager.startTest(t.getTestCase());
			System.out.println(t.getTestCase());
			String jsonPayload = null;
			if(t.getJsonObject() != null) 
			{
				jsonPayload = replaceVariables(t.getJsonObject().toString());
			} 
			else if(t.getJsonObjects() != null) 
			{
				jsonPayload = t.getJsonObjects().toString();
			}
			else if(t.getjsonarray()!=null) {
				jsonPayload = replaceVariables(t.getjsonarray().toString());
			}
			//System.out.println(jsonPayload);
			//RestAPIUtil.getInstance().invokePOST(URI, jsonPayload);
			testdata="";urlmethod = "URL : "+url + "<br>Method : PUT ";
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			response = putForIntegrations(url, jsonPayload, map);
			System.out.println("response code :"+response.getStatusCode());
			System.out.println("payload status :"+t.getExpectedStatusCode());
			ExtentTestManager.info("Expected status code : "+t.getExpectedStatusCode()+" and Expected Message : "+t.getExpectedMessage());
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())) {
				String[] fieldlist = t.getExpectedMessage().split(",");
				checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())&&response.asString().toLowerCase().contains(t.getExpectedMessage().toLowerCase().trim()))) {
					ExtentTestManager.passResults(t.testCase+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(t.testCase+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(t.testCase,response);
			ExtentTestManager.endTest();
		}
		return responses;
	}
	
	public Map<String, Response> integrationsPATCH(String url,String jsonFName,Map<String,String> map) throws Exception 
	{
		Response response;
		Map<String,Response> responses = new HashMap<String,Response>();
		List<TestCase> listOfTestCase = getPayloads(jsonFName);
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		for(TestCase t : listOfTestCase) 
		{
			ExtentTestManager.startTest(t.getTestCase());
			System.out.println(t.getTestCase());
			String jsonPayload = null;
			if(t.getJsonObject() != null) 
			{
				jsonPayload = replaceVariables(t.getJsonObject().toString());
			} 
			else if(t.getJsonObjects() != null) 
			{
				jsonPayload = t.getJsonObjects().toString();
			}
			else if(t.getjsonarray()!=null) {
				jsonPayload = replaceVariables(t.getjsonarray().toString());
			}
			//System.out.println(jsonPayload);
			//RestAPIUtil.getInstance().invokePOST(URI, jsonPayload);
			testdata="";urlmethod = "URL : "+url + "<br>Method : PATCH ";
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			response = patchForIntegrations(url, jsonPayload, map);
			System.out.println("response code :"+response.getStatusCode());
			System.out.println("payload status :"+t.getExpectedStatusCode());
			ExtentTestManager.info("Expected status code : "+t.getExpectedStatusCode()+" and Expected Message : "+t.getExpectedMessage());
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())) {
				String[] fieldlist = t.getExpectedMessage().split(",");
				checkAttributes(response,fieldlist);
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())&&response.asString().toLowerCase().contains(t.getExpectedMessage().toLowerCase().trim()))) {
					ExtentTestManager.passResults(t.testCase+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(t.testCase+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(t.testCase,response);
			ExtentTestManager.endTest();
		}
		return responses;
	}
	public String searchFilter(String url,Map<String, String> data) {
		boolean initial=true;
		if(!data.get("pageNo").equals("X")) {
			if(initial) {
				url=url+"?"+"pageNo="+data.get("pageNo");
				initial = false;
			}
			else
				url=url+"&"+"pageNo="+data.get("pageNo");
		}
		if(!data.get("pageSize").equals("X")) {
			if(initial) {
				url=url+"?"+"pageSize="+data.get("pageSize");
				initial = false;
			}
			else
				url=url+"&"+"pageSize="+data.get("pageSize");
		}
		if(!data.get("isDescendingOrder").equals("X")) {
			if(initial) {
				url=url+"?"+"isDescendingOrder="+data.get("isDescendingOrder");
				initial = false;
			}
			else
				url=url+"&"+"isDescendingOrder="+data.get("isDescendingOrder");
		}
		if(!data.get("sortName").equals("X")) {
			if(initial) {
				url=url+"?"+"sortName="+data.get("sortName");
				initial = false;
			}
			else
				url=url+"&"+"sortName="+data.get("sortName");
		}
		if(!data.get("queryString").equals("X")) {
			if(initial) {
				url=url+"?"+"queryString="+data.get("queryString");
				initial = false;
			}
			else
				url=url+"&"+"queryString="+data.get("queryString");
		}
		
		return url;
	}
	
	public static void writeUsingFiles(String TestCaseName,String inputs,String testData,String Error,String File) {
		 String data = TestCaseName+"\t\t"+inputs+"\t\t"+testData+"\t\t"+Error;
	        try {
	            File file = new File(System.getProperty("user.dir")+"/src/test/resources/"+File);
	            FileWriter fr = new FileWriter(file, true);
	            BufferedWriter br = new BufferedWriter(fr);
	            br.write(data+"\n");

	            br.close();
	            fr.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
	public static void writeNotSavedProperties(String TestCaseName,String var) {
		String data = TestCaseName+"\t\t"+var;
        try {
            File file = new File(System.getProperty("user.dir")+"/src/test/resources/notsaved.txt");
            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            br.write(data+"\n");

            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	
	public static void checkFailure(String response) {
		if(response.contains("io.restassured.internal")) {
			writeUsingFiles(ExtentTestManager.getTest().getModel().getName(),urlmethod,testdata,Error+"<br>"+response.toString(), "AutomationFailures.txt");
			Error="";
		}
		else if(response.contains("null")) {
			writeUsingFiles(ExtentTestManager.getTest().getModel().getName(),urlmethod,testdata,response.toString(), "AutomationFailures.txt");
		}
		else {
			writeUsingFiles(ExtentTestManager.getTest().getModel().getName(),urlmethod,testdata,response.toString(), "genuine.txt");
		}
	}
	
	public Map<String, Response> methodPOSTforSSOIntegration(String url,String jsonFName,Map<String,String> map) throws Exception 
	{
		Response response;
		Map<String,Response> responses = new HashMap<String,Response>();
		List<TestCase> listOfTestCase = getPayloads(jsonFName);
		//ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
		int size = listOfTestCase.size();
		int count=0;
		for(TestCase t : listOfTestCase) 
		{
			count++;
			ExtentTestManager.startTest(t.getTestCase());
			System.out.println(t.getTestCase());
			String jsonPayload = null;
			if(t.getJsonObject() != null) 
			{
				jsonPayload = replaceVariables(t.getJsonObject().toString());
			} 
			else if(t.getJsonObjects() != null) 
			{
				jsonPayload = t.getJsonObjects().toString();
			}
			else if(t.getjsonarray()!=null) {
				jsonPayload = replaceVariables(t.getjsonarray().toString());
			}
			else {
				jsonPayload="null";
			}
			//System.out.println(jsonPayload);
			//RestAPIUtil.getInstance().invokePOST(URI, jsonPayload);
			testdata="";testdata="";urlmethod = "URL : "+url + "<br>Method : POST ";
			if(map!=null) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				testdata=testdata+entry.getKey()+" : "+entry.getValue()+"<br>";
			}
			}
			else {
				testdata=testdata+"No inputs";
			}
			map.put("intgId",t.getDescription().toString());
			response = post(url, jsonPayload, map);
			System.out.println("response code :"+response.getStatusCode());
			System.out.println("payload status :"+t.getExpectedStatusCode());
			ExtentTestManager.info("Expected status code : "+t.getExpectedStatusCode()+" and Expected Message : "+t.getExpectedMessage());
			if(response.getStatusCode()==200 && response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())) {
				String[] fieldlist = t.getExpectedMessage().split(",");
				checkAttributes(response,fieldlist);
				if(size!=count) {
					String installedId=response.jsonPath().getString("id");
					JSONObject jsonP = new JSONObject();
					jsonP.put("uninstallReason", "Reason");
					Map<String,String> umap= new HashMap<String,String>();
					umap.put("clientId",map.get("clientId"));
					umap.put("installedIntgId",installedId);
					delete(APIUri.UNINSTALL_INTEGRATION,jsonP, umap);
				}
			}
			else {
				if((response.getStatusCode()==Integer.parseInt(t.getExpectedStatusCode())&&response.asString().toLowerCase().contains(t.getExpectedMessage().toLowerCase().trim()))) {
					ExtentTestManager.passResults(t.testCase+" is passed", "ResponseCode is "+response.getStatusCode());
				}
				else {
					ExtentTestManager.failResults(t.testCase+" is failed","ResponseCode is "+response.getStatusCode());
					checkFailure(response.asString());
				}
			}
			responses.put(t.testCase,response);
			ExtentTestManager.endTest();
		}
		return responses;
	}
	
}