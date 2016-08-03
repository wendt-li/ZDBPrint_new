/*
 * 文 件 名:  ExcelUtils.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月21日
 */
package com.slic.utils;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFooter;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;

import com.slic.adapter.ProtocalAdapterInterface;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月21日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ExcelUtils1
{
    
    private static ProtocalAdapterInterface protocalAdapterInterface;
    
    static{
        try {
            protocalAdapterInterface = (ProtocalAdapterInterface)Class.forName("com.slic.adapter.ProtocalAdapterBase").newInstance();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
   /**
    * <一句话功能简述>
    * <功能详细描述>
    * @param sheetName
    * @param printPattern
    * @param keyPathMap
    * @param source
    * @param out
    * @see [类、类#方法、类#成员]
    */
    public static void createExcel(String sheetName,String printPattern,Map<String, String> keyPathMap,String source,OutputStream out){
     // 创建工作薄
        HSSFWorkbook workbook = new HSSFWorkbook();
        // 创建sheet
        HSSFSheet sheet = workbook.createSheet(sheetName);
        
        try{
            JSONObject json = JSONObject.fromObject(printPattern);
            // 获取文本框信息
            JSONArray text = json.getJSONArray("text");
            for(int i=0;i<text.size();i++){
                JSONObject tempObj = text.getJSONObject(i);
                int yPath = Integer.parseInt(tempObj.getString("yPath"));
                createCell(workbook,sheet,tempObj,yPath);
            }
            
            // 表格开始行号
            int beginRowNum = -1;
            // 当前输出行号
            int printRowNum = -1;
            
            // 获取表格信息
            JSONArray grid = json.getJSONArray("grid");
            for(int i=0;i<grid.size();i++){
                JSONObject tempObj = grid.getJSONObject(i);
                // 获取表格开始位置
                JSONObject gridParent = tempObj.getJSONObject("parentMsg");
                // 行列
                int xPath = Integer.parseInt(gridParent.getString("xPath"));
                int yPath = Integer.parseInt(gridParent.getString("yPath"));
                // 表头行
                HSSFRow gridHeaderRow = sheet.getRow(yPath);
                if(gridHeaderRow == null){
                    gridHeaderRow = sheet.createRow(yPath);
                }
                
                // 记录开始行号
                if(beginRowNum < 0){
                    beginRowNum = yPath;
                }
                
                // 表头信息
                JSONArray headers = tempObj.getJSONArray("header");
                for(int j=xPath,k=0;k<headers.size();k++){
                    JSONObject header = headers.getJSONObject(k);
                    HSSFCell cell = gridHeaderRow.getCell(j+k);
                    if(cell == null){
                        cell = gridHeaderRow.createCell(j+k);
                    }
                    // 设置单元格样式/内容
                    setCellStyle(header,workbook,cell);
                }
                
                // 表体信息
                JSONArray bodys = tempObj.getJSONArray("body");
                // 获取 路径
                String path = keyPathMap.get("grid");
                JSONArray listJson = (JSONArray)protocalAdapterInterface.getParamValue(path, source);
                
                for(int data=0;data<listJson.size();data++){
                    printRowNum = yPath+data+1;
                    // 表体
                    HSSFRow gridBodyRow = sheet.getRow(printRowNum);
//                    if(gridBodyRow != null){
//                     // 记录已写行
//                        hasPrintRows.add(gridBodyRow);
//                    }
                    
                    gridBodyRow = sheet.createRow(printRowNum);
                    
                    // 取出单条记录
                    JSONObject singleData = listJson.getJSONObject(data);
                    
                    for(int j=xPath,k=0;k<bodys.size();k++){
                        JSONObject body = bodys.getJSONObject(k);
                        HSSFCell cell = gridBodyRow.getCell(j+k);
                        if(cell == null){
                            cell = gridBodyRow.createCell(j+k);
                        }
                        // 设置单元格样式/内容
                        setCellStyle(body,workbook,cell);
                        
                        cell.setCellValue(singleData.getString(body.getString("systemcode")));
                    }
                }
            }
            
//            // 已打印行回写
//            for(int i=0;i<hasPrintRows.size();i++){
//                HSSFRow row = sheet.createRow(printRowNum + i + 3);
//                HSSFRow _row = hasPrintRows.get(i);
//                POIUtils.copyRow(workbook, _row, row, true);
//            }
            for(int i=0;i<text.size();i++){
                JSONObject _temp = text.getJSONObject(i);
                int yPath = Integer.parseInt(_temp.getString("yPath"));
                if(yPath > beginRowNum){
                    createCell(workbook,sheet,text.getJSONObject(i),(printRowNum + yPath - beginRowNum));
                }
            }
            
            // 自适应宽度
            for(int i=0;i<=900;i++){
                sheet.autoSizeColumn(i,true); 
            }
            
            String printType = json.getString("printtype");
            if("horizontal".equals(printType)){
              //设置横向打印
                sheet.getPrintSetup().setLandscape(true);
            }
            
            // 生成页眉
            createPageHeader(json.getJSONObject("header"),sheet);
            // 生成页脚
            createPageFooter(json.getJSONObject("footer"),sheet);
            // 输出excel
            workbook.write(out);
        }catch (Exception e){
            e.printStackTrace();
        }
        
    }
    
    private static void createCell(HSSFWorkbook workbook,HSSFSheet sheet,JSONObject tempObj,int yPath){
        
        // 行列
        int xPath = Integer.parseInt(tempObj.getString("xPath"));
        // 占用的行列
        int cols = Integer.parseInt(tempObj.getString("cols"));
        int rows = Integer.parseInt(tempObj.getString("rows"));
        // 获取行信息
        HSSFRow row = sheet.getRow(yPath);
        // 判断行是否被创建
        if(row == null){
            row = sheet.createRow(yPath);
        }
        
        HSSFCell cell = row.getCell(xPath);
        // 判断单元是否被创建
        if(cell == null){
            cell = row.createCell(xPath);
        }
      
        //合并单元格居中
        sheet.addMergedRegion(new CellRangeAddress(yPath,yPath+rows-1,xPath,xPath+cols-1));
        // 设置单元格样式/内容
        setCellStyle(tempObj,workbook,cell);
    }
    
    /**
     * 生成页眉
     */
    private static void createPageHeader(JSONObject headerObj,HSSFSheet sheet){
    	HSSFHeader header = sheet.getHeader();
    	JSONObject headerCenter = headerObj.getJSONObject("center");
	    header.setCenter(getStr(headerCenter));
	    JSONObject headerLeft = headerObj.getJSONObject("left");
	    header.setLeft(getStr(headerLeft));
	    JSONObject headerRight = headerObj.getJSONObject("right");
	    header.setRight(getStr(headerRight));
    }
    
    private static String getStr(JSONObject obj){
        if(obj.getString("content") == null || "".equals(obj.getString("content").trim())){
            return "";
        }
        return obj.getString("content");
    }
    
    /**
     * 生成页脚
     * @param sheet
     */
    private static void createPageFooter(JSONObject footerObj,HSSFSheet sheet){
    	HSSFFooter footer = sheet.getFooter();
    	JSONObject footerRight = footerObj.getJSONObject("right");
    	footer.setRight(getStr(footerRight));//"Page " + HSSFFooter.page() + " of " + HSSFFooter.numPages() 
	    JSONObject footerLeft = footerObj.getJSONObject("left");
	    footer.setLeft(getStr(footerLeft));
	    JSONObject footerCenter = footerObj.getJSONObject("center");
	    footer.setCenter(getStr(footerCenter));

    }
    
    /**
     * 设置单元格样式
     * <功能详细描述>
     * @param obj
     * @param workbook
     * @param cell
     * @see [类、类#方法、类#成员]
     */
    private static void setCellStyle(JSONObject obj,HSSFWorkbook workbook,HSSFCell cell){
     // 单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 获取字体对齐方式
        String textAlign = obj.getString("textalign");
        short wordAlign = 0;
        if("center".equals(textAlign)){
        	wordAlign = HSSFCellStyle.ALIGN_CENTER;
        }else if("left".equals(textAlign)){
        	wordAlign = HSSFCellStyle.ALIGN_LEFT;
        }else{
        	wordAlign = HSSFCellStyle.ALIGN_RIGHT;
        }
        // 字体居中
        cellStyle.setAlignment(wordAlign);
        // 设置字体
        HSSFFont font = getFont(obj,workbook);
        cellStyle.setFont(font);
        cell.setCellStyle(cellStyle);
        cell.setCellValue(obj.getString("content"));
    }
    
    /**
     * 设置字体
     * <功能详细描述>
     * @param obj
     * @param workbook
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static HSSFFont getFont(JSONObject obj,HSSFWorkbook workbook){
        //设置字体
        HSSFFont font = workbook.createFont();
        font.setFontName("仿宋_GB2312");
        // 获取字体是否需要加粗
        String fontWeight = obj.getString("fontweight");
        if(!"400".equals(fontWeight) && !"normal".equals(fontWeight)){
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体加粗
        }
        // 获取字体大小
        int fontSize = Integer.parseInt(obj.getString("fontsize").replace("px", ""));
        font.setFontHeightInPoints((short)fontSize);
        return font;
    }
    
}
