package com.slic.service;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slic.utils.HSSFTools;
import com.slic.utils.HSSFUtil;
import com.slic.utils.MoneyUtils;

public class ExportExcelService {

	private Logger logger = LoggerFactory.getLogger(getClass());
	private TemplateService templateService = new TemplateService();
	/**
	 * 字体样式
	 */
	private Map<String, HSSFFont> fonts;
	/**
	 * 单元格样式
	 */
	private Map<String, HSSFCellStyle> cellStyles;
	/**
	 * 用户资料
	 */
	private JSONObject userJson;
	/**
	 * 配置信息
	 */
	private Properties configs;
	/**
	 * 模版名称
	 */
	private String templateName;
	/**
	 * 模版格式
	 */
	private JSONObject setsJson;
	/**
	 * sheet 各列宽属性
	 */
	private JSONArray colsJson;
	/**
	 * sheet 行属性
	 */
	private JSONArray rowsJson;
	/**
	 * 所有列宽和 px
	 */
	private int colsWidth;
	/**
	 * 所有行髙和 px 
	 */
	private int rowsHeight;
	/**
	 * 打印模版高度 cm
	 */
	private double paperheight;
	/**
	 * 打印模版宽度 cm
	 */
	private double paperwidth;
	/**
	 * 数据明细髙度 px
	 */
	private int datailHeight;
	/**
	 * 报表标题髙度 px
	 */
	private int reportTitleHeight;
	/**
	 * 汇总髙度 px
	 */
	private int accountHeight;
	/**
	 * HSSFWorkbook
	 */
	private HSSFWorkbook workbook;
	/**
	 * 行序号
	 */
	private int rowIndex = 0;
	/**
	 * 打印序号
	 */
	private int rowNum = 1;
	/**
	 * 数据每页开始索引
	 */
	private int itemBeginIndex = 0;
	/**
	 * 是否最后一页
	 */
	private boolean isLastPage = false;
	/**
	 * 第几页
	 */
	private int repeatPageNum;
	/**
	 * 总页数
	 */
	private int pageCount;
	/**
	 * 每个sheet单的数据明细
	 */
	private JSONArray itemsData;
	/**
	 * 数据明细 size（）
	 */
	private int detailTotalRows = 0;
	/**
	 * 页汇总map
	 */
	private Map<String, BigDecimal> pageSums = new HashMap<String, BigDecimal>();
	/**
	 * 汇总map
	 */
	private Map<String, BigDecimal> allSums = new HashMap<String, BigDecimal>();
	/**
	 * 每页标签占几行
	 */
	private int diviRow;
	/**
	 * 每页标签占几列
	 */
	private int diviCol;
	/**
	 * 单标签所占行数,所占列数
	 */
	private int zirow,zicol;
	/**
	 * 初始化打印页面模板
	 * @param templateName
	 * @param userJson
	 * @param configs
	 * @throws Exception
	 */
	public void initPrintPageTPL(String templateName, JSONObject userJson,Properties configs) throws Exception
	{
		logger.info("============= 初始化打印页面模板 - statr =============");
		this.userJson = userJson;
		this.configs = configs;
		this.templateName = templateName;
		
		// 获取模版json
		String str = templateService.parseTemplate(templateName, userJson.getString("eid"));
		logger.info("模版数据 ==>" + str);
		JSONObject templateJson = JSONObject.fromObject(str);
		setsJson = templateJson.getJSONObject("printSetting");
		colsJson = templateJson.getJSONArray("cols");
		rowsJson = templateJson.getJSONArray("rows");
		
		// 默认下边距15mm
		if (setsJson.getInt("bottompadding") < 15) {
			setsJson.put("bottompadding", 15);
		}
		
		// 统计cell总宽
		colsWidth = 0;
		for (int i = 0, j = colsJson.size(); i < j; i++) {
			colsWidth += Integer.parseInt(colsJson.getJSONObject(i).getString("width"));
		}
		// 统计row总髙
		rowsHeight = 0;
		for (int i = 0, j = rowsJson.size(); i < j; i++) {
			if ("dataDetail".equals(rowsJson.getJSONObject(i).getString("group"))){
				datailHeight = rowsJson.getJSONObject(i).getInt("height");
			}
			else if ("reportTitle".equals(rowsJson.getJSONObject(i).getString("group"))){
				reportTitleHeight = rowsJson.getJSONObject(i).getInt("height");
			}
			else if ("account".equals(rowsJson.getJSONObject(i).getString("group"))){
				accountHeight = rowsJson.getJSONObject(i).getInt("height");
			}
			rowsHeight += Integer.parseInt(rowsJson.getJSONObject(i).getString("height"));
		}
		// 0 纵向  1横向
		int printdirection = setsJson.getInt("printdirection"); 
		if (0 == printdirection){
			paperheight = setsJson.getDouble("paperheight");
			paperwidth = setsJson.getDouble("paperwidth");
		}
		else {
			paperwidth = setsJson.getDouble("paperheight");
			paperheight = setsJson.getDouble("paperwidth");
		}
		
		// 标签打印
		if (1 == setsJson.getInt("printType")) {
			diviRow = setsJson.getJSONObject("printPriceTicket").getInt("row");
			diviCol = setsJson.getJSONObject("printPriceTicket").getInt("col");
			zirow = setsJson.getJSONObject("printPriceTicket").getInt("zirow") + 1;
			zicol = setsJson.getJSONObject("printPriceTicket").getInt("zicol") + 1;
		}
		
		double pwidth = paperwidth - ((setsJson.getDouble("leftpadding")/10.0) + (setsJson.getDouble("rightpadding")/10.0));
		double pheight = paperheight - ((setsJson.getDouble("toppadding")/10.0) + (setsJson.getDouble("bottompadding")/10.0));
		if (HSSFUtil.pxToWidthCM(colsWidth) > pwidth) {
			throw new Exception("模板设计错误;原因: 所有列宽和大于整个页面宽度！（请注意打印方向）");
		}
		if (HSSFUtil.pxToHeightCM(rowsHeight) > pheight) {
			throw new Exception("模板设计错误;原因: 所有行高和大于整个页面高度！（请注意打印方向）");
		}
		
		workbook = new HSSFWorkbook();
		fonts = new HashMap<String, HSSFFont>();
		cellStyles = new HashMap<String, HSSFCellStyle>();
		
		logger.info("============= 初始化打印页面模板 - end =============");
	}

