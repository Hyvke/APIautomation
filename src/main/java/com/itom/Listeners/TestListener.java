package com.itom.Listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.itom.api.APIUtil;
import com.itom.base.BaseClass;
import com.itom.reports.ExtentManager;
import com.itom.reports.ExtentTestManager;

public class TestListener implements ITestListener {

	String prev="";
	public void onStart(ITestContext context) {
		System.out.println("*** Test Suite " + context.getName() + " started ***");
	}

	public void onFinish(ITestContext context) {
		System.out.println(("*** Test Suite " + context.getName() + " ending ***"));
		ExtentTestManager.endTest();
		ExtentManager.getInstance().flush();
	}

	public void onTestStart(ITestResult result) {
		System.out.println("*** Running test method " + BaseClass.getMethodName());
		System.out.println(("*** Running test method " + result.getMethod().getMethodName() + "..."));
		//ExtentTest test = ExtentTestManager.startParentTest(result.getMethod().getMethodName());
		
		  if(!BaseClass.getMethodName().equals(result.getMethod().getMethodName())) {
		  ExtentTest test =ExtentTestManager.startParentTest(result.getMethod().getMethodName());
		  
		  }
		 
	}

	public void onTestSuccess(ITestResult result) {
		System.out.println("*** Executed " + result.getMethod().getMethodName() + " test successfully...");
		//ExtentTestManager.getTest().log(Status.PASS, "Test passed");
		long time = ExtentTestManager.getTest().getModel().getRunDurationMillis();
		BaseClass.AddTotalTime(time);
		System.out.println("Time taken = "+time);
	}

	public void onTestFailure(ITestResult result) {
		System.out.println("*** Test execution " + result.getMethod().getMethodName() + " failed...");
		ExtentTestManager.getTest().log(Status.FAIL, "exception is :"+result.getThrowable()+"\n error : "+result.toString());
		ExtentTestManager.getTest().log(Status.FAIL, "Test Failed");
		BaseClass.Totalfailed();
		APIUtil.writeUsingFiles(result.getMethod().getMethodName(),"see","see",result.toString(),"AutomationFailures.txt");
		long time = ExtentTestManager.getTest().getModel().getRunDurationMillis();
		BaseClass.AddTotalTime(time);
		System.out.println("Time taken = "+time);
	}

	public void onTestSkipped(ITestResult result) {
		System.out.println("*** Test " + result.getMethod().getMethodName() + " skipped...");
		ExtentTestManager.getTest().log(Status.SKIP, "Test Skipped");
	}

	public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
		System.out.println("*** Test failed but within percentage % " + result.getMethod().getMethodName());
	}

}