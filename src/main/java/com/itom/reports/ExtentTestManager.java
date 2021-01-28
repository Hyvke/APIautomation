package com.itom.reports;

import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.itom.base.BaseClass;

public class ExtentTestManager {
	static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
	static ExtentReports extent = ExtentManager.getInstance();
	static ExtentTest parentTest;
	static String methodname="";
	static boolean flag = false;

	public static synchronized ExtentTest getTest() {
		return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
	}

	public static synchronized void endTest() {
		extent.flush();
	}
	
	public static synchronized ExtentTest startParentTest(String testName) {
		parentTest = extent.createTest(testName);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), parentTest);
		
		return parentTest;
	}

	public static synchronized ExtentTest startTest(String testName) {
		ExtentTest test = parentTest.createNode(testName);
		extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
		BaseClass.AddTotalTestcases();
		return test;
	}
	
	public static void passResults(String desc,String Result) {
		getTest().log(Status.PASS, desc);
		if(!getTest().getModel().getName().equals(methodname)) {
			methodname=getTest().getModel().getName();
			BaseClass.Totalpassed();
			flag=true;
		}
	}
	
	public static void failResults(String desc,String Result) {
		getTest().log(Status.FAIL, desc);
		if(getTest().getModel().getName().equals(methodname) && flag==true) {
			methodname=getTest().getModel().getName();
			BaseClass.Totalfailed();
			flag=false;
			BaseClass.deletepassed();
		}
		if(!getTest().getModel().getName().equals(methodname)) {
			methodname=getTest().getModel().getName();
			BaseClass.Totalfailed();
		}
	}
	
	public static void info(String desc) {
		getTest().log(Status.INFO, desc);
	}
	
	public static void warning(String desc) {
		getTest().log(Status.WARNING, desc);
	}
}
