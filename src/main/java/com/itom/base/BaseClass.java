package com.itom.base;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.itom.reports.ExtentManager;
import com.itom.reports.ExtentTestManager;

public class BaseClass {

	public static Properties context = null;
	public static String propFileLocation;
	public static long TOTAL_TIME_TAKEN=0;
	private static String methodName = "";
	private static String beforemethodname="";
	private static int TotalTestCases=0;
	private static int TotalPassed=0;
	private static int TotalFailed=0;
	
	public static void Totalfailed() {
		TotalFailed=TotalFailed+1;
	}
	
	public static void Totalpassed() {
		TotalPassed = TotalPassed +1;
	}
	
	public static void AddTotalTime(long time) {
		TOTAL_TIME_TAKEN = TOTAL_TIME_TAKEN+time;
	}
	
	public static void AddTotalTestcases() {
		TotalTestCases = TotalTestCases+1;
	}
	
	public static void deletepassed() {
		TotalPassed = TotalPassed-1;
	}
	
	public static String getProperty(String key){
		FileReader reader;
		Properties p=new Properties(); 
		try {
			reader = new FileReader(System.getProperty("user.dir")+"/src/test/resources/PropertiesFromResponse.properties");
		    p.load(reader); 
		} catch (Exception e) {
			System.out.println("error : "+e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    context=p;
	    return (String) p.get(key);
	}
	
	public static String getConstant(String key) {
		FileReader reader;
		Properties p=new Properties(); 
		try {
			reader = new FileReader(System.getProperty("user.dir")+"/src/test/resources/shivalik.properties");
		    p.load(reader); 
		} catch (Exception e) {
			System.out.println(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
	    return (String) p.get(key);
	}
	
	@BeforeSuite(alwaysRun = true)
	public void beforeSuite() throws Exception {
		errorFilecreation();
		String filename = System.getProperty("user.dir")+"/src/test/resources/PropertiesFromResponse.properties";
		File file = new File(filename);
		boolean filecreation = file.createNewFile();
		if(!filecreation) {
			boolean b = file.delete();
			System.out.println("Properties File deleted :" +b);
		}
		try {
			 
			ExtentManager.getInstance();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void errorFilecreation() {
		String filename = System.getProperty("user.dir")+"/src/test/resources/genuine.txt";
		String filename2= System.getProperty("user.dir")+"/src/test/resources/AutomationFailures.txt";
		String filename3= System.getProperty("user.dir")+"/src/test/resources/notsaved.txt";
		File file = new File(filename);
		File file2 = new File(filename2);
		File file3 = new File(filename3);
		try {
		boolean filecreation = file.createNewFile();
		boolean filecreation2 = file2.createNewFile();
		boolean filecreation3 = file3.createNewFile();
		
		if(!filecreation) {
			boolean b = file.delete();
			System.out.println("genuineFailures File deleted :" +b);
			filecreation = file.createNewFile();
		}

		if(!filecreation2) {
			boolean b = file2.delete();
			System.out.println("AutomationFailures File deleted :" +b);
			filecreation2 = file2.createNewFile();
		}
		
		if(!filecreation3) {
			boolean b = file3.delete();
			System.out.println("notsaved File deleted :" +b);
			filecreation3 = file3.createNewFile();}
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}

	@DataProvider(name = "ITOMLoginCredentials")
	public Iterator<Object[]> dataprovider2(Method method) {
		return new ExcelService().readTestDataFromExcel("ITOMTestData.xlsx",
				"TestData", method.getName());
	}
	
	@BeforeMethod
	public void setmeth(Method method) {
		beforemethodname=method.getName();
	}
	
	@BeforeTest(alwaysRun = true)
	public void setUpTest()
	{
		if(StringUtils.isEmpty(System.getProperty("env"))) {
			propFileLocation =System.getProperty("user.dir")+"/src/test/resources/";
		}
		else {
		propFileLocation = System.getProperty("env").substring(7, System.getProperty("env").lastIndexOf("/"));
		}
		System.out.println("final property location >>>"+propFileLocation);
	}
	
	@AfterMethod
	public void aftermeth(ITestResult result,Method method) {
		//System.out.println("retryCount : "+result.getAttribute("retryCount").toString());
		System.out.println("status : "+result.getStatus());
		 if(result.getStatus()==ITestResult.SKIP) {
			 ExtentManager.getInstance().removeTest(ExtentTestManager.getTest());
			 return;
		 }
		 
		 ExtentTestManager.endTest();
		 setMethodName(method.getName());
	}
	
	 private Date getTime(long millis) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(millis);
	        return calendar.getTime();
	    }
	
	@AfterSuite
	public void set() {
		TOTAL_TIME_TAKEN = TimeUnit.MILLISECONDS.toMinutes(TOTAL_TIME_TAKEN);

		/*sendMail(System.getProperty("user.dir")+"/src/test/resources/genuine.txt",getConstant("AllEmails"));
		sendMail(System.getProperty("user.dir")+"/src/test/resources/AutomationFailures.txt",getConstant("AutomationEmails"));*/
	}
	
	public static String getCurrentDirectory(){
	    return System.getProperty("user.dir")+"/JsonFiles";
	}
	
	public static String getDateFormat(String vDateFormat) {

		Date vDate = Calendar.getInstance().getTime();
		SimpleDateFormat sdf = new SimpleDateFormat(vDateFormat);
		return sdf.format(vDate);

	}

	public static String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public static void sendMail(String filePath,String emails) {
		  Properties props = new Properties();
	      props.put("mail.smtp.auth", "true");
	      props.put("mail.smtp.starttls.enable", "true");
	      props.put("mail.smtp.host", "smtp.gmail.com");
	      props.put("mail.smtp.port", "587");
	      props.put("mail.debug", "true");
	      Session session = Session.getInstance(props,
	  		  new Authenticator() {
	  			protected PasswordAuthentication getPasswordAuthentication() {
	  				return new PasswordAuthentication("ramadurga.vantakula@opsramp.com", "Konda@946");
	  			}
	  		  });
	      File file = new File(filePath); 
	      try {
	      BufferedReader br = new BufferedReader(new FileReader(file)); 
	      
	      String st; 
	      int count=0;
	      String content = "";
	      while ((st = br.readLine()) != null) {
	    	  System.out.println("while coming");
	        String[] list = st.split("\t\t");
	        System.out.println("size is : "+list.length);
	        for(int i=0;i<list.length;i++) {
	        	System.out.println(list[i]);
	        }
	        content = content +
	        		"  <tr>\r\n" + 
	    			"    <td class=\"error\">"+list[0]+"</td>\r\n" + 
	    			"    <td class=\"error\">"+list[1]+"</td>\r\n" + 
	    			"    <td class=\"error\">"+list[2]+"</td>\r\n" + 
	    			"    <td class=\"error\">"+list[3]+"</td>\r\n" + 
	    			"  </tr>\r\n";
	        count++;
	       
	      }
	     
		        MimeMessage msg = new MimeMessage(session);
		        msg.setFrom(new InternetAddress("no-reply@opsramp.com"));
		        msg.setRecipients(Message.RecipientType.TO, emails);
		        msg.setSubject("Failures in APIAutomation");
		        msg.setSentDate(new Date());
		        
		        Multipart multipart = new MimeMultipart();
		        
		        MimeBodyPart htmlPart = new MimeBodyPart();
		        
		        htmlPart.setContent(HTMLtemplate(content,count), "text/html");
		        multipart.addBodyPart(htmlPart);
		        msg.setContent(multipart);
		        Transport.send(msg);
		        System.out.println("---Done---");
	       } catch (Exception ex) {
	    	    ex.printStackTrace();
	    	    System.out.println("mail error is : "+ex.getMessage());
	       }
	}
	
	public static String HTMLtemplate(String content,int count) {
		String htmlContent = "<html>  \r\n" + 
				"					<head>  \r\n" + 
				"					<style>  \r\n" + 
				"					table.error {  \r\n" + 
				"					  font-family: arial, sans-serif;  \r\n" + 
				"					  border-collapse: collapse;  \r\n" + 
				"					  width: 100%;  \r\n" + 
				"					  background-color:#ffe6ee;\r\n" + 
				"					}  \r\n" + 
				"					  \r\n" + 
				"					td.details{\r\n" + 
				"						font-weight: bold;\r\n" + 
				"					}\r\n" + 
				"					td.error{  \r\n" + 
				"					  border: 2.5px solid #7070db;  \r\n" + 
				"					  text-align: left;  \r\n" + 
				"					  padding: 8px;  \r\n" + 
				"					  width:25%  \r\n" + 
				"					}  \r\n" + 
				"					  \r\n" + 
				"					th.error{  \r\n" + 
				"					  border: 2.5px solid #7070db;  \r\n" + 
				"					  text-align: left;  \r\n" + 
				"					  padding: 8px;   \r\n" + 
				"					  background-color:#c2d6d6;\r\n" + 
				"					  font-weight: bold;\r\n" + 
				"					}  \r\n" + 
				"					  \r\n" + 
				"					tr:nth-child(even).error {  \r\n" + 
				"					  background-color: #dddddd;  \r\n" + 
				"					}  \r\n" + 
				"					table.details {  \r\n" + 
				"					  font-family: arial, sans-serif;  \r\n" + 
				"					  border-collapse: collapse;  \r\n" + 
				"					  width: 100%;  \r\n" + 
				"					}  \r\n" + 
				"					 \r\n" + 
				"					</style>  \r\n" + 
				"					</head>  \r\n" + 
				"					<body>  \r\n" + 
				"					  \r\n" + 
				"					<center><h2 style=\\color:blue;font-size:250%;background-color:orange;\\>APIAutomation Failures Report</h2></center>\r\n" + 
				"					<table class=\"details\">\r\n" + 
				"						<tr>\r\n" + 
				"							<td class=\"details\">Application : APIAutomation</td>\r\n" + 
				"							<td class=\"details\">Branch : "+System.getProperty("branch")+"</td>\r\n" + 
				"							<td class=\"details\">Environment : "+getConstant("opsRamp_web_url")+"</td>\r\n" + 
				"						</tr>\r\n" + 
				"						<tr>\r\n" + 
				"							<td class=\"details\">Date : "+getDate()+"</td>\r\n" + 
				"							<td class=\"details\" >Build : "+System.getProperty("build")+"</td>\r\n" + 
				"							<td class=\"details\">ExecutionTime : "+TOTAL_TIME_TAKEN+"mins</td>\r\n" + 
				"						</tr>\r\n" + 
				"						<tr>\r\n" + 
				"							<td class=\"details\">TestCases Executed : "+TotalTestCases+"</td>\r\n" + 
				"							<td class=\"details\">Passed : "+TotalPassed+"</td>\r\n" + 
				"							<td class=\"details\">Failed : "+TotalFailed+"</td>\r\n" + 
				"						</tr>\r\n" + 
				"					</table>\r\n" + 
				"					<br><br>\r\n" + 
				"					  \r\n" + 
				"					<table class=\"error\" > \r\n" + 
				"					  <tr class=\"error\">  \r\n" + 
				"					    <th style=\\background-color:#00b386\\ class=\"error\">TestCaseName</th>  \r\n" + 
				"					    <th style=\\background-color:#ff4d4d\\ class=\"error\">Test Steps</th>  \r\n" + 
				"					    <th style=\\background-color:#ffd11a\\ class=\"error\">Test Data</th>  \r\n" + 
				"						 <th style=\\background-color:#ffd11a\\ class=\"error\">Failure</th>  \r\n" + 
				"					  </tr>  \r\n" + 
										content+
				"					</table>  \r\n" + 
				"					  \r\n" + 
				"					</body>  \r\n" + 
				"					</html>";
		
		return htmlContent;
	}
	
	public static String getDate() {
		 DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy"); 
		   LocalDateTime now = LocalDateTime.now();  
		   String date = dtf.format(now); 
		   return date;
	}
	
}