	/**
	 * 打印 execl （数据）
	 * @param resultData
	 * @throws Exception
	 */
	public void printExecl(JSONObject resultData) throws Exception 
	{
		logger.info("============= 打印 execl （数据） - start =============");
		
		JSONObject backDatas = JSONObject.fromObject(resultData.get("responseText"));
		
		// 0 正常打印 1标签打印
		if (1 == setsJson.getInt("printType"))
		{
			printDivi(backDatas);
		}
		else {
			// 即打即停
			if (setsJson.getBoolean("stopFight")) 
			{
				printExeclNoPage(backDatas);
			}
			// 分页打印
			else
			{
				printExeclPage(backDatas);
			}
		}
		logger.info("============= 打印 execl （数据） - end =============");
	}

	/**
	 * 标签打印
	 * @param backDatas
	 */
	private void printDivi(JSONObject backDatas) throws Exception {
		logger.info("================== 标签打印 - start ===================");
		// item_0 表头数据数组
		JSONArray totalData = backDatas.getJSONArray("item_0");
		int sheeCount = totalData.size();
		String execlSheetName = "标签订单";
		
		for (int sheetNum = 0; sheetNum < sheeCount; sheetNum++)
		{
			JSONObject mainData = totalData.getJSONObject(sheetNum);
			JSONArray itemsData = backDatas.getJSONArray("item_" + (sheetNum + 1));
			doDiviSheet(mainData, itemsData, execlSheetName);
		}
		logger.info("================== 标签打印 - end ===================");
		
	}

	/**
	 * 标签打印
	 * @param mainData
	 * @param itemsData
	 * @param execlSheetName
	 */
	private void doDiviSheet(JSONObject mainData, JSONArray itemsData, String execlSheetName) {
		this.detailTotalRows = itemsData.size();
		HSSFSheet sheet = workbook.getSheet(execlSheetName);
		if (sheet == null) {
			sheet = workbook.createSheet(execlSheetName);
			// 设置模版打印基础信息
			HSSFTools.initPrintSytle(sheet, setsJson);
			// 设置sheet各列宽
			HSSFTools.setColumnWidth(sheet, colsJson, diviCol, zicol);
		}
		// 循环打印数据
		for (int i = 0, j = itemsData.size(); i < j; i++) {
			JSONObject jsonData = itemsData.getJSONObject(i);
			int dv_row = i / diviCol;
			int dv_col = i % diviCol;
			
			// 循环模版json
			for (int k = 0, p = rowsJson.size(); k < p; k++) {
				JSONObject rowObj = rowsJson.getJSONObject(k);
				addDiviRow(sheet, mainData, jsonData, rowObj, dv_row, dv_col);
			}
		}
	}

