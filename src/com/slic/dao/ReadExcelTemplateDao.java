/*
 * 文 件 名:  ReadExcelTemplateDao.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年8月10日
 */
package com.slic.dao;

import java.io.File;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.RegionUtil;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Parent;
import org.jdom.input.SAXBuilder;

import com.slic.utils.CommonUtils;
import com.slic.utils.MoneyUtils;

/**
 * 读取打印模板
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年8月10日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ReadExcelTemplateDao {
    // 单元格名称
    private static final String NORMAL_CELL_NAME = "cell";
    // 普通表格名称
    private static final String TABLE_NAME = "table";
    // 边框
    private static final String BORDER_BOTTOM = "bottom";
    private static final String BORDER_LEFT = "left";
    private static final String BORDER_RIGHT = "right";
    private static final String BORDER_TOP = "top";
    private static final String BORDER_ALL = "all";
    
    private HSSFWorkbook workbook;
    private HSSFSheet sheet;
    private Object jsonData;
    private JSONObject userLoginInfo ;
    // 起始列号
    private int cols = 0;
    // 起始行号
    private int rows = 0;
   
    
    
    /**
     * 初始化方法
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public void init() {
        this.cols = 0;
        this.rows = 0;
    }
    
    /**
     * 读取模板
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public void readTemplate(String templateName,HSSFWorkbook workbook,HSSFSheet sheet,Object json,JSONObject userLoginInfo)
        throws Exception {
        // 复位
        init();
        this.workbook = workbook;
        this.sheet = sheet;
        this.jsonData = json;
        this.userLoginInfo = userLoginInfo;
        
        String path =
            Thread.currentThread().getContextClassLoader().getResource("").getPath() + "/template/" + templateName;
        File file = new File(path);
        SAXBuilder builder = new SAXBuilder();
        //解析xml文件
        Document parse = builder.build(file);
        //获取xml文件根节点
        Element root = parse.getRootElement();
        // 获取所有子节点
        List<Element> rowGroups = root.getChildren();
        if (CommonUtils.listIsNotEmpty(rowGroups)) {
            for (int i = 0, len = rowGroups.size(); i < len; i++) {
            	Element currentRow = rowGroups.get(i);
            	if(i == 0){
            		String beginRowNum = currentRow.getAttribute("ypath").getValue();
            		// 起始行号
            		rows = Integer.parseInt(beginRowNum) - 1;
            		rows = rows >= 0 ? rows : 0;
            	}else{
            		// 计算上一行与下一行的差值
            		Element prevObj = rowGroups.get(i - 1);
            		String prevBeginRowNum = prevObj.getAttributeValue("ypath");
            		String currentRowNum = currentRow.getAttributeValue("ypath");
            		rows += Integer.parseInt(currentRowNum) - Integer.parseInt(prevBeginRowNum) - 1;
            	}
            	// 起始列号
            	String beginColNum = currentRow.getAttributeValue("xpath");
            	cols = Integer.parseInt(beginColNum) - 1;
            	cols = cols >= 0 ? cols : 0;
            	
                // 读取所有单元格内容
                List<Element> cellGroups = currentRow.getChildren();
                for (int j = 0, len1 = cellGroups.size(); j < len1; j++) {
                    Element cell = cellGroups.get(j);
                    //                  System.out.println(cell.getName());
                    if (NORMAL_CELL_NAME.equalsIgnoreCase(cell.getName())) {
                        // 普通单元格
                        createCell(cell,json,0);
                        if(j+1 == len1){
                        	String ownRows = cell.getAttributeValue("rows");
                            int ownRowsInt = Integer.parseInt(ownRows);
                        	rows += ownRowsInt;
                        }
                    }else {
                        // 表格
                        createTable(cell);
                    }
                    
                }
            }
        }
    }
    
    /** 
     * 创建单元格
     * @param cell
     * @see [类、类#方法、类#成员]
     */
    private void createCell(Element cellObj,Object json,int index) {
    	HSSFRow row = sheet.getRow(rows);
    	if(row == null){
    		row = sheet.createRow(rows);
    	}
    	HSSFCell cell = row.createCell(cols);
//    	System.out.println(rows+"-----"+cols);
    	// 设置单元格内容
    	if(json != null){
    	    // 判断是否需要从接口取值
    	    if(json instanceof JSONObject && !"".equals(cellObj.getAttributeValue("systemcode"))){
                JSONObject temp = (JSONObject) json;
                String key = cellObj.getAttributeValue("systemcode");
                
                if("@index".equals(key)){
                 // 判断下标
                    cell.setCellValue(index);
                }else if("@printdate".equals(key)){
                    // 判断打印当前时间
                    cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                }else{
                    // 普通需要从接口取值字段
                    String type = cellObj.getAttributeValue("type");
                    // 判断是否需要对接口返回字段做特殊处理
                    if(type == null){
                        String giftflag = null;
                        try{
                            giftflag = temp.getString("giftflag");
                        }catch(Exception e){}
                        // 判断是否为赠品
                        if("1".equals(giftflag)){
                            // 赠品
                            setGift(key,cell,temp);
                        }else{
                            // 非赠品
                            setNormal(key,cell,temp);
                        }
                    }else if("moneyUpperCase".equals(type)){
                        // 钱大写
                        try{
                            String _temp = MoneyUtils.number2CNMontrayUnit(new BigDecimal(temp.getString(key)));
                            cell.setCellValue(_temp); 
                        }catch(Exception e){
                            cell.setCellValue("");
                        }
                    }else if("moneyLowerCase".equals(type)){
                        // 钱小写
                        try{
                            String _temp = MoneyUtils.moneyFilter(Double.parseDouble(temp.getString(key)));
                            cell.setCellValue("￥"+_temp); 
                        }catch(Exception e){
                            cell.setCellValue("");
                        }
                    }
                    
                }
            }else{
                cell.setCellValue(cellObj.getAttributeValue("content"));
            } 
    	}else{
    	    cell.setCellValue(cellObj.getAttributeValue("content"));
    	}
    	
    	// 设置单元格样式/内容
        setCellStyle(cellObj,cell);
        // 占有行数
        String ownRows = cellObj.getAttributeValue("rows");
        int ownRowsInt = Integer.parseInt(ownRows);
    	// 占有列数
        String ownCols = cellObj.getAttributeValue("cols");
        int ownColsInt = Integer.parseInt(ownCols);
    	//合并单元格居中
        CellRangeAddress cellRangeAddress = new CellRangeAddress(rows,rows+ownRowsInt-1,cols,cols+ownColsInt-1);
        sheet.addMergedRegion(cellRangeAddress);
        // 设置边框
        setBorder(cellObj,cellRangeAddress);
        cols += ownColsInt;
    }
    
   /** <一句话功能简述>
     * 设置订单的普通字段
     * @see [类、类#方法、类#成员]
     */
    private void setNormal(String key,HSSFCell cell,JSONObject temp) {
        if("packprice".equals(key) || "bulkprice".equals(key)){
            // 整价/散价 特殊处理
            String _temp = MoneyUtils.moneyFilter(Double.parseDouble(temp.getString(key)));
            setCellValue(_temp, cell);
        }else if("editdate".equals(key)){
            // 新增订单时间特殊处理
            String _temp = temp.getString(key);
            if(_temp != null && _temp.length() > 10){
                _temp = _temp.substring(0, 10);
            }
            setCellValue(_temp, cell);
        }else if("realname".equals(key)){
            // 制单员
            setCellValue(userLoginInfo.getString(key), cell);
        }else if("ename".equals(key)){
            // 制单员
            setCellValue(userLoginInfo.getString(key), cell);
        }else if("localename".equals(key)){
            // 制单员
            setCellValue(temp.getString("ename"), cell);
        }else if("goodsname".equals(key)){
            // 
            String _temp = temp.getString(key);
            if(_temp != null && _temp.length() > 15){
                _temp = _temp.substring(0,15) + "...";
            }
            setCellValue(_temp, cell);
        }else if("barcode".equals(key)){
            // 
            String _temp = temp.getString(key);
            if(_temp != null && _temp.length() > 13){
                _temp = _temp.substring(0,13) + "...";
            }
            setCellValue(_temp, cell);
        }else{
            // 不需特殊处理字段
            setCellValue(temp,key,cell);
        }
    }

