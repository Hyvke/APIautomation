package com.itom.base;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import com.itom.api.APIUtil;

public class ExcelService {
	public Iterator<Object[]> readTestDataFromExcel(String sWorkBookName,String sWorkbookSheetName,String tableName) {
    	String testDataFolderPath = BaseClass.propFileLocation; //System.getProperty("user.dir")+"\\"+(String)BaseClass.context.getBean("root_folder")
    	try {

    		if(new File(testDataFolderPath).isDirectory())
    		{
    			testDataFolderPath = BaseClass.propFileLocation;
    		}
    		else {
    			System.out.println("couldn't find filepath. Reading from project base dir");
    			testDataFolderPath =System.getProperty("user.dir")+"/src/test/resources";
    		}
    	
    	}
    	catch(Exception e) {
    		System.out.println("Exception ->>> couldn't find filepath. Reading from project base dir");
    		testDataFolderPath =System.getProperty("user.dir")+"/src/test/resources";
    	}
        
        List<HashMap<String, String>> testData = null;
        for (File testDataFile : new File(testDataFolderPath).listFiles()) {
            if (!testDataFile.getAbsolutePath().contains("$")) {
                FileInputStream file = null;
                if(testDataFile.getPath().contains(sWorkBookName)){
                	try {
                    file = new FileInputStream(testDataFile);
                } catch (FileNotFoundException e) {
                }
                Workbook workBook = null;
                try {
                    workBook = WorkbookFactory.create(file);
                }  catch (Exception e) {
                }
                Sheet testDataSheet = workBook.getSheet(sWorkbookSheetName);
               testData = getTestDataBySheet(testDataSheet,tableName);
            }
        }
         
        }
        return getIterator(testData);
    }
	
	 public Iterator<Object[]> getIterator(List<HashMap<String, String>> testDataList) {
	        List<Object[]> iteratorList = new ArrayList<Object[]>();
	        for (Map<String, String> map : testDataList) {
	            iteratorList.add(new Object[]{map});
	        }

	        return iteratorList.iterator();

	    }
	
	 public List<HashMap<String, String>> getTestDataBySheet(Sheet testDataSheet,String tableName) {
	        List<HashMap<String, String>> testData = new ArrayList<HashMap<String, String>>();
	        int startRow,endRow;
	        startRow= getStartRow(testDataSheet,tableName);
	        List<String> headers = getHeaders(testDataSheet,startRow+1);
	        endRow=getEndRow(testDataSheet,tableName,startRow);
	        for (int i = startRow+2; i < endRow; i++) {
	            HashMap<String, String> map = new HashMap<String, String>();
	            Row dataRow = testDataSheet.getRow(i);
	            Cell dataCell;
	            for (int j=1;j<headers.size();j++) {
	                if(!headers.get(j).equals("CELL NOT FOUND") && !headers.get(j).equals("MISSING CONTENT")){
	                try {
	                	dataCell = dataRow.getCell(j,Row.RETURN_NULL_AND_BLANK);
	                	dataCell.setCellType(Cell.CELL_TYPE_STRING);
	                	if(("".equals(dataCell.getStringCellValue().trim()))) {
	                		map.put(headers.get(j), " ");
	                	}
	                    if(dataCell != null && !("".equals(dataCell.getStringCellValue().trim()))){
	                        String cellvalue = APIUtil.replaceVariables(""+dataCell.getStringCellValue().trim());
	                    map.put(headers.get(j), cellvalue);
	                    
	                    }
	                } catch (Exception e) {
	                	e.printStackTrace();
	                }
	                }
	            }
	            testData.add(map);

	        }
	        return testData;
	    }
	 
	 public int getStartRow(Sheet testDataSheet, String tableName) {
			for (int i=0 ; i<testDataSheet.getLastRowNum()+1; i++){
				try{
					if(testDataSheet.getRow(i).getCell(0).getStringCellValue().toString().equalsIgnoreCase(tableName))
				return i;
				}catch(Exception e){
				}
			}
			return 0;
		}
	 
	 public List<String> getHeaders(Sheet sheet,int row) {

	        List<String> headers = new ArrayList<String>();
	        Row headerRow = sheet.getRow(row);
	        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
	            Cell dataCell = headerRow.getCell(i,Row.RETURN_NULL_AND_BLANK);
	            if(dataCell == null)
	            	headers.add("CELL NOT FOUND");
	            else if("".equals(dataCell.getStringCellValue().trim()))
	            	headers.add("MISSING CONTENT");
	            else{ 
	            dataCell.setCellType(Cell.CELL_TYPE_STRING);
	            headers.add(dataCell.getStringCellValue());
	            }
	        }
	        return headers;

	    }
	 
	 private int getEndRow(Sheet testDataSheet, String tableName, int startRow) {
			for (int i=startRow+1 ; i<testDataSheet.getLastRowNum()+1; i++){
				try{
				if(testDataSheet.getRow(i).getCell(0).getStringCellValue().toString().equalsIgnoreCase(tableName))
				return i;
				}catch(Exception e){
					
				}
			}
			return startRow;
		}
}