	/**
	 * 标签打印row
	 * @param sheet
	 * @param mainData
	 * @param jsonData
	 * @param rowObj
	 * @param dv_row
	 * @param dv_col
	 */
	private void addDiviRow(HSSFSheet sheet, JSONObject mainData, JSONObject jsonData, JSONObject rowObj, int dv_row, int dv_col) {
		
		int v = rowObj.getJSONArray("list").getJSONObject(0).getInt("row") + (zirow * dv_row);
		//row行
		HSSFRow row = sheet.getRow(v);
		if (row == null) {
			 row = sheet.createRow(v);
			 row.setHeight(HSSFUtil.cmToRowHeightUnit(HSSFUtil.pxToHeightCM(rowObj.getInt("height"))));
		}

		JSONArray cellList = rowObj.getJSONArray("list");
		// 循环生成该row行的cell
		for (int k = 0, p = cellList.size(); k < p; k++) {
			JSONObject cellObj = cellList.getJSONObject(k);
			// 生成cell
			addDiviCell(sheet, row, cellObj, mainData, jsonData, dv_row, dv_col);
		}

	}

	/**
	 * 标签打印cell
	 * @param sheet
	 * @param row
	 * @param cellObj
	 * @param mainData
	 * @param jsonData
	 * @param dv_row
	 * @param dv_col
	 */
	private void addDiviCell(HSSFSheet sheet, HSSFRow row, JSONObject cellObj,
			JSONObject mainData, JSONObject jsonData, int dv_row, int dv_col) {
		// 创建cell
		HSSFCell cell = row.createCell(cellObj.getInt("col") + (zicol * dv_col) );
		String systemCode = cellObj.getString("systemCode").toLowerCase();
		int type = cellObj.getInt("type");
		
		// 页面普通文本
		if (type == 2) {
			cell.setCellValue(cellObj.getString("data"));
		}
		// 内置
		else if (type == 1) {
			if ("rowNum".equalsIgnoreCase(systemCode)) {
				cell.setCellValue(rowNum++);
			} else if ("currentTime".equalsIgnoreCase(systemCode)) {
				cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			} else if ("currentData".equalsIgnoreCase(systemCode)) {
				cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
			} 
		}
		// 数据库
		else if (type == 0) {
			// 建单日期
			if ("editdate".equals(systemCode)) {
				String editdate = jsonData.getString(systemCode);
				if (editdate != null && editdate.length() > 10) {
					editdate = editdate.substring(0, 10);
				}
				cell.setCellValue(editdate);
			}
			// 订单类型
			else if ("sheettype".equals(systemCode)) {
				String[] temp = templateName.split("_");
				String sheetTypeName = configs.getProperty(temp[0]);
				sheetTypeName = sheetTypeName == null ? "" : sheetTypeName;
				cell.setCellValue(sheetTypeName);
			}
			// 打印公司名称
			else if ("ename".equals(systemCode)) {
				cell.setCellValue(userJson.getString("ename"));
			}
			// 普通字段
			else if (mainData.containsKey(systemCode)) {
				cell.setCellValue(mainData.getString(systemCode));
			}
			// 普通字段
			else if (jsonData.containsKey(systemCode)) {
				cell.setCellValue(jsonData.getString(systemCode));
			}
			// 用户登录信息
			else if (null != userJson) {
				cell.setCellValue(userJson.getString(systemCode));
			}
		}
		// 设置cell样式 
		HSSFTools.setCellStyle(workbook, sheet, cell, cellObj, dv_row, dv_col, zirow, zicol, fonts, cellStyles);

	}

	/**
	 * 即打即停
	 * @param backDatas
	 * @throws Exception
	 */
	public void printExeclNoPage(JSONObject backDatas) throws Exception 
	{
		logger.debug("================== 即打即停 - start ===================");
		// item_0 表头数据数组
		JSONArray totalData = backDatas.getJSONArray("item_0");
		int sheeCount = totalData.size();
		// 重置行序号
		rowIndex = 0;
		// 重置明细数据
		this.detailTotalRows = 0;
		String execlSheetName = "订单";
		
		for (int sheetNum = 0; sheetNum < sheeCount; sheetNum++)
		{
			rowNum = 1;
			// 重置汇总项
			setAllSumsMapKey();
			JSONObject mainData = totalData.getJSONObject(sheetNum);
			JSONArray itemsData = backDatas.getJSONArray("item_" + (sheetNum + 1));
			generateSingleSheet(mainData, itemsData, execlSheetName);
		}
		logger.debug("================== 即打即停 - end ===================");
	}

