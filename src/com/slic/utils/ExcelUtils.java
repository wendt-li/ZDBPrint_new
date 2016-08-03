/*
 * 文 件 名:  ExcelUtils.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月21日
 */
package com.slic.utils;

import java.io.File ;
import java.io.FileOutputStream ;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.ArrayList ;
import java.util.Arrays;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月21日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ExcelUtils
{
    public void createExcel(String[] headers,String[] headerColumns,List<Object> datas,OutputStream out){
        createExcel("sheet0",headers,headerColumns,datas,out);
    }
    public void createExcel(String sheetName,String[] headers,String[] headerColumns,List<Object> datas,OutputStream out){
        createExcel(sheetName,Arrays.asList(headers),Arrays.asList(headerColumns),datas,out);
        
    }
    
    public void createExcel(String sheetName,List<String> headers,List<String> headerColumns,List<Object> datas,OutputStream out){
     // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(sheetName);
        // 创建表头
        HSSFRow headRow = sheet.createRow(0);
        for(int i=0;i<headers.size();i++){
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(headers.get(i));
        }
        
        // 循环生成内容
        for(int i=0;i<datas.size();i++){
            HSSFRow bodyRow = sheet.createRow(i + 1);
            Object obj = datas.get(i);
            for (int j = 0; j < headerColumns.size(); j++) {
                Object value = getFieldValueByName(headerColumns.get(j),obj);
                bodyRow.createCell(j).setCellValue(value.toString());
            }
        }
        
        try
        {
            // 输出excel
            workbook.write(out);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
    }
    
    /**
     * @param string
     * @param obj
     * @return
     */
    private Object getFieldValueByName(String fieldName, Object obj) {
        String firstLetter = fieldName.substring(0,1).toUpperCase();
        String getter = "get" +firstLetter + fieldName.substring(1);
        try {
            Method method = obj.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(obj, new Object[]{});
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } 
    }
    
    public static void main(String[] args) throws Exception {
		File file = new File("d:/text.xls");
		FileOutputStream fos = new FileOutputStream(file);
		ExcelUtils eu = new ExcelUtils();
		String[] header = {"1","2","3"};
		String[] columnType = {"a","b","c"};
		List data = new ArrayList();
		data.add(new A("1","2","3"));
		data.add(new A("1","2","3"));
		data.add(new A("1","2","3"));
		data.add(new A("1","2","3"));
		eu.createExcel(header, columnType, data, fos);
	}
}

class A{
	private String a;
	private String b;
	private String c;
	public A(String a,String b,String c){
		this.a = a;
		this.b = b;
		this.c = c;
	}
	/**
	 * @return 返回 a
	 */
	public String getA() {
		return a ;
	}
	/**
	 * @param 对a进行赋值
	 */
	public void setA(String a) {
		this.a = a ;
	}
	/**
	 * @return 返回 b
	 */
	public String getB() {
		return b ;
	}
	/**
	 * @param 对b进行赋值
	 */
	public void setB(String b) {
		this.b = b ;
	}
	/**
	 * @return 返回 c
	 */
	public String getC() {
		return c ;
	}
	/**
	 * @param 对c进行赋值
	 */
	public void setC(String c) {
		this.c = c ;
	}
	
}
