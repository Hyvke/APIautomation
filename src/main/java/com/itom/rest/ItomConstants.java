/**
 * 
 */
package com.itom.rest;

import com.itom.base.BaseClass;;

/**
 * @author prabhudatta.choudhur
 *
 */
public class ItomConstants {
	
	public static String jsonFilesPath = BaseClass.getCurrentDirectory();
	public static String BUILD_VERSION_PORTAL= "5.2.0";
	public static String executionType = (String) BaseClass.getProperty("executionType");
	
	public static String clientId = (String) BaseClass.getProperty("clientId");//"client_33553"; 
	public static String deviceId = (String) BaseClass.getProperty("deviceId");//"9c7bed46-d300-41a9-b1b9-124afdc75770";
	public static String test_automation_DeviceGroup_UUID = (String) BaseClass.getProperty("test_automation_DeviceGroup_UUID");
	public static String mspID = (String) BaseClass.getProperty("mspID");
	public static String client_key = (String) BaseClass.getProperty("client_key");
	public static String client_sec = (String) BaseClass.getProperty("client_sec");
	public static String sp_secret = (String) BaseClass.getProperty("secret");
	public static String sp_key = (String) BaseClass.getProperty("key");
	public static String env = (String) BaseClass.getProperty("targetEnv");
	public static String PatchVersion = (String) BaseClass.getProperty("patchVersion");
	public static String AgentFileName =(String) BaseClass.getProperty("agentFilename");
	public static String solutionprovider_key = (String) BaseClass.getProperty("solutionprovider_key");
	public static String solutionprovider_sec = (String) BaseClass.getProperty("solutionprovider_sec");
	public static String cloudProviderId = "685648";
	
	public static String APPLICATION_URL=(String) BaseClass.getProperty("opsRamp_web_url");
	public static String BUILD_VERSION = (String) BaseClass.getProperty("buildVersion");
	public static String roleId = (String) BaseClass.getProperty("roleId");//1138
	public static String cloudProviderUID=(String)BaseClass.getProperty("cloudProviderUID");
	public static String USERNAME = (String) BaseClass.getProperty("userName");
	public static String PASSWORD = (String) BaseClass.getProperty("passWord");
	public static String DEVICElONGID = (String)BaseClass.getProperty("deviceLongID");
	
	public static String patch_key = (String) BaseClass.getProperty("patchSPKey");
	public static String patch_sec = (String) BaseClass.getProperty("patchSPSec");
	
	/**
	 *  Below constants are for send mail purpose
	 */
	
	public static String TO = (String) BaseClass.getProperty("to");
	public static String CC = (String) BaseClass.getProperty("cc");
	public static String BCC = (String) BaseClass.getProperty("bcc");
	public static String HOST = (String) BaseClass.getProperty("host");
	public static String PORT = (String) BaseClass.getProperty("port");
	public static String STARTTLS = (String) BaseClass.getProperty("starttls");
	public static String AUTH = (String) BaseClass.getProperty("auth");
	public static String SOCKET_FACTORY_CLASS = (String) BaseClass.getProperty("socketFactoryClass");
	public static String FALLBACK = (String) BaseClass.getProperty("fallback");
	public static String ITERATIONS = (String) BaseClass.getProperty("iterations");
	public static String PLATFORM = (String) BaseClass.getProperty("platform");
	public static String WHICH_LOGIN = (String) BaseClass.getProperty("whichLogin");
	public static String SCREENING_USER_AGREEMENT = (String) BaseClass.getProperty("screeninguserAgreement");
	public static String WHICH_CONFIG = (String) BaseClass.getProperty("whichConfig");
	
	public static String INTG_NAME = "CUSTOM";
	public static String INTG_LIMIT = "50";
	public static String INTG_UID = "";
	public static String INTG_ID = "232";
	
	
}