	/**
	 * 即打即停
	 * @param mainData 
	 * @param itemsData 
	 * @param execlSheetName - sheet名称
	 * @throws Exception
	 */
	private void generateSingleSheet(JSONObject mainData, JSONArray itemsData, String execlSheetName) throws Exception
	{
		// 重新计算商品（赠品）
		itemsData = retCountItems(itemsData);
		this.detailTotalRows += itemsData.size();
		HSSFSheet sheet = workbook.getSheet(execlSheetName);
		if (sheet == null) 
		{
			sheet = workbook.createSheet(execlSheetName);
			// 设置模版打印基础信息
			HSSFTools.initPrintSytle(sheet,setsJson);
			// 设置sheet各列宽
			HSSFTools.setColumnWidth(sheet, colsJson) ;
		}
		
		// 开始循环模版json
		for (int i = 0, j = rowsJson.size(); i < j; i++) {
			JSONObject rowObj = rowsJson.getJSONObject(i);
			
			// 是否数据明细特殊row
			if ("dataDetail".equals(rowObj.getString("group"))) {
				addDatailRows(sheet, itemsData, rowObj);
				continue;
			}
			// 普通row
			addOneRow(sheet, mainData, rowObj);
		}
		// 添加空白分隔行
		HSSFRow row = sheet.createRow(rowIndex++);
		row.setHeight((short) HSSFUtil.cmToRowHeightUnit(0.5));
	}
	
	/**
	 * 生成单条普通row
	 * @param sheet
	 * @param jsonData
	 * @param rowObj
	 */
	private void addOneRow(HSSFSheet sheet, JSONObject jsonData,JSONObject rowObj) {
		// row行
		int q = rowIndex++;
		HSSFRow row = sheet.getRow(q);
		if (row == null) {
			row = sheet.createRow(q);
			row.setHeight(HSSFUtil.cmToRowHeightUnit(HSSFUtil.pxToHeightCM(rowObj.getInt("height"))));
		}
		// 页注脚 10 ；汇总 20
		int printType = 0;
		if ("pageFoot".equalsIgnoreCase(rowObj.getString("group"))) {
			printType = 10;
		}
		if ("account".equalsIgnoreCase(rowObj.getString("group"))) {
			printType = 20;
		}
		
		int bi_row = 0;
		JSONArray cellList = rowObj.getJSONArray("list");
		// 循环生成该row行的cell
		for (int k = 0, p = cellList.size(); k < p; k++) {
			JSONObject cellObj = cellList.getJSONObject(k);
			// 本该开始行序
			int startRow = cellObj.getInt("row");
			// 本该结束行序
			int endRow = cellObj.getInt("endRow");
			int v = endRow - startRow;
			if (v > bi_row) {
				bi_row = v;
			}
			// 生成cell
			addCell(sheet, row, cellObj, jsonData, printType);
		}
		rowIndex += bi_row;
	}
	
	/**
	 * 循环生成数据明细rows
	 * @param sheet
	 * @param itemsData
	 * @param rowObj
	 */
	private void addDatailRows(HSSFSheet sheet, JSONArray itemsData,JSONObject rowObj) {
		// 重置页注销数据
		setPageSumsMapKey();
		// 循环商品生成
		for (int i = 0, j = itemsData.size(); i < j; i++) {
			// 第i条的数据
			JSONObject jsonData = itemsData.getJSONObject(i);
			/********************* 统计页注脚、汇总 *********************/
			if (pageSums.size() > 0) {
				for (String key : pageSums.keySet()) {
					if (isTrueData(jsonData.getString(key))) {
						BigDecimal item = new BigDecimal(jsonData.getString(key));
						pageSums.put(key, item.add(pageSums.get(key)));
					}
				}
			}
			if (allSums.size() > 0) {
				for (String key : allSums.keySet()) {
					if (isTrueData(jsonData.getString(key))) {
						BigDecimal item = new BigDecimal(jsonData.getString(key));
						allSums.put(key, item.add(allSums.get(key)));
					}
				}
			}
			/********************* 统计页注脚、汇总 *********************/
			addOneRow(sheet, jsonData, rowObj);
		}
	}

