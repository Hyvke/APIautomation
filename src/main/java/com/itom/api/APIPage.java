package com.itom.api;

import static io.restassured.RestAssured.given;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.params.CoreConnectionPNames;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.itom.base.BaseClass;
import com.itom.reports.ExtentManager;
import com.itom.reports.ExtentTestManager;
import com.itom.rest.APIUri;

import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.EncoderConfig;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.MultiPartConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.config.SSLConfig;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

/**
 * 
 * @author prabhudatta
 *
 */
@SuppressWarnings("deprecation")
public class APIPage {

	public static int NUMBER_OF_THREADS = 0;
	public static String API_NAME = null;
	private static String clientID = "clientId";
	private static String deviceID = "deviceId";
	private static String pageNO = "pageNo";
	private static String pageSize = "pageSize";
	private static String appJson = "application/json";
	private static String appactet = "application/octet-stream";
	private static String tenantID = "tenantId";
	private static String dateType = "yyyy-MM-dd'T'HH:mm:ss 0530";
	@SuppressWarnings("unused")
	private static String dateType2 = "yyyy-MM-dd'T'HH:mm:ss";
	private static String dateType3 = "yyyy-MM-dd HH:mm:ss";
	private static String ticket = "ticketType";
	private static String queryStr = "queryString";
	private static String alertID = "alertId";
	private static String serviceGroupID = "serviceGroupId";
	private static String search = "name";
	private static String siteID = "siteId";
	private static String querystring = "queryString";
	private static String discoveryID = "discoveryId";
	private static String ticketID = "ticketId";
	private static String noteID = "noteId";
	private JSONObject payloadJSON;
	private JSONArray responseJsonArray;
	private static long timeout = 180000;
	
	//public String accessToken = null;
	//public static long access_time= 0;