/** <一句话功能简述>
     * 设置赠品
     * @see [类、类#方法、类#成员]
     */
    private void setGift(String key,HSSFCell cell,JSONObject temp) {
        String[] printArray = {"barcode","goodsname","bulkqty","notes"};
        for(String printStr : printArray){
            if(printStr.equals(key)){
                if("bulkqty".equals(printStr)){
                    setCellValue(temp,"giftqty",cell);
                }else if("notes".equals(printStr)){
                    setCellValue("赠品", cell);
                }else{
                    setCellValue(temp,key,cell);
                }
            }
        }
    }

 /** <一句话功能简述>
     * <功能详细描述>
     * @param temp
     * @param key
     * @param cell
     * @see [类、类#方法、类#成员]
     */
    private static void setCellValue(JSONObject temp, String key, HSSFCell cell) {
        // 不需特殊处理字段
        try{
            setCellValue(temp.getString(key),cell) ;
        }catch(Exception e){
            cell.setCellValue("");
        } 
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param string
     * @see [类、类#方法、类#成员]
     */
    private static void setCellValue(String value,HSSFCell cell) {
        cell.setCellValue(value);
    }

    /**
     * 设置边框
     * <功能详细描述>
     * @param cell xml 节点
     * @param cellRangeAddress 合并单元格
     * @see [类、类#方法、类#成员]
     */
    private void setBorder(Element cell,CellRangeAddress cellRangeAddress){
        //HSSFCellStyle cellStyle = cell.getCellStyle();
        //cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);//下边框        
        //cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框        
        //cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框        
        //cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框 
        String borderType = cell.getAttributeValue("border");
//        System.out.println("---------------------------------------"+borderType);
        if(borderType == null){
            return;
        }
        if(borderType.indexOf(BORDER_BOTTOM) > -1 || BORDER_ALL.equals(borderType)){
            RegionUtil.setBorderBottom(1, cellRangeAddress, sheet, workbook);
        }
        if(borderType.indexOf(BORDER_LEFT)  > -1 || BORDER_ALL.equals(borderType)){
            RegionUtil.setBorderLeft(1, cellRangeAddress, sheet, workbook);
        }
        if(borderType.indexOf(BORDER_RIGHT) > -1 || BORDER_ALL.equals(borderType)){
            RegionUtil.setBorderRight(1, cellRangeAddress, sheet, workbook);
        }
        if(borderType.indexOf(BORDER_TOP)  > -1 || BORDER_ALL.equals(borderType)){
            RegionUtil.setBorderTop(1, cellRangeAddress, sheet, workbook);
        }
        
    }
    
    /**
     * 设置单元格样式
     * <功能详细描述>
     * @param obj
     * @param workbook
     * @param cell
     * @see [类、类#方法、类#成员]
     */
    private void setCellStyle(Element obj,HSSFCell cell){
     // 单元格样式
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        // 获取字体对齐方式
        String textAlign = obj.getAttributeValue("textalign");
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
    }
    
    /**
     * 设置字体
     * <功能详细描述>
     * @param obj
     * @param workbook
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static HSSFFont getFont(Element obj,HSSFWorkbook workbook){
        //设置字体
        HSSFFont font = workbook.createFont();
        font.setFontName("宋体");
        
        String fontColor = obj.getAttributeValue("fontcolor");
        if(CommonUtils.strIsNotEmpty(fontColor)){
        	//处理把它转换成十六进制并放入一个数 
            int[] color=new int[3]; 
            color[0]=Integer.parseInt(fontColor.substring(1, 3), 16); 
            color[1]=Integer.parseInt(fontColor.substring(3, 5), 16); 
            color[2]=Integer.parseInt(fontColor.substring(5, 7), 16); 
            //自定义颜色 
            HSSFPalette palette = workbook.getCustomPalette(); 
            palette.setColorAtIndex(HSSFColor.BLACK.index,(byte)color[0], (byte)color[1], (byte)color[2]);
            font.setColor(HSSFColor.BLACK.index);
        }
        
        // 获取字体是否需要加粗
        String fontWeight = obj.getAttributeValue("fontweight");
        if(!"400".equals(fontWeight) && !"normal".equals(fontWeight)){
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//字体加粗
        }
        // 获取字体大小
        int fontSize = Integer.parseInt(obj.getAttributeValue("fontsize").replace("px", ""));
        font.setFontHeightInPoints((short)fontSize);
        return font;
    }
    
    /** 
     * 创建表格
     * @param cell
     * @see [类、类#方法、类#成员]
     */
    private void createTable(Element cellObj) {
        // 生成表头
    	Element thead = cellObj.getChild("thead");
    	createThead(thead);
    	
    	Element tbody = cellObj.getChild("tbody");
    	 JSONObject json = null;
         if(jsonData instanceof JSONObject){
             json = (JSONObject)jsonData;
         }
         if(json.get("detail") instanceof JSONObject){
             JSONObject detail = json.getJSONObject("detail");
             createTBody(tbody,detail,1);
         }else if(json.get("detail") instanceof JSONArray){
             JSONArray detail = json.getJSONArray("detail");
             for(int i=0;i<detail.size();i++){
                 createTBody(tbody,detail.getJSONObject(i),i+1);
             }
         }
    	
    	
    	Element tother = cellObj.getChild("tother");
    	createTOther(tother);
    }

    /**
     * 创建非重复表体
     * @param tother
     */
    private void createTOther(Element tother) {
    	List<Element> trGroups = tother.getChildren("tr");
    	for(int i=0,len=trGroups.size();i<len;i++){
    		cols = 0;
    		List<Element> tdGroups = trGroups.get(i).getChildren();
            for (int j = 0, len1 = tdGroups.size(); j < len1; j++) {
                Element cell = tdGroups.get(j);
                // 普通单元格
                createCell(cell,jsonData,0);
                if(j+1 == len1){
                	String ownRows = cell.getAttributeValue("rows");
                    int ownRowsInt = Integer.parseInt(ownRows);
                	rows += 1;
                }
            }
    	}
	}

	/**
     * 创建表体
     * @param tbody
     */
    private void createTBody(Element tbody,JSONObject json,int index) {
    	List<Element> trGroups = tbody.getChildren("tr");
    	for(int i=0,len=trGroups.size();i<len;i++){
    		cols = 0;
    		List<Element> tdGroups = trGroups.get(i).getChildren();
            for (int j = 0, len1 = tdGroups.size(); j < len1; j++) {
                Element cell = tdGroups.get(j);
                // 普通单元格
                createCell(cell,json,index);
                if(j+1 == len1){
                	String ownRows = cell.getAttributeValue("rows");
                    int ownRowsInt = Integer.parseInt(ownRows);
                	rows += 1;
                }
            }
    	}
	}

	/**
     * 创建表头
     * @param thead
     */
	private void createThead(Element thead) {
		List<Element> trGroups = thead.getChildren("tr");
    	for(int i=0,len=trGroups.size();i<len;i++){
    		cols = 0;
    		List<Element> tdGroups = trGroups.get(i).getChildren();
            for (int j = 0, len1 = tdGroups.size(); j < len1; j++) {
                Element cell = tdGroups.get(j);
                // 普通单元格
                createCell(cell,null,0);
                if(j+1 == len1){
                	String ownRows = cell.getAttributeValue("rows");
                    int ownRowsInt = Integer.parseInt(ownRows);
                	rows += 1;
                }
            }
    	}
	}
	
	
}