	/**
	 * 生成 cell添加样式、json数据
	 * @param sheet 
	 * @param row 
	 * @param cellObj cell属性
	 * @param jsonObject 打印json数据
	 * @param printType 
	 */
	public void addCell(HSSFSheet sheet,HSSFRow row,JSONObject cellObj,JSONObject jsonObject, int printType) {
		// 创建cell
		HSSFCell cell = row.createCell(cellObj.getInt("col"));
		String systemCode = cellObj.getString("systemCode").toLowerCase();
		int type = cellObj.getInt("type");
		
		// 页注脚打印
		if (printType == 10) {
			if (pageSums.containsKey(systemCode)) {
				cell.setCellValue(pageSums.get(systemCode).toString());
			}
			else {
				cell.setCellValue(cellObj.getString("data"));
			}
		}
		// 汇总打印
		else if (printType == 20) {
			if (allSums.containsKey(systemCode.split(",")[0])) {
				String value = allSums.get(systemCode.split(",")[0]).toString();
				if (systemCode.indexOf("summoney,")>-1) {
					handleSpecialField(value, systemCode.split(",")[1], cell);
				}
				else {
					cell.setCellValue(allSums.get(systemCode).toString());
				}
			}
			else {
				cell.setCellValue(cellObj.getString("data"));
			}
		}
		else {
			// 页面普通文本
			if (type == 2) {
				cell.setCellValue(cellObj.getString("data"));
			}
			// 内置
			else if (type == 1) {
				if ("rowNum".equalsIgnoreCase(systemCode)) {
					cell.setCellValue(rowNum++);
				}
				else if ("currentTime".equalsIgnoreCase(systemCode)){
					cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
				}
				else if ("currentData".equalsIgnoreCase(systemCode)){
					cell.setCellValue(new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				}
				else if ("pagination".equalsIgnoreCase(systemCode)) {
					cell.setCellValue("["+repeatPageNum+"/"+pageCount+"]");
				}
				else if ("pageNum".equalsIgnoreCase(systemCode)) {
					cell.setCellValue("repeatPageNum");
				}
				else if ("pageCount".equalsIgnoreCase(systemCode)) {
					cell.setCellValue(pageCount);
				}
			}
			// 数据库
			else if (type == 0) {
				// 建单日期
				if ("editdate".equals(systemCode))
				{
					String editdate = jsonObject.getString(systemCode);
					if (editdate != null && editdate.length() > 10)
					{
						editdate = editdate.substring(0, 10);
					}
					cell.setCellValue(editdate);
				} 
				// 订单类型
				else if ("sheettype".equals(systemCode))
				{
					String[] temp = templateName.split("_");
					String sheetTypeName = configs.getProperty(temp[0]);
					sheetTypeName = sheetTypeName == null ? "" : sheetTypeName;
					cell.setCellValue(sheetTypeName);
				} 
				// 打印公司名称
				else if ("ename".equals(systemCode)) 
				{
					cell.setCellValue(userJson.getString("ename"));
				} 
				else if (jsonObject.containsKey("giftflag") && "1".equals(jsonObject.getString("giftflag"))) 
				{
					// 赠品判断
					if ("bulkqty".equals(systemCode)) {
						cell.setCellValue(jsonObject.getString("bulkqty"));
					} 
					else if ("notes".equals(systemCode)) {
						cell.setCellValue("赠品");
					} 
					else {
						cell.setCellValue(jsonObject.getString(systemCode));
					}
				}
				// 普通字段
				else if (jsonObject.containsKey(systemCode)) {
					cell.setCellValue(jsonObject.getString(systemCode));
				}
				// 用户登录信息
				else if (null != userJson) {
					cell.setCellValue(userJson.getString(systemCode));
				}
			}
		}
		
		// 设置cell样式
		HSSFTools.setCellStyle(workbook, sheet, cell, cellObj,fonts,cellStyles);
	}

	/**
	 * 分页打印
	 * @param backDatas
	 * @throws Exception
	 */
	public void printExeclPage(JSONObject backDatas) throws Exception 
	{
		logger.debug("============= 分页打印  - start ===================");
		// item_0 表头数据数组
		JSONArray totalData = backDatas.getJSONArray("item_0");
		int sheeCount = totalData.size();
		JSONObject mainData;
		JSONArray itemsData;

		String execlSheetName = "订单号-%s";
		for (int sheetNum = 0; sheetNum < sheeCount; sheetNum++) 
		{
			rowNum = 1;
			rowIndex = 0;
			// 重置汇总项
			setAllSumsMapKey();
			mainData = totalData.getJSONObject(sheetNum);
			itemsData = backDatas.getJSONArray("item_" + (sheetNum + 1));
			generateSingleSheetRepeat(mainData, itemsData, String.format(execlSheetName, mainData.getString("sheetid")));
			logger.debug("============= 分页打印 - 正在打印... 订单号：{}",mainData.getString("sheetid"));
		}
		logger.debug("============= 分页打印  - end ===================");
	}
	
	/**
	 * 分页打印 
	 * @param mainData
	 * @param itemsData
	 * @param pageRows
	 * @param execlSheetName
	 * @throws Exception
	 */
	private void generateSingleSheetRepeat(JSONObject mainData, JSONArray itemsData, String execlSheetName) throws Exception 
	{
		this.itemBeginIndex = 0;
		this.isLastPage = false;
		this.repeatPageNum = 0;
		this.itemsData = retCountItems(itemsData);
		this.detailTotalRows = this.itemsData.size();
		// 计算总页数
		calculationPageCount();
		page(mainData, execlSheetName);
	}
	
	private void page(JSONObject mainData, String execlSheetName) throws Exception
	{
//		fonts = new HashMap<String, HSSFFont>();
//		cellStyles = new HashMap<String, HSSFCellStyle>();
		rowIndex = 0;
		repeatPageNum++;
		// 未打印row数
		int remainRows = detailTotalRows - itemBeginIndex;
		// 本页打印row数
		int pageRows = getPageRows(remainRows);
		// 本页打印json数据
		JSONArray newItemsData = new JSONArray();
		for (int i = itemBeginIndex; i < itemBeginIndex + pageRows; i++)
		{
			newItemsData.add(itemsData.getJSONObject(i));
		}
		// 是否最后一页
		if (remainRows <= pageRows) {
			isLastPage = true;
			doPagePrint(mainData, newItemsData, execlSheetName + "_" + repeatPageNum);
			return;
		}
		// 继续分页
		doPagePrint(mainData, newItemsData, execlSheetName + "_" + repeatPageNum);
		itemBeginIndex += pageRows;
		page(mainData, execlSheetName);
	}

	/**
	 * 获取本页打印row数
	 * @param remainRows 未打印条数
	 * @return
	 */
	private int getPageRows(int remainRows) {
		// 总数1页直接打印全部
		if (pageCount == 1) {
			return remainRows;
		}
		else {
			// 总页髙 - 上下边距
			double v = paperheight - ((setsJson.getDouble("toppadding")/10.0) + (setsJson.getDouble("bottompadding")/10.0));
			// 所有行髙和 cm （除去数据明细髙）
			double qt = HSSFUtil.pxToHeightCM(rowsHeight - datailHeight);
			// 表格标题髙 cm
			double bt = HSSFUtil.pxToHeightCM(reportTitleHeight);
			// 汇总高度 cm
			double hz = HSSFUtil.pxToHeightCM(accountHeight);
			// 数据明细髙cm
			double my = HSSFUtil.pxToHeightCM(datailHeight);
			
			if (repeatPageNum == 1) {
				// 第一页条数
				int oneNum = (int) ((v - qt + hz) / my);
				return remainRows > oneNum ? oneNum : remainRows;
			}
			if (repeatPageNum == detailTotalRows) {
				// 最后页条数
				int endNum = (int) ((v - qt + bt) / my); 
				return remainRows > endNum ? endNum : remainRows;
			}
			else {
				// 中间页条数
				int zjNum = (int) ((v -qt + bt + hz) / my);
				return remainRows > zjNum ? zjNum : remainRows;
			}
		}
	}
	
	/**
	 * 计算分页打印总页数
	 * @param itemsData
	 */
	private void calculationPageCount() 
	{
		// 总页髙 - 上下边距
		double v = paperheight - ((setsJson.getDouble("toppadding")/10.0) + (setsJson.getDouble("bottompadding")/10.0));
		// 所有行髙和 cm （除去数据明细髙）
		double qt = HSSFUtil.pxToHeightCM(rowsHeight - datailHeight);
		// 表格标题髙 cm
		double bt = HSSFUtil.pxToHeightCM(reportTitleHeight);
		// 汇总高度 cm
		double hz = HSSFUtil.pxToHeightCM(accountHeight);
		// 数据明细髙cm
		double my = HSSFUtil.pxToHeightCM(datailHeight);
		
		// 一页打完
		int num = (int) ((v -qt) / my);
		if (num > detailTotalRows) {
			this.pageCount = 1;
		}
		else {
			// 第一页条数
			int oneNum = (int) ((v - qt + hz) / my);
			// 最后页条数
			int endNum = (int) ((v - qt + bt) / my); 
			if ((oneNum + endNum) >= detailTotalRows) {
				this.pageCount = 2;
			}
			else {
				// 中间页条数
				int zjNum = (int) ((v -qt + bt + hz) / my);
				this.pageCount = (int) Math.ceil((detailTotalRows - oneNum - endNum)*1.0 / zjNum*1.0) + 2;
			}
		}
	}

	/**
	 * 分页打印 
	 * @param mainData
	 * @param itemsData
	 * @param execlSheetName
	 * @throws Exception
	 */
	private void doPagePrint(JSONObject mainData, JSONArray itemsData, String execlSheetName) throws Exception 
	{
		logger.debug("========== 正在分页打印：第{}页，开始行索引：{},是否最后一页：{}, 打印数量：{}=========",
				repeatPageNum, itemBeginIndex, isLastPage,itemsData.size());
		
		HSSFSheet sheet = workbook.getSheet(execlSheetName);
		if (sheet == null) 
		{
			sheet = workbook.createSheet(execlSheetName);
			// 设置模版打印基础信息
			HSSFTools.initPrintSytle(sheet, setsJson);
			// 设置sheet各列宽
			HSSFTools.setColumnWidth(sheet, colsJson);
		}
		
		// 开始循环模版json
		for (int i = 0, j = rowsJson.size(); i < j; i++) {
			JSONObject rowObj = rowsJson.getJSONObject(i);
			
			// 第一页才打印报表标题
			if (repeatPageNum != 1 && "reportTitle".equals(rowObj.getString("group"))) {
				continue;
			}
			// 是否数据明细特殊row
			if ("dataDetail".equals(rowObj.getString("group"))) {
				addDatailRows(sheet, itemsData, rowObj);
				continue;
			}
			// 最后页才打印汇总
			if (!isLastPage && "account".equals(rowObj.getString("group"))) 
			{
				continue;
			}	
			// 普通row
			addOneRow(sheet, mainData, rowObj);
		}
		// 添加本页分页符
		//sheet.setRowBreak(rowIndex - 1);
	}
	
	/**
	 * 重新计算商品数
	 * @param itemsData
	 * @return
	 */
	private JSONArray retCountItems(JSONArray itemsData)
	{
		DecimalFormat df = new DecimalFormat("######0.0000");
		// 详情数据arr
		JSONArray newItemsData = new JSONArray();
		for (int i = 0, len = itemsData.size(); i < len; i++) 
		{
			JSONObject temp = itemsData.getJSONObject(i);
			String bulkqtyValue = df.format( temp.getDouble("bulkqty"));
			temp.put("bulkqty", bulkqtyValue);
			
			newItemsData.add(temp);
			
			// 如果为手动赠送,复制一条记录
			if (temp.containsKey("giftflag") && "0".equals(temp.getString("giftflag")))
			{
				double giftqty = temp.getDouble("giftqty");
				if (giftqty > 0) 
				{
					JSONObject gift = temp;
					double packunitqty = temp.getDouble("packunitqty");
					String bulkqty = df.format( (giftqty % packunitqty));
				    String packqty = df.format((giftqty / packunitqty));
					gift.put("bulkqty", bulkqty);
					gift.put("packqty", packqty);
					gift.put("giftflag", 1);
					gift.put("packprice", 0);
					gift.put("bulkprice", 0);
					gift.put("summoney", 0);
					gift.put("sumqty", giftqty);
					newItemsData.add(gift);
				}
			}
		}

		// 收送赠品处理
		for (int i = 0, len = newItemsData.size(); i < len; i++)
		{
			JSONObject temp = newItemsData.getJSONObject(i);
			// 如果为赠品,把价格等置0
			if (temp.containsKey("giftflag") && "1".equals(temp.getString("giftflag")))
			{
				double giftqty = temp.getDouble("giftqty");
				double packunitqty = temp.getDouble("packunitqty");
				String bulkqty = df.format( (giftqty % packunitqty));
				int packqty = (int)Math.floor((giftqty / packunitqty));
				temp.put("bulkqty", bulkqty);
				temp.put("packqty", packqty);
				temp.put("packprice", 0);
				temp.put("bulkprice", 0);
				temp.put("sumqty", giftqty);
				temp.put("summoney", 0);
			}
		}

		JSONArray result = new JSONArray();
		int bulkQty = 0;
		int packqty = 0;
		for (int i = 0, len = newItemsData.size(); i < len; i++)
		{
			JSONObject temp = newItemsData.getJSONObject(i);
			// 商品同时为0 不输出
			bulkQty = getJsonValueInt(temp, "bulkqty", 0);
			packqty = getJsonValueInt(temp, "packqty", 0);
			if (bulkQty == 0 && packqty == 0) 
			{
				continue;
			}
			result.add(temp);
		}
		logger.debug("商品明细计算前后值 计算前={}.计算后={} ", itemsData.size(), result.size());
		return result;
	}

	public Integer getJsonValueInt(JSONObject temp, String key, Integer defaultVal) 
	{
		try 
		{
			if (!temp.containsKey(key)) 
			{
				return defaultVal;
			}
			return temp.getInt(key);
		} 
		catch (Exception e) 
		{
			return defaultVal;
		}
	}
	
	/**
	 * 统计/重置  页注脚项
	 */
	public void setPageSumsMapKey() {
		// 开始循环模版json
		for (int i = 0, j = rowsJson.size(); i < j; i++) {
			JSONObject rowObj = rowsJson.getJSONObject(i);
			// 是否页注脚
			if ("pageFoot".equals(rowObj.getString("group"))) {
				JSONArray list = rowObj.getJSONArray("list");
				for (int k = 0, p = list.size(); k < p; k++) {
					JSONObject obj = list.getJSONObject(k);
					int type = obj.getInt("type");
					// 数据库数据统计
					if (type == 0) {
						pageSums.put(obj.getString("systemCode").toLowerCase(), BigDecimal.ZERO);
					}
				}
			}
		}
	}
	
	/**
	 * 统计/重置 汇总项
	 */
	public void setAllSumsMapKey() {
		// 开始循环模版json
		for (int i = 0, j = rowsJson.size(); i < j; i++) {
			JSONObject rowObj = rowsJson.getJSONObject(i);
			// 是否汇总
			if ("account".equals(rowObj.getString("group"))) {
				JSONArray list = rowObj.getJSONArray("list");
				for (int k = 0, p = list.size(); k < p; k++) {
					JSONObject obj = list.getJSONObject(k);
					int type = obj.getInt("type");
					// 数据库数据统计
					if (type == 0) {
						allSums.put(obj.getString("systemCode").toLowerCase().split(",")[0], BigDecimal.ZERO);
					}
				}
			}
		}
	}
	
	/**
	 * 判断是否正确的合计参数
	 * @param str
	 * @return
	 */
	private boolean isTrueData(String str)
	{
		if (null == str || "".equals(str) || (str.indexOf("+") != -1)
				|| (str.indexOf("-") != -1) || (str.indexOf("*") != -1)
				|| (str.indexOf("/") != -1) || (str.indexOf("%") != -1)) {
			return false;
		}
		String reg = "[\u4e00-\u9fa5]";
		Pattern pat = Pattern.compile(reg);
		Matcher mat = pat.matcher(str);
		String repickStr = mat.replaceAll("");
		return str.equals(repickStr);
	}

	/**
	 * 特殊处理单元格
	 * @param value
	 * @param handleType
	 * @param cell
	 */
	private void handleSpecialField(String value, String handleType, HSSFCell cell)
	{
		String _temp = "";
		if ("moneyUpperCase".toLowerCase().equals(handleType))
		{
			// 钱大写
			_temp = MoneyUtils.number2CNMontrayUnit(new BigDecimal(value));
			cell.setCellValue(_temp);
		} 
		else if ("moneyLowerCase".toLowerCase().equals(handleType))
		{
			// 钱小写
			_temp = MoneyUtils.moneyFilter(Double.parseDouble(value));
			cell.setCellValue("￥" + _temp);
		}
	}
	
	public HSSFWorkbook getWorkbook() {
		return workbook;
	}

	public void setWorkbook(HSSFWorkbook workbook) {
		this.workbook = workbook;
	}

	public JSONObject getSetsJson() {
		return setsJson;
	}

	public void setSetsJson(JSONObject setsJson) {
		this.setsJson = setsJson;
	}

	public int getDatailHeight() {
		return datailHeight;
	}

	public void setDatailHeight(int datailHeight) {
		this.datailHeight = datailHeight;
	}

	public int getDetailTotalRows() {
		return detailTotalRows;
	}

	public void setDetailTotalRows(int detailTotalRows) {
		this.detailTotalRows = detailTotalRows;
	}

	public double getPaperheight() {
		return paperheight;
	}

	public void setPaperheight(double paperheight) {
		this.paperheight = paperheight;
	}

	public double getPaperwidth() {
		return paperwidth;
	}

	public void setPaperwidth(double paperwidth) {
		this.paperwidth = paperwidth;
	}

	public int getRowsHeight() {
		return rowsHeight;
	}

	public void setRowsHeight(int rowsHeight) {
		this.rowsHeight = rowsHeight;
	}

	

}