	public static String getAccessToken(String key, String secret) {
		Response response = null;
		try {
			HashMap<String, String> credentials = new HashMap<String, String>();
			credentials.put("grant_type", "client_credentials");
			credentials.put("client_secret", secret);//
			credentials.put("client_id", key);//
			RestAssured.useRelaxedHTTPSValidation();
			
			RestAssuredConfig config = RestAssured.config()
			        .httpClient(HttpClientConfig.httpClientConfig()
			                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
			                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
			
			config.encoderConfig(
					EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
			response = (Response) given().contentType("application/x-www-form-urlencoded").header("Accept", appJson)
					.config(RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation()))
					.formParams(credentials).when().post(APIUri.baseUri + APIUri.accessToken).then().extract();
		} catch (Exception e) {
			//AtuReports.info("The access token not generated proeprly ", e.getMessage(), "", e.getLocalizedMessage());
		}
		System.out.println("Access token :" + response.asString());
		String	accessToken = response.path("access_token").toString();
		//access_time = System.currentTimeMillis()+response.jsonPath().getLong("expires_in");
		return accessToken;
	}
	
	
	public static String getAccessToken(String key, String secret, String content, String header) {
		Response response = null;
		try {
			HashMap<String, String> credentials = new HashMap<String, String>();
			credentials.put("grant_type", "client_credentials");
			credentials.put("client_secret", secret);//
			credentials.put("client_id", key);//
			RestAssured.useRelaxedHTTPSValidation();
			
			RestAssuredConfig config = RestAssured.config()
			        .httpClient(HttpClientConfig.httpClientConfig()
			                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
			                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
			config.multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("PARTS_BOUNDARY"));
			response = (Response) given().config(RestAssured.config().encoderConfig(RestAssured.config()
					.getEncoderConfig().encodeContentTypeAs(content, ContentType.ANY))).header("Accept", header)
					.formParams(credentials).when().post(APIUri.baseUri + APIUri.accessToken).then().extract();
			
		} catch (Exception e) {
			e.printStackTrace();
			//AtuReports.info("The access token not generated proeprly ", e.getMessage(), "", e.getLocalizedMessage());
		}
		System.out.println("Access token :" + response.asString());
		String accessToken = response.path("access_token").toString();
		//access_time = System.currentTimeMillis()+response.jsonPath().getLong("expires_in");
		return accessToken;
	}

	/**
	 * setAccessToken() is to set the access token
	 */
	public static String setAccessToken() {
		Response response = null;
		String secret = (String) BaseClass.getConstant("secret");
		String key = (String) BaseClass.getConstant("key");
		
		try {
			HashMap<String, String> credentials = new HashMap<String, String>();
			credentials.put("grant_type", "client_credentials");
			credentials.put("client_secret", secret);//
			credentials.put("client_id", key);//
			RestAssured.useRelaxedHTTPSValidation();
			RestAssuredConfig config = RestAssured.config()
			        .httpClient(HttpClientConfig.httpClientConfig()
			                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
			                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
			config.encoderConfig(
					EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
			response = (Response) given().contentType("application/x-www-form-urlencoded").header("Accept", appJson)
					.config(RestAssured.config().sslConfig(new SSLConfig().relaxedHTTPSValidation()))
					.formParams(credentials).when().post(APIUri.baseUri + APIUri.accessToken).then().extract();
		} catch (Exception e) {
			e.printStackTrace();
			//AtuReports.info("The access token not generated proeprly ", e.getMessage(), "", e.getLocalizedMessage());
		}
		System.out.println(response.asString());
		String accessToken = response.path("access_token").toString();
		//access_time = System.currentTimeMillis()+response.jsonPath().getLong("expires_in");
		return accessToken;

	}

	/**
	 * Returns common request specification for API Request
	 * 
	 * @param accessToken2
	 * @return Returns common request specification for API Request
	 */
	private static RequestSpecification specificationBuilder(String accessToken2) {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(APIUri.apibaseUri);
		builder.addHeader("Authorization", "Bearer " + accessToken2);
		builder.setContentType(ContentType.URLENC);
		builder.setAccept(appJson);
		RestAssuredConfig config = RestAssured.config()
		        .httpClient(HttpClientConfig.httpClientConfig()
		                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
		                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
		
		
		config.encoderConfig(
				EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}
	
	private static RequestSpecification specificationBuilderforFileDownload(String accessToken2) {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(APIUri.apibaseUri);
		builder.addHeader("Authorization", "Bearer " + accessToken2);
		builder.setContentType(ContentType.BINARY);
		builder.setAccept(appactet);
		RestAssuredConfig config = RestAssured.config()
		        .httpClient(HttpClientConfig.httpClientConfig()
		                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
		                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
		
		
		config.encoderConfig(
				EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}
	
	private static RequestSpecification specificationBuilderoctate(String accessToken2) {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(APIUri.apibaseUri);
		builder.addHeader("Authorization", "Bearer " + accessToken2);
		builder.setContentType(ContentType.BINARY);
		builder.setAccept(appactet);
		RestAssuredConfig config = RestAssured.config()
		        .httpClient(HttpClientConfig.httpClientConfig()
		                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
		                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
		
		
		config.encoderConfig(
				EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}
	
	
	
	/**
	 * Returns common request specification for API Request
	 * 
	 * @param accessToken2
	 * @return Returns common request specification for API Request
	 */
	private static RequestSpecification specificationBuilderAgentDownload(String accessToken2) {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(APIUri.apibaseUri);
		builder.addHeader("Authorization", "Bearer " + accessToken2);
		builder.setContentType(ContentType.BINARY);
		builder.setAccept(appJson);
		RestAssuredConfig config = RestAssured.config()
		        .httpClient(HttpClientConfig.httpClientConfig()
		                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
		                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
		
		
		config.encoderConfig(
				EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}

	/**
	 * Returns common request specification for API Request
	 * 
	 * @param accessToken2
	 * @return Returns common request specification for API Request
	 */
	private static RequestSpecification postspecificationBuilder(String accessToken2) {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(APIUri.apibaseUri);
		builder.addHeader("Authorization", "Bearer " + accessToken2);
		builder.setContentType(ContentType.JSON);
		builder.setAccept(appJson);
		RestAssuredConfig config = RestAssured.config()
		        .httpClient(HttpClientConfig.httpClientConfig()
		                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
		                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
		
		
		config.encoderConfig(
				EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}
	
	private static RequestSpecification postspecificationBuilder2(String accessToken2) {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(APIUri.basicUri);
		builder.addHeader("Authorization", "Bearer " + accessToken2);
		builder.setContentType(ContentType.JSON);
		builder.setAccept(appJson);
		RestAssuredConfig config = RestAssured.config()
		        .httpClient(HttpClientConfig.httpClientConfig()
		                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
		                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
		
		
		config.encoderConfig(
				EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}
	
	
	/**
	 * Returns common request specification for API Request
	 * 
	 * @param accessToken2
	 * @return Returns common request specification for API Request
	 */
	private static RequestSpecification postspecificationBuilderMultiPart(String accessToken2, String content) {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(APIUri.apibaseUri);
		builder.addHeader("Authorization", "Bearer " + accessToken2);
		builder.setContentType(content);
		builder.setAccept(content);
		
		RestAssuredConfig config = RestAssured.config()
		        .httpClient(HttpClientConfig.httpClientConfig()
		                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
		                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
		config.multiPartConfig(MultiPartConfig.multiPartConfig().defaultBoundary("PARTS_BOUNDARY"));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}

	public static Response post(String pathUri, Object obj, Map<String, String> map, String key, String secret) {
		Response response = null;
		
		String accessToken = getAccessToken(key, secret);
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when().post(APIUri.apibaseUri + pathUri)
						.then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.post(APIUri.apibaseUri + pathUri, map).then().extract().response();
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.post(APIUri.apibaseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.post(APIUri.apibaseUri + pathUri, map).then().extract().response();
			}
		}
		ExtentTestManager.getTest().info(APIUri.basicUri+pathUri);
		ExtentTestManager.getTest().info("Method : POST");
		ExtentTestManager.getTest().info("ResponseCode : "+response.statusCode());
		ExtentTestManager.getTest().info("Response Message :"+response.asString());
		//AtuReports.info(APIUri.apibaseUri,  pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
	
	public static Response postWithContent(String pathUri, Object obj, Map<String, String> map, String key, String secret, String content, String header, String filename) {
		Response response = null;
		String accessToken = getAccessToken(key, secret, content, header);
		
		if (obj == null) {

			if (map == null) {
				response =given().spec(postspecificationBuilderMultiPart(accessToken,content)).multiPart(new MultiPartSpecBuilder(content).fileName(filename)
		                .controlName("file")
		                .build())
						.body(obj)
						.post(APIUri.apibaseUri + pathUri);
				
			} else {
				response = given().spec(postspecificationBuilderMultiPart(accessToken,content)).multiPart(new MultiPartSpecBuilder(content).fileName(filename)
		                .controlName("file")
		                .build())
						.body(obj)
						.post(APIUri.apibaseUri + pathUri, map);
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilderMultiPart(accessToken,content)).multiPart(new MultiPartSpecBuilder(content).fileName(filename)
		                .controlName("file")
		                .build())
						.body(obj)
						.post(APIUri.apibaseUri + pathUri);
			} else {
				response = given().spec(postspecificationBuilderMultiPart(accessToken,content)).multiPart(new MultiPartSpecBuilder(content).fileName(filename)
		                .controlName("file")
		                .build()).body(obj)
						.post(APIUri.apibaseUri + pathUri, map);
			}
		}
		//AtuReports.info(APIUri.apibaseUri,  pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}


	/**
	 * Returns post response of an API
	 * 
	 * @param path
	 *            uri The unique path for particular API
	 * @param obj
	 *            The object is payload given to request for creation.
	 * @param map
	 *            The map object is to assign path variables
	 * @return Returns post response of an API
	 */
	public static Response post(String pathUri, Object obj, Map<String, String> map) {
		Response response = null;
		if(pathUri.contains("/basic/")) {
			System.out.println(APIUri.basicUri + pathUri);
			String accessToken = setAccessToken();
			if (obj == null) {

				if (map == null) {
					response = given().spec(postspecificationBuilder2(accessToken)).when().post(APIUri.basicUri + pathUri)
							.then().extract().response();
				} else {
					response = given().spec(postspecificationBuilder2(accessToken)).when()
							.post(APIUri.basicUri + pathUri, map).then().extract().response();
					ExtentTestManager.info("Url replaced data : "+map.toString());
				}

			} else {

				if (map == null) {
					response = given().spec(postspecificationBuilder2(accessToken)).body(obj).when()
							.post(APIUri.basicUri + pathUri).then().extract().response();
				} else {
					response = given().spec(postspecificationBuilder2(accessToken)).body(obj).when()
							.post(APIUri.basicUri + pathUri, map).then().extract().response();
					ExtentTestManager.info("Url replaced data : "+map.toString());
				}
			}
			System.out.println(response.asString());
			ExtentTestManager.getTest().info(APIUri.basicUri+pathUri);
			ExtentTestManager.getTest().info("Method : POST");
			ExtentTestManager.getTest().info("ResponseCode : "+response.statusCode());
			ExtentTestManager.getTest().info("Response Message :"+response.asString());
			//AtuReports.info(APIUri.apibaseUri ,  pathUri,"200", String.valueOf(response.statusCode())+response.asString());
			return response;
		}
	else {
			System.out.println(APIUri.apibaseUri + pathUri);
		String accessToken = setAccessToken();
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when().post(APIUri.apibaseUri + pathUri)
						.then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.post(APIUri.apibaseUri + pathUri, map).then().extract().response();
				ExtentTestManager.info("Url replaced data : "+map.toString());
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.post(APIUri.apibaseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.post(APIUri.apibaseUri + pathUri, map).then().extract().response();
				ExtentTestManager.info("Url replaced data : "+map.toString());
			}
		}
		System.out.println(response.asString());
		ExtentTestManager.getTest().info(APIUri.apibaseUri+pathUri);
		ExtentTestManager.getTest().info("Method : POST");
		ExtentTestManager.getTest().info("ResponseCode : "+response.statusCode());
		ExtentTestManager.getTest().info("Response Message :"+response.asString());
		//AtuReports.info(APIUri.apibaseUri ,  pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
}

	public static Response put(String pathUri, Object obj, Map<String, String> map) {
		Response response = null;
		
		String accessToken = setAccessToken();
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when().put(pathUri).then().extract()
						.response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when().put(pathUri, map).then().extract()
						.response();
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when().put(pathUri).then()
						.extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when().put(pathUri, map).then()
						.extract().response();
			}
		}
		ExtentTestManager.getTest().info(APIUri.apibaseUri+pathUri);
		ExtentTestManager.getTest().info("Method : PUT");
		ExtentTestManager.getTest().info("ResponseCode : "+response.statusCode());
		ExtentTestManager.getTest().info("Response Message :"+response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
	
	
	/**
	 * Returns get response of an API
	 * 
	 * @param map
	 *            The map object is to assign path variables
	 * @return Returns get response of an API
	 */
	@SuppressWarnings("unchecked")
	public static Response getDownload(String pathUri, @SuppressWarnings("rawtypes") Map map) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", APIUri.apibaseUri + pathUri, "", "");
		
		String accessToken = setAccessToken();
		if (map == null) {
			response = given().spec((RequestSpecification) specificationBuilderAgentDownload(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri).then().extract().response();
		}

		else {
			response = given().spec((RequestSpecification) specificationBuilderAgentDownload(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri, map).then().extract().response();

		}
		System.out.println(
				APIUri.apibaseUri + pathUri + " >> " + response.getStatusCode() + " >>> " + response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}

	/**
	 * Returns get response of an API
	 * 
	 * @param map
	 *            The map object is to assign path variables
	 * @return Returns get response of an API
	 */
	@SuppressWarnings("unchecked")
	public static Response get(String pathUri, @SuppressWarnings("rawtypes") Map map) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", APIUri.apibaseUri + pathUri, "", "");
		ExtentTestManager.info("The complete API Path is "+ APIUri.apibaseUri + pathUri);
		
		String accessToken = setAccessToken();
		if (map == null) {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri).then().extract().response();
		}

		else {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri, map).then().extract().response();
			ExtentTestManager.info("Url replaced data : "+map.toString());
		}
		ExtentTestManager.getTest().info("Method : GET");
		
		System.out.println(
				APIUri.apibaseUri + pathUri + " >> " + response.getStatusCode() + " >>> " + response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		ExtentTestManager.info(String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
	public static Response getforFileDownload(String pathUri, @SuppressWarnings("rawtypes") Map map) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", APIUri.apibaseUri + pathUri, "", "");
		ExtentTestManager.info("The complete API Path is "+ APIUri.apibaseUri + pathUri);
		
		String accessToken = setAccessToken();
		if (map == null) {
			response = given().spec((RequestSpecification) specificationBuilderforFileDownload(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri).then().extract().response();
		}

		else {
			response = given().spec((RequestSpecification) specificationBuilderforFileDownload(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri, map).then().extract().response();
			ExtentTestManager.info("Url replaced data : "+map.toString());
		}
		ExtentTestManager.getTest().info("Method : GET");
		
		System.out.println(
				APIUri.apibaseUri + pathUri + " >> " + response.getStatusCode());
		//AtuReports.info(APIUri.apibaseUri , pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		ExtentTestManager.info(String.valueOf(response.statusCode()));
		return response;
	}

	@SuppressWarnings("unchecked")
	public static Response getWithKeySec(String pathUri, @SuppressWarnings("rawtypes") Map map, String clientKey,
			String clientSecret) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", APIUri.apibaseUri + pathUri, "", "");
		
		String accessToken = getAccessToken(clientKey, clientSecret);
		if (map == null) {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri).then().extract().response();
		}

		else {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri, map).then().extract().response();

		}
		System.out.println(APIUri.apibaseUri + pathUri);
		System.out.println(response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
	
	@SuppressWarnings("unchecked")
	public static Response getWithKeysSec(String pathUri, @SuppressWarnings("rawtypes") Map map, String clientKey,
			String clientSecret, String content, String header) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", APIUri.apibaseUri + pathUri, "", "");
		String accessToken = getAccessToken(clientKey, clientSecret, content, header);
		if (map == null) {
			response = given().spec((RequestSpecification) specificationBuilderoctate(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri).then().extract().response();
		}

		else {
			response = given().spec((RequestSpecification) specificationBuilderoctate(accessToken)).when()
					.get(APIUri.apibaseUri + pathUri, map).then().extract().response();

		}
		System.out.println(APIUri.apibaseUri + pathUri);
		System.out.println(response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	

	public static Map<String, String> prepareDeviceAvailability(String clientId, String resourceType,
			String resourceUUID) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put("resourceType", resourceType);
		map.put("resourceUUID", resourceUUID);
		return map;
	}

	public static Map<String, String> prepareMapServiceGroupID(String clientId, String serviceGroupId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put(serviceGroupID, serviceGroupId);
		return map;
	}

	public static Map<String, String> prepareMapServiceGroupSearchName(String clientId, String serviceGroupName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put(search, serviceGroupName);
		return map;
	}

	public static Map<String, String> prepareMapDeviceGroupID(String clientId, String deviceGroupId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put("deviceGroupId", deviceGroupId);
		return map;
	}

	/**
	 * Returns map containing clientId and device_id
	 * 
	 * @param clientId
	 *            The id is client unique id
	 * @param device_id
	 *            The id is device uuid
	 * @return Returns map containing clientId and device_id
	 */
	public static Map<String, String> prepareMapDevice(String clientId, String device_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put(deviceID, device_id);
		return map;
	}

	public static Map<String, String> prepareMapSite(String clientId, String siteId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put(siteID, siteId);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareMapSearch(String clientId, String device_id) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateType);
		Map map = new HashMap();
		map.put(clientID, clientId);
		map.put(deviceID, device_id);
		map.put(pageNO, 1);
		map.put(pageSize, 10);
		map.put("isDescendingOrder", true);
		map.put(queryStr, "recStatus:Completed+consoleType:RDP+startDate:" + sdf.format(new Date()) + "+endDate:"
				+ sdf.format(new Date()));

		return map;
	}

	public static Map<String, String> prepareMapAlertCoRelation(String clientId, String correlationId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put("correlationId", correlationId);
		return map;
	}

	public static Map<String, String> prepareMapTenant(String tenantId, String device_id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put(deviceID, device_id);
		return map;
	}

	public static Map<String, String> prepareMapDownloadResponse(String clientId, String incidentId, String responseId,
			String resourceId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put("incidentId", incidentId);
		map.put("responseId", responseId);
		map.put("resourceId", resourceId);
		return map;
	}

	public static Map<String, String> prepareMapIncident(String clientId, String incidentId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put("incidentId", incidentId);
		return map;
	}

	public static Map<String, String> prepareMapOrgId(String orgId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orgId", orgId);
		return map;
	}
	
	public static int randomRange(int min, int max)
	{
		int range = (max - min) + 1;     
		return (int)(Math.random() * range) + min;
	}
	
	public static String getRandomString() {
		String alphabets = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
		String string = "AutoTest"+ alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51))+alphabets.charAt(randomRange(0,51));
		return string;
	}
	
	@SuppressWarnings("unchecked")
	public static Response createUser(String orgId) {
		int val = getRandomValues();

		JSONObject userPayload = new JSONObject();
		userPayload.put("loginName", val + "_AutoTestUser_"+getRandomString());
		userPayload.put("lastName", "AutoTesting");
		userPayload.put("firstName", val + "_AutoTestUser_"+getRandomString());
		userPayload.put("password", "Api@123");
		userPayload.put("email", "iAutomation@gml.com");
		userPayload.put("country", "India");
		JSONObject timeZone = new JSONObject();
		timeZone.put("code", "GMT");
		userPayload.put("timeZone", timeZone);
		Response createRes = post(APIUri.CREATE_USER, userPayload, prepareMapOrgId(orgId));
		System.out.println(createRes.getStatusCode() + " >>> " + createRes.asString());
		return createRes;
	}

	public static Map<String, String> prepareMapClient(String clientid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		return map;
	}

	public static Map<String, String> prepareMapClientResource(String clientid, String resourceId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("resourceId", resourceId);
		return map;
	}
	
	public static Map<String, String> prepareMapClientTopology(String clientid, String ip) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("IP", ip);
		return map;
	}

	public static Map<String, String> prepareMapAlert(String clientid, String alertId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put(alertID, alertId);
		return map;
	}

	public static Map<String, String> prepareMapCategories(String clientid, String incidents) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put(alertID, incidents);
		return map;
	}

	public static Map<String, String> prepareMapSubCategories(String clientid, String incidents, String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put(alertID, incidents);
		map.put("Id", id);
		return map;
	}

	public static Map<String, String> prepareMapAlertAction(String clientId, String alertId, String action) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put(alertID, alertId);
		map.put("action", action);
		return map;
	}

	/**
	 * Returns map containing partner id or client id or service provider id
	 * based upon parameter send
	 * 
	 * @param id
	 *            The id will be partner id or client id or service provider id
	 * @return Returns map containing partner id or client id or service
	 *         provider id based upon parameter send
	 */
	public static Map<String, String> prepareMap(String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		return map;
	}

	public static Map<String, String> prepareMapExtAlertId(String id, String extAlertId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		map.put("extAlertIds", extAlertId);
		return map;
	}

	public static Map<String, String> prepareMapSearchSite(String tenantId, String queryString) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put(querystring, queryString);
		return map;
	}

	public static Map<String, String> prepareUserMap(String tenantId, String userID) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("userId", userID);
		return map;
	}

	public static Map<String, String> prepareRoleMap(String tenantId, String roleId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("roleId", roleId);
		return map;
	}

	public static Map<String, String> prepareSRMap(String id, String serviceReqID) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		map.put("serviceRequestId", serviceReqID);
		return map;
	}

	public static Map<String, String> prepareTaskMap(String id, String taskID) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		map.put("taskId", taskID);
		return map;
	}

	public static Map<String, String> prepareTBRMap(String id, String timeBoundRequestId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		map.put("timeBoundRequestId", timeBoundRequestId);
		return map;
	}

	/**
	 * Returns object of particular JSON file
	 * 
	 * @param fileName
	 *            The JSON file name to create object
	 * @param resourceDirectory
	 *            The resource directory of file
	 * @return Returns object of particular JSON file
	 */
	public static Object getObject(String fileName, String resourceDirectory) {
		File file = new File(resourceDirectory + "/" + fileName);
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
			return (Object) new JSONParser().parse(fileReader);
		} catch (FileNotFoundException fileNotFoundException) {
			return fileNotFoundException;
		} catch (IOException ioException) {
			return ioException;
		} catch (ParseException parseException) {
			return parseException;
		} finally {
			try {
				if (fileReader != null) {
					fileReader.close();
				}

			} catch (Exception e) {
				//AtuReports.failResults("inside get object exception ", e.getLocalizedMessage(), e.getMessage(), "");
			}
			file = null;
		}
	}

	public static Response create_Incident(String payloadDirectory, String clientId) {
		Response response = null;
		JSONArray jsonArray = (JSONArray) (getObject("create_incident_payload.json", payloadDirectory));
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject json = (JSONObject) jsonArray.get(i);
			response = post(APIUri.CREATE_INCIDENT, json, prepareMapClient(clientId));
			if (response.getStatusCode() == 200) {
				//AtuReports.info("Incident created successfully", response.asString(), "", "");
			} else {
				//AtuReports.info("Couldnt able to create Incident ", response.asString(), "", "");
			}
		}
		return response;
	}

	public static boolean isResponseEmpty(String response) {
		boolean isEmpty = false;
		try {

			if (!StringUtils.isAlphanumeric(response)) {
				isEmpty = true;
				//AtuReports.warning("The response is coming as empty ", response, "", "");
			} else {
				isEmpty = false;
				//AtuReports.passResults1("The response is coming as ", response, "", "");
			}
		} catch (Exception e) {
			//AtuReports.warning("The response is coming as ", response, e.getMessage(), e.getLocalizedMessage());
		}

		return isEmpty;
	}

	/*
	 * public static FileInputStream readPayLoadFile(String path) { String
	 * apiDataFolderPath = System.getProperty("user.dir") + "/" + (String)
	 * BaseClass.context.getBean("api_folder"); FileInputStream fis = null; try {
	 * fis = new FileInputStream(apiDataFolderPath + "/" + path +
	 * "_Payload.json.gz"); } catch (FileNotFoundException fe) {
	 * //AtuReports.failResults(path +
	 * " payload file couldnt able to locate in the folder ",
	 * apiDataFolderPath,fe.getMessage(), fe.getLocalizedMessage()); } return fis; }
	 */

	public static Map<String, String> prepareMapProblem(String clientId, String problemId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put("problemId", problemId);
		return map;
	}

	public static Map<String, String> prepareMapStatusReasons(String tenantId, String ticketType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put(ticket, ticketType);
		return map;
	}

	public static Map<String, String> prepareCategoriesMap(String clientId, String ticketType) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put(ticket, ticketType);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareSubCategoriesMap(String clientId, String ticketType,
			int parentCategoryId) {
		Map map = new HashMap();
		map.put(clientID, clientId);
		map.put(ticket, ticketType);
		map.put("parentCategoryId", parentCategoryId);
		return map;
	}

	public static String generateRandom(List<String> list) {
		Collections.shuffle(list);
		return list.get(0).toString();
	}

	public static String generateRandomAmongInt(List<Integer> list) {
		Collections.shuffle(list);
		return list.get(0).toString();
	}

	public static Map<String, String> prepareGetJobMap(String tenantId, String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("id", id);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareGetJobMap(String tenantId, int id) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, tenantId);
		map.put("id", id);
		return map;
	}

	
	public static Map<String, String> prepareAttributeMap(String tenantId, String id, String valueId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("id", id);
		map.put("valueID", valueId);
		return map;
	}

	public static Map<String, String> prepareGetIntgEvntMap(String tenantId, String installedIntgId, String eventId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("installedIntgId", installedIntgId);
		map.put("eventId", eventId);
		return map;
	}

	public static Map<String, String> prepareGetMetricMap(String tenantId, String metric) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("metric", metric);
		return map;
	}

	public static Map<String, String> prepareGetMetricResourceMap(String tenantId, String resourceType,
			String resourceID) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("rtype", resourceType);
		map.put("resource", resourceID);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareGetMetricOnMetricMap(String tenantId, String resourceType,
			String resourceID, String metric,String metricGrp) {
		Map map = new HashMap();
		map.put(tenantID, tenantId);
		map.put("rtype", resourceType);
		map.put("resource", resourceID);
		map.put("metric", metric);
		map.put("metricGrp", metricGrp);
		map.put("startTime", String.valueOf(new Date().getTime()));
		map.put("endTime", String.valueOf(new Date().getTime()));
		return map;
	}

	public static List<Boolean> executePUTAPI(String api, Object jso, Map<String, String> paramMap) {
		List<Boolean> result = new CopyOnWriteArrayList<Boolean>();
		Response response = null;
		response = APIPage.put(api, jso, paramMap);
		System.out.println(response.getStatusCode() + "PUT API response >>> " + response.asString());
		if (response.getStatusCode() == 200) {
			//AtuReports.passResults(
					//api + "API could find response associated with the tenant id " + ItomConstants.clientId,
					//response.asString(), "200", "");
			result.add(Boolean.TRUE);
		} else {
			result.add(Boolean.FALSE);
			//AtuReports.warning(
					//api + "API could not find any reponse associated with the tenant id " + ItomConstants.clientId,
					//response.asString(), "200", String.valueOf(response.getStatusCode()));
		}

		return result;
	}

	public static List<Boolean> executeAPI(String api, Map<String, String> paramMap) {
		List<Boolean> result = new CopyOnWriteArrayList<Boolean>();
		Response response = null;
		response = APIPage.get(api, paramMap);
		System.out.println(response.getStatusCode());
		if (response.getStatusCode() == 200) {
			//AtuReports.passResults(
					//api + "API could find response associated with the tenant id " + ItomConstants.clientId,
					//response.asString(), "200", "");
			result.add(Boolean.TRUE);
		} else {
			result.add(Boolean.FALSE);
			//AtuReports.warning(
					//api + "API could not find any response associated with the tenant id " + ItomConstants.clientId,
					//response.asString(), "200", String.valueOf(response.getStatusCode()));
		}

		return result;
	}

	public static List<Boolean> executePostAPI(String api, Object jso, Map<String, String> paramMap) {
		List<Boolean> result = new CopyOnWriteArrayList<Boolean>();
		Response response = null;
		response = APIPage.post(api, jso, paramMap);
		System.out.println(response.getStatusCode() + "POST API response >>> " + response.asString());
		if (response.getStatusCode() == 200) {
			//AtuReports.passResults(
					//api + "API could find response associated with the tenant id " + ItomConstants.clientId,
					//response.asString(), "200", "");
			result.add(Boolean.TRUE);
		} else {
			result.add(Boolean.FALSE);
			//AtuReports.warning(
					//api + "API could not find any reponse associated with the tenant id " + ItomConstants.clientId,
					//response.asString(), "200", String.valueOf(response.getStatusCode()));
		}

		return result;
	}

	public static List<Boolean> executePost(String api, Object jso, Map<String, String> paramMap, String key,
			String secret) {
		List<Boolean> result = new CopyOnWriteArrayList<Boolean>();
		Response response = null;
		response = APIPage.post(api, jso, paramMap, key, secret);
		System.out.println(response.getStatusCode() + "POST API response >>> " + response.asString());
		if (response.getStatusCode() == 200) {
			//AtuReports.passResults(
					//api + "API could find response associated with the tenant id " + ItomConstants.clientId,
					//response.asString(), "200", "");
			result.add(Boolean.TRUE);
		} else {
			result.add(Boolean.FALSE);
			//AtuReports.warning(
					//api + "API could not find any reponse associated with the tenant id " + ItomConstants.clientId,
					//response.asString(), "200", String.valueOf(response.getStatusCode()));
		}

		return result;
	}

	public static Map<String, String> prepareMapCustomField(String clientId, String customField) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientId);
		map.put("classCode", customField);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareGetResponseMap(String clientId, String param, String serviceRequestID) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateType);
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, clientId);
		map.put(param, serviceRequestID);
		map.put(pageNO, 1);
		map.put(pageSize, 10);
		map.put(queryStr, "startCreationDate:" + sdf.format(new Date()) + "+endCreationDate:" + sdf.format(new Date()));
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareGetAssAttrMap(String attributeId) {
		Map map = new HashMap<Object, Object>();
		map.put("attributeId", attributeId);
		map.put(pageNO, 1);
		map.put(pageSize, 10);
		map.put(queryStr, "");
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareRoasterMap(String tenantId) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, tenantId);
		map.put(pageNO, 1);
		map.put(pageSize, 10);
		map.put(queryStr, "name:Auto Create escalate alert policy API+allList:true");
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> preparePatchFeedMap(String tenantId) {
		Map map = new HashMap<Object, Object>();
		map.put(clientID, tenantId);
		map.put(pageNO, 1);
		map.put(pageSize, 10);
		return map;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareMap(String param, String value) {
		Map map = new HashMap<Object, Object>();
		map.put(param, value);
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareMapObject(Object param, Object value) {
		Map map = new HashMap<Object, Object>();
		map.put(param, value);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareClientMap(String tenantId, String clientid, String action) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, tenantId);
		map.put(clientID, clientid);
		map.put("action", action);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> preparemapTenant(String mspID, String clientId) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, mspID);
		map.put(clientID, clientId);
		return map;
	}

	

	
	@SuppressWarnings("unchecked")
	private void createDeviceGroupPayload() {
		int val = getRandomValues();
		this.payloadJSON = new JSONObject();
		this.payloadJSON.put( "name", "test device group"+val);
		this.payloadJSON.put( "type", "DEVICE_GROUP");
		
		this.responseJsonArray = new JSONArray();
		this.responseJsonArray.add(this.payloadJSON);
		
	}

	




	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareSPAttrValueMap(String attrID, String valueId) {
		Map map = new HashMap<Object, Object>();
		map.put("attributeId", attrID);
		map.put("valueId", valueId);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map preparePartnerMap(String partnerID, String action) {
		Map map = new HashMap<Object, Object>();
		map.put("id", partnerID);
		map.put("action", action);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map preparePartnersMap() {
		SimpleDateFormat sdf = new SimpleDateFormat(dateType);
		Map map = new HashMap<Object, Object>();
		map.put(pageSize, 10);
		map.put(queryStr, "startUpdationDate:" + sdf.format(new Date()) + "+endUpdationDate:" + sdf.format(new Date()));
		return map;
	}

	public static Map<String, String> prepareMapUser(String orgId, String userId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("orgId", orgId);
		map.put("userId", userId);
		return map;
	}

	/**
	 * @param clientid
	 * @param data
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareSearchUserMap(String clientid, Map<String, String> data) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, clientid);
		map.put(pageSize, 10);
		String qstring = "loginName:" + data.get("loginName") + "+lastName:" + data.get("lastName");
		map.put(queryStr, qstring.trim());
		return map;
	}
	
	/**
	 * @param clientid
	 * @param data
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareSearchUserMap(String clientid,  String query) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, clientid);
		map.put(pageSize, 10);
		map.put(queryStr, query);
		return map;
	}

	public static int getRandomValues() {
		Random r = new Random();
		return r.nextInt(5000) + 1;

	}

	public static Map<String, String> prepareIntgMapProp(String clientid, String integrationType, String entity) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("intgId", integrationType);
		map.put("entityType", entity);
		return map;
	}

	public static Map<String, String> prepareMapSearchDiscoveryProfile(String tenantId, String name) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("discoveryprofilename", name);
		return map;
	}

	public static Map<String, String> prepareMapDiscoveryProfileId(String clientId, String discoveryId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientId);
		map.put(discoveryID, discoveryId);
		return map;
	}

	public static Map<String, String> prepareMapDiscoveryProfiletenantId(String clientId, String discoveryId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientId);
		map.put(discoveryID, discoveryId);
		return map;
	}

	public static Map<String, String> prepareAlertIDMap(String clientId,String altIds) {
		Map<String, String> map = null;
			map = new HashMap<String, String>();
			map.put(tenantID, clientId);
			map.put("alertId", altIds);
		return map;
	}

	public static Map<String, String> prepareLatestAlertStateOfEachMetric(String rtypes, String resources,
			String metrics) {
		Map<String, String> map = null;
		try {
			metrics = java.net.URLDecoder.decode(metrics, "UTF-8");
			map = new HashMap<String, String>();
			map.put("rtype", rtypes);
			map.put("resource", resources);
			map.put("metric", metrics);
		} catch (Exception e) {
		}
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareUpdateTicketStatus(String clientid, long ticketId, int status) {
		Map map = new HashMap();
		map.put("clientId", clientid);
		map.put("ticketId", ticketId);
		map.put("status", status);
		return map;

	}

	public static Map<String, String> prepareGetTopMetrics(String clientId, String metrics, String duration) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("clientId", clientId);
		map.put("duration", duration);
		map.put("metricnames", metrics);

		return map;
	}

	public static Map<String, String> preparePurgeOldMetrics(String clientId, String duration) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenant", clientId);
		map.put("duration", duration);
		return map;
	}

	public static Map<String, String> prepareGetAvailableMetricsByResource(String clientId, String rtype,
			String resourceID) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenant", clientId);
		map.put("rtype", rtype);
		map.put("resource", resourceID);
		return map;
	}

	public static Map<String, String> prepareSaveMetricsOnDevice(String clientId, String resourceID) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenantUid", clientId);
		map.put("deviceUid", resourceID);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareGetTopMetricsWithQuery(String clientId, String metrics, String duration) {
		clientId = clientId.replaceAll("client_", "");
		clientId = clientId.trim();
		long clientID = Long.parseLong(clientId);
		Map map = new HashMap();
		map.put("clientId", clientID);
		map.put("duration", duration);
		map.put("metricnames", metrics);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareLatestMetricsByMetricID(String clientID, String rtype, String rID, String mid) {
		clientID = clientID.replaceAll("client_", "");
		clientID = clientID.trim();
		double clientIdl = Double.parseDouble(clientID);
		double resourceIdl = Double.parseDouble(rID);
		double metricIdl = Double.parseDouble(mid);
		long clientId = (long) clientIdl;
		long resourceId = (long) resourceIdl;
		long metricId = (long) metricIdl;
		Map map = new HashMap();
		map.put("tenant", clientId);
		map.put("rtype", rtype);
		map.put("resource", resourceId);
		map.put("mid", metricId);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareChangeMetricTypes(String newmetricId, String oldmetricId) {
		long newmetricID = Long.parseLong(newmetricId);
		long oldmetricID = Long.parseLong(oldmetricId);
		Map map = new HashMap();
		map.put("oldmid", oldmetricID);
		map.put("newmid", newmetricID);
		return map;
	}

	public static Map<String, String> prepareGetAvailableMetricsByMetricGroup(String clientId, String metricGroup) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("clientId", clientId);
		map.put("metricGroup", metricGroup);
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map metricmap(String tenantId, String resourceuuid) {
		Map map = new HashMap();
		map.put("tenantId", tenantId);
		map.put("resourceUUID", resourceuuid);
		return map;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map scanMap(String tenantId, String installedIntgId) {
		Map map = new HashMap();
		map.put("tenantId", tenantId);
		map.put("installedIntgId", installedIntgId);
		return map;
	}

	public static Map<String, String> prepareMapDownloadGoAgent(String clientid, String platfrm, String filename) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("platform", platfrm);
		map.put("fileName", filename);
		return map;
	}

	public static Map<String, String> prepareMapPlatform(String clientid, String platfrm) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("platform", platfrm);
		return map;
	}

	public static Map<String, String> prepareMapPatchVersion(String resourceid, String patchversion) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("resourceUUID", resourceid);
		map.put("patchVersion", patchversion);
		return map;
	}

	public static Map<String, String> prepareMapModule(String resourceid, String modulename, String moduleversion) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("resourceUUID", resourceid);
		map.put("moduleFileName", modulename);
		map.put("moduleVersion", moduleversion);
		return map;
	}

	public static Map<String, String> prepareMapPlugInName(String resourceid, String pluginname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("resourceUUID", resourceid);
		map.put("pluginName", pluginname);
		return map;
	}

	public static Map<String, String> prepareMapPlugInNameWithClient(String clientid, String pluginname) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("appUID", pluginname);
		return map;
	}

	public long getTimeInSeconds(long milliseconds) {
		return TimeUnit.MILLISECONDS.toSeconds(milliseconds);
	}

	public int getTimeDifference(long time1, long time2)

	{
		long td = time2 - time1;
		return (int) (td / 1000);
	}

	public static Map<String, String> prepareGetScriptMap(String tenantId, String id, String scriptId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("id", id);
		map.put("scriptId", scriptId);
		return map;
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareGetScriptMap(String tenantId, String id, int scriptId) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, tenantId);
		map.put("id", id);
		map.put("scriptId", scriptId);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareMapClientticket(String clientId, String ticketType, String ticketId) {
		Map map = new HashMap();
		map.put(clientID, clientId);
		map.put(ticket, ticketType);
		map.put(ticketID, ticketId);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareMapClienttickeNote(String clientId, String ticketType, String ticketId,
			String noteId) {
		Map map = new HashMap();
		map.put(clientID, clientId);
		map.put(ticket, ticketType);
		map.put(ticketID, ticketId);
		map.put(noteID, noteId);
		return map;
	}

	public static Map<String, String> prepareMapsmId(String clientid, String smid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("smId", smid);
		return map;
	}

	public static String getInternalAPIUri(String uri) {
		int lastocc = uri.lastIndexOf("v2");
		return uri.substring(0, lastocc);
	}

	public static Response postInternal(String accessToken, Object obj, String internalAPIUri,
			Map<String, String> map) {
		Response response = null;
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when().post(internalAPIUri).then()
						.extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when().post(internalAPIUri).then()
						.extract().response();
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when().post(internalAPIUri)
						.then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when().post(internalAPIUri)
						.then().extract().response();
			}
		}
		//AtuReports.info("The POST API response code is coming as  ", internalAPIUri,String.valueOf(response.statusCode()), response.asString());
		return response;
	}

	public static Map<String, String> prepareMapuuid(String uuid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uuid", uuid);
		return map;
	}

	public static Map<String, String> prepareMapuuidcheckid(String uuid, String checkId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("uuid", uuid);
		map.put("checkId", checkId);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> preparemapUserview(String tenantId, String userId, String viewId) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, tenantId);
		map.put("userId", userId);
		map.put("viewId", viewId);
		return map;
	}

	public static Map<String, String> prepareMapEntity(String id, String appId, String entityId, String entityName,
			String ts) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		map.put("appId", appId);
		map.put("entityId", entityId);
		map.put("entityName", entityName);
		map.put("ts", ts);
		return map;
	}

	public static Map<String, String> prepareMapEntitys(String id, String appId, String entityId, String entityName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		map.put("appId", appId);
		map.put("entityId", entityId);
		map.put("entityName", entityName);

		return map;
	}

	public static Map<String, String> prepareMapResponse(String id, String ticketId, String responseId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		map.put("ticketId", ticketId);
		map.put("responseId", responseId);
		return map;
	}

	public static Map<String, String> prepareMapticket(String id, String ticketId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, id);
		map.put("ticketId", ticketId);
		return map;
	}

	public static Response delete(String pathUri, Object obj, Map<String, String> map, String key, String secret) {
		Response response = null;
		ExtentTestManager.info("The complete API Path is "+ APIUri.apibaseUri + pathUri);
		ExtentTestManager.info("Method : DELETE");
		String accessToken = getAccessToken(key, secret);
		if (obj == null) {
			response = given().spec(postspecificationBuilder(accessToken)).when()
					.delete(APIUri.apibaseUri + pathUri, map).then().extract().response();
		} else {
			response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
					.delete(APIUri.apibaseUri + pathUri, map).then().extract().response();
		}
		if (response.statusCode() == 407) {
			setAccessToken();
			post(pathUri, obj, map);
		}
		//AtuReports.info("The POST API response code is coming as  ", APIUri.apibaseUri + pathUri,String.valueOf(response.statusCode()), "");
		return response;
	}

	private static RequestSpecification internalpostspecificationBuilder(String accessToken2) {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(getInternalAPIUri(APIUri.apibaseUri) + "basic");
		builder.addHeader("Authorization", "Basic " + accessToken2);
		builder.setContentType(ContentType.JSON);
		builder.setAccept(appJson);
		RestAssured.config().encoderConfig(
				EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}

	public static String getBasicAccessToken(Map<String, String> data) {
		String str = data.get("validateSaveAuditActivity") + ":" + data.get("Password");
		byte[] bytesEncoded = Base64.getEncoder().encode(str.getBytes());
		String token = new String(bytesEncoded);
		System.out.println("encoded value is " + new String(bytesEncoded));
		return token;
	}

	@SuppressWarnings("unchecked")
	public static Response getInternal(String accessToken, String pathUri, @SuppressWarnings("rawtypes") Map map) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", getInternalAPIUri(APIUri.apibaseUri) + pathUri, "", "");
		if (map == null) {
			response = given().spec((RequestSpecification) internalpostspecificationBuilder(accessToken)).when()
					.get(getInternalAPIUri(APIUri.apibaseUri) + pathUri).then().extract().response();
		}

		else {
			response = given().spec((RequestSpecification) internalpostspecificationBuilder(accessToken)).when()
					.get(getInternalAPIUri(APIUri.apibaseUri) + pathUri, map).then().extract().response();

		}
		System.out.println(response.getStatusCode() + " >>> " + response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri,String.valueOf(response.statusCode()), "");
		return response;
	}

	public static Map<String, String> prepareMapDisableComponentOfMetric(long tenant, String rtype, String res,
			String component, String metric) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, String.valueOf(tenant));
		map.put("rtype", rtype);
		map.put("resource", res);
		map.put("component", component);
		map.put("metric", metric);
		return map;
	}

	public static Map<String, String> prepareMapMetric(String tenant, String rtype, String res, String metric) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenant);
		map.put("rtype", rtype);
		map.put("resource", res);
		map.put("metric", metric);
		return map;
	}

	public static Map<String, String> prepareMapMetricGroup(String clientid, String metricgroup) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientid);
		map.put("metricGroup", metricgroup);
		return map;
	}

	public static Map<String, String> prepareMapMetricResource(String clientid, String rtype, String resource,
			String mid, long start, long end) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientid);
		map.put("rtype", rtype);
		map.put("resource", resource);
		map.put("mid", mid);
		map.put("startTime", String.valueOf(start));
		map.put("endTime", String.valueOf(end));
		return map;
	}

	public static Map<String, String> prepareMapMetricgroupandresourcegroup(String clientid, String metricgroup,
			String resourcegroup) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientid);
		map.put("metricGroup", metricgroup);
		map.put("resourceGroup", resourcegroup);
		return map;
	}

	public static Map<String, String> prepareMapRowKey(long clientid, String rowkey, long start, long end) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, String.valueOf(clientid));
		map.put("rowKey", rowkey);
		map.put("startDate", String.valueOf(start));
		map.put("endDate", String.valueOf(end));
		return map;
	}

	public static Map<String, String> prepareMapDownTimeValues(String clientid, String rtype, String resource) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientid);
		map.put("rtype", rtype);
		map.put("resource", resource);
		return map;
	}

	public static Map<String, String> prepareMapReport(String clientid, String uuid, String filename) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientid);
		map.put("uuid", uuid);
		map.put("filename", filename);
		return map;
	}

	public static Map<String, String> prepareGetResources(String orgId, Map<String, String> data) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenantId", orgId);
		map.put("queryString", data.get("queryString"));
		return map;
	}

	public static Map<String, String> prepareGetResourcesGroup(String orgId, Map<String, String> data) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateType3);
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenantId", orgId);
		map.put("startDate", sdf.format(new Date()));
		Date date = new Date();
		int month = date.getMonth();
		date.setMonth(month + 1);
		map.put("endDate", sdf.format(date));
		if (data.get("QueryString").contains("{clientId}")) {
			map.put("clientId", orgId);
		}
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Map<String, String> preparealert(String clientId, String alertId, String ticketId) {
		@SuppressWarnings("rawtypes")
		Map map = new HashMap();
		double d = Double.parseDouble(ticketId);
		long l = (long) d;
		map.put("clientId", clientId);
		map.put("alertId", alertId);
		map.put("ticketId", l);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Response getpathuri(String pathUri, @SuppressWarnings("rawtypes") Map map) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", pathUri, "", "");
		
		String accessToken = setAccessToken();
		if (map == null) {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when().get(pathUri).then()
					.extract().response();
		}

		else {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when().get(pathUri, map)
					.then().extract().response();

		}
		System.out.println(response.getStatusCode() + " >>> " + response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri, String.valueOf(response.statusCode()), "");
		return response;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareMapdwlattchmnt(String clientId, String payloadType, String id,
			String responseid, String resourceid) {
		Map map = new HashMap();
		long res = Long.parseLong(responseid);
		long resourceid1 = Long.parseLong(resourceid);
		map.put(clientID, clientId);
		map.put("payloadType", payloadType);
		map.put("id", id);
		map.put("responseId", res);
		map.put("resourceId", resourceid1);
		return map;
	}

	@SuppressWarnings("unchecked")
	public static Response getInternal(String pathUri, @SuppressWarnings("rawtypes") Map map, String clientKey,
			String clientSecret) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", pathUri, "", "");
		
		String accessToken = getAccessToken(clientKey, clientSecret);
		if (map == null) {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when()
					.get("https://sjchc-trunk.vistara.it/" + pathUri).then().extract().response();
		}

		else {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when()
					.get("https://sjchc-trunk.vistara.it/" + pathUri, map).then().extract().response();

		}
		System.out.println(response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri, String.valueOf(response.statusCode()), "");
		return response;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareMapchangeStatus(long clientid, String rtype, long resource,
			long mid) {
		Map map = new HashMap();
		map.put(clientID, clientid);
		map.put("rtype", rtype);
		map.put("resource", resource);
		map.put("mid", mid);
		return map;
	}

	public static Map<String, String> prepareMapticketId(String clientid, String ticketid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientid);
		map.put("ticketId", ticketid);
		return map;
	}

	public static Map<String, String> prepareMapPageNation(String srid, String pageno, String pagesize) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("sr_id", srid);
		map.put("pageNo", pageno);
		map.put("pageSize", pagesize);
		return map;
	}

	public static Map<String, String> prepareMapGatewayUUID(String gatewayuuid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("gatewayUUID", gatewayuuid);
		return map;
	}

	public static Map<String, String> prepareMapTenantId(String tenantid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenantId", tenantid);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareCustomSearchMap(String clientID, String pageNo, String pSize) {
		Map map = new HashMap<Object, Object>();
		map.put(tenantID, clientID);
		map.put(pageNO, pageNo);
		map.put(pageSize, pSize);
		return map;
	}

	public static Map<String, String> prepareMapScriptDemo(String clientid, String srvciname, String ip,
			String divName) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("serviceName", srvciname);
		map.put("deviceIp", ip);
		map.put("deviceName", divName);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareMapRespondTicketIds(String tenant, String ticket, String start, String end) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenant);
		map.put("ticketIds", ticket);
		map.put("startTime", start);
		map.put("endTime", end);
		return map;
	}

	public static Map<String, String> prepareMapImlinkActivitylist(String appid, String clientid, String pagesize) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("appId", appid);
		map.put(tenantID, clientid);
		map.put("pageSize", pagesize);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareGetJobMid(String tenantId, String id) {
		Map map = new HashMap();
		double d = Double.parseDouble(id);
		long l = (long) d;
		map.put(tenantID, tenantId);
		map.put("mid", l);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareGetJobMap(long clientId2, String id) {
		Map map = new HashMap();
		map.put(tenantID, clientId2);
		map.put("id", id);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> prepareMap(long id) {
		Map map = new HashMap();
		map.put(tenantID, id);
		return map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareMetricsByMetricID(String clientID, String rtype, String rID) {
		clientID = clientID.replaceAll("client_", "");
		clientID = clientID.trim();
		double clientId = Double.parseDouble(clientID);
		double resourceId = Double.parseDouble(rID);
		long clientIdl = (long) clientId;
		long resourceIdl = (long) resourceId;
		Map map = new HashMap();
		map.put("tenant", clientIdl);
		map.put("rtype", rtype);
		map.put("resource", resourceIdl);
		return map;
	}

	public static Map<String, String> prepareMapExtAlertIds(String clientid, String extaltid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("externalAlertID", extaltid);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareMapmid(String clientid, String mid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientid);
		map.put("mid", mid);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareMapResource(String clientid, String resourceId) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, clientid);
		map.put("resourceId", resourceId);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareMapConfigBackup(String clientid, String deviceid, String conftype) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("deviceId", deviceid);
		map.put("confType", conftype);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareMapPartnerName(String id, String name) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("name", name);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareMapAccount(String id, String account) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("accNo", account);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareWorkFlowMap(String tenantId, String ticketId, String status, String time) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("tenant", tenantId);
		map.put("ticketId", ticketId);
		map.put("resClkStatus", status);
		map.put("ts", time);
		return map;
	}

	public static Response delete(String pathUri, Object obj, Map<String, String> map) {
		Response response = null;
		
		String accessToken = setAccessToken();
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.delete(APIUri.apibaseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.delete(APIUri.apibaseUri + pathUri, map).then().extract().response();
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.delete(APIUri.apibaseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.delete(APIUri.apibaseUri + pathUri, map).then().extract().response();
			}
		}
		if (response.statusCode() == 407) {
			setAccessToken();
			delete(pathUri, obj, map);
		}
		//AtuReports.info(APIUri.apibaseUri ,pathUri,String.valueOf(response.statusCode()), "");
		return response;
	}
	
	public static Response deleteWithKeys(String pathUri, Object obj, Map<String, String> map, String key, String secret) {
		Response response = null;
		
		String accessToken = getAccessToken(key, secret);
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.delete(APIUri.apibaseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.delete(APIUri.apibaseUri + pathUri, map).then().extract().response();
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.delete(APIUri.apibaseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.delete(APIUri.apibaseUri + pathUri, map).then().extract().response();
			}
		}
		if (response.statusCode() == 407) {
			setAccessToken();
			delete(pathUri, obj, map);
		}
		//AtuReports.info(APIUri.apibaseUri ,pathUri,String.valueOf(response.statusCode()), "");
		return response;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareMapreport(String clientid, String tikttype) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("ticketType", tikttype);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareMapResourceExternalUUID(String s1, String s2) {
		Map<String,String> map=new HashMap<String,String>();
		map.put("cloudProviderUniqueId",s1);
		map.put("resourceExternalUUID", s2);
		return map;
	}
	
	public static Map<String, String> prepareCustomReportRunMap(String tenantId,String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(tenantID, tenantId);
		map.put("id", id);
		return map;
	}
	
	public static Map<String, String> prepareCustomReportDeleteFolder(String id) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		return map;
	}

	@SuppressWarnings("rawtypes")
	public static Map prepareTimeSeriesMap(String clientid, String metric, String deviceid) {
		Map<String, String> map = new HashMap<String, String>();
		map.put(clientID, clientid);
		map.put("type", metric);
		map.put("resourceId", deviceid);
		return map;
	}
	
	@SuppressWarnings("rawtypes")
	public static Map prepareDecomMap(String mspID, String action) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("mspId", mspID);
		map.put("module", "EXTENDEDDATARETENTION");
		map.put("action", action);
		return map;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map prepareMapActivityLog(String client, String entity, String id) {
		Map map = new HashMap();
		map.put("clientId", client);
		map.put("entityName", entity);
		map.put("entityId", id);
		return map;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Map<String, String> preparemapML(String clientid, String filename) {
		Map map = new HashMap();
		map.put("clientId", clientid);
		map.put("fileId", filename);
		return map;
	}
	
	private static RequestSpecification postspecificationBuilderForWebHooks() {

		RequestSpecBuilder builder = new RequestSpecBuilder();
		builder.setBaseUri(APIUri.baseUri);
		builder.setContentType(ContentType.JSON);
		builder.setAccept(appJson);
		RestAssuredConfig config = RestAssured.config()
		        .httpClient(HttpClientConfig.httpClientConfig()
		                .setParam(CoreConnectionPNames.CONNECTION_TIMEOUT, timeout)
		                .setParam(CoreConnectionPNames.SO_TIMEOUT, timeout));
		
		
		config.encoderConfig(
				EncoderConfig.encoderConfig().appendDefaultContentCharsetToContentTypeIfUndefined(false));
		RestAssured.config().getSSLConfig().relaxedHTTPSValidation();
		RestAssured.useRelaxedHTTPSValidation();
		return builder.build();
	}
	
	public static Response postforwebhooks(String pathUri, Object obj, Map<String, String> map) {
		Response response = null;
		System.out.println(APIUri.baseUri + pathUri);
		//String accessToken = setAccessToken();
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilderForWebHooks()).when().post(APIUri.baseUri + pathUri)
						.then().extract().response();
			} else {
				response = given().spec(postspecificationBuilderForWebHooks()).when()
						.post(APIUri.baseUri + pathUri, map).then().extract().response();
				ExtentTestManager.info("Url replaced data : "+map.toString());
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilderForWebHooks()).body(obj).when()
						.post(APIUri.baseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilderForWebHooks()).body(obj).when()
						.post(APIUri.baseUri + pathUri, map).then().extract().response();
				ExtentTestManager.info("Url replaced data : "+map.toString());
			}
		}
		System.out.println(response.asString());
		ExtentTestManager.getTest().info(APIUri.baseUri+pathUri);
		ExtentTestManager.getTest().info("Method : POST");
		ExtentTestManager.getTest().info("ResponseCode : "+response.statusCode());
		ExtentTestManager.getTest().info("Response Message :"+response.asString());
		//AtuReports.info(APIUri.apibaseUri ,  pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
	public static Response postForIntegrations(String pathUri, Object obj, Map<String, String> map) {
		Response response = null;
		System.out.println(APIUri.baseUri + pathUri);
		String accessToken = setAccessToken();
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when().post(APIUri.baseUri + pathUri)
						.then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.post(APIUri.baseUri + pathUri, map).then().extract().response();
				ExtentTestManager.info("Url replaced data : "+map.toString());
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.post(APIUri.baseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.post(APIUri.baseUri + pathUri, map).then().extract().response();
				ExtentTestManager.info("Url replaced data : "+map.toString());
			}
		}
		System.out.println(response.asString());
		ExtentTestManager.getTest().info(APIUri.baseUri+pathUri);
		ExtentTestManager.getTest().info("Method : POST");
		ExtentTestManager.getTest().info("ResponseCode : "+response.statusCode());
		ExtentTestManager.getTest().info("Response Message :"+response.asString());
		//AtuReports.info(APIUri.apibaseUri ,  pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
	public static Response patchForIntegrations(String pathUri, Object obj, Map<String, String> map) {
		Response response = null;
		System.out.println(APIUri.baseUri + pathUri);
		String accessToken = setAccessToken();
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when().patch(APIUri.baseUri + pathUri)
						.then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.patch(APIUri.baseUri + pathUri, map).then().extract().response();
				ExtentTestManager.info("Url replaced data : "+map.toString());
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.patch(APIUri.baseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.patch(APIUri.baseUri + pathUri, map).then().extract().response();
				ExtentTestManager.info("Url replaced data : "+map.toString());
			}
		}
		System.out.println(response.asString());
		ExtentTestManager.getTest().info(APIUri.baseUri+pathUri);
		ExtentTestManager.getTest().info("Method : PATCH");
		ExtentTestManager.getTest().info("ResponseCode : "+response.statusCode());
		ExtentTestManager.getTest().info("Response Message :"+response.asString());
		//AtuReports.info(APIUri.apibaseUri ,  pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
	@SuppressWarnings("unchecked")
	public static Response getForIntegrations(String pathUri, @SuppressWarnings("rawtypes") Map map) {
		Response response = null;
		//AtuReports.info("The complete API Path is ", APIUri.apibaseUri + pathUri, "", "");
		ExtentTestManager.info("The complete API Path is "+ APIUri.baseUri + pathUri);
		
		String accessToken = setAccessToken();
		if (map == null) {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when()
					.get(APIUri.baseUri + pathUri).then().extract().response();
		}

		else {
			response = given().spec((RequestSpecification) specificationBuilder(accessToken)).when()
					.get(APIUri.baseUri + pathUri, map).then().extract().response();
			ExtentTestManager.info("Url replaced data : "+map.toString());
		}
		ExtentTestManager.getTest().info("Method : GET");
		
		System.out.println(
				APIUri.baseUri + pathUri + " >> " + response.getStatusCode() + " >>> " + response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		ExtentTestManager.info(String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
	public static Response deleteForIntegrations(String pathUri, Object obj, Map<String, String> map) {
		Response response = null;
		
		String accessToken = setAccessToken();
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.delete(APIUri.baseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when()
						.delete(APIUri.baseUri + pathUri, map).then().extract().response();
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.delete(APIUri.baseUri + pathUri).then().extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when()
						.delete(APIUri.baseUri + pathUri, map).then().extract().response();
			}
		}
		if (response.statusCode() == 407) {
			setAccessToken();
			delete(pathUri, obj, map);
		}
		//AtuReports.info(APIUri.apibaseUri ,pathUri,String.valueOf(response.statusCode()), "");
		return response;
	}
	
	public static Response putForIntegrations(String pathUri, Object obj, Map<String, String> map) {
		Response response = null;
		
		String accessToken = setAccessToken();
		if (obj == null) {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).when().put(pathUri).then().extract()
						.response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).when().put(pathUri, map).then().extract()
						.response();
			}

		} else {

			if (map == null) {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when().put(pathUri).then()
						.extract().response();
			} else {
				response = given().spec(postspecificationBuilder(accessToken)).body(obj).when().put(pathUri, map).then()
						.extract().response();
			}
		}
		ExtentTestManager.getTest().info(APIUri.baseUri+pathUri);
		ExtentTestManager.getTest().info("Method : PUT");
		ExtentTestManager.getTest().info("ResponseCode : "+response.statusCode());
		ExtentTestManager.getTest().info("Response Message :"+response.asString());
		//AtuReports.info(APIUri.apibaseUri , pathUri,"200", String.valueOf(response.statusCode())+response.asString());
		return response;
	}
	
}