package com.slic.utils;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;

public class HSSFTools{
	/**
	 * 设置HSSFCellStyle 样式
	 * @param cell
	 * @param fonts 
	 * @param cellStyles 
	 * @param sytleObj
	 */
	public static void setCellStyle(HSSFWorkbook workbook, HSSFSheet sheet, HSSFCell cell, JSONObject cellObj, Map<String, HSSFFont> fonts, Map<String, HSSFCellStyle> cellStyles) 
	{
		// 所在行序
		int newRow = cell.getRowIndex();
		// 本该开始行序
		int startRow = cellObj.getInt("row");
		// 本该结束行序
		int endRow = cellObj.getInt("endRow");
		int startCol = cellObj.getInt("col");
		int endCol = cellObj.getInt("endCol");
	//	System.out.println("=======!!!!!!!=========现在行xu:"+newRow+" 开始row："+startRow+"结束row："+endRow+"开始cell："+startCol+"结束cell:"+endCol);
		CellRangeAddress cellRangeAddress;
		if (newRow > startRow) {
			// 相差行序
			int ibs = newRow - startRow;
			cellRangeAddress = new CellRangeAddress((startRow + ibs), (endRow + ibs), startCol, endCol);
		} else {
			cellRangeAddress = new CellRangeAddress(startRow, endRow, startCol, endCol);
		}
		// 合并单元格居中
		sheet.addMergedRegion(cellRangeAddress);

		String key = cellObj.getString("row") + "-" + cellObj.getString("col");
		JSONObject sytleObj = cellObj.getJSONObject("style");
		// HSSFCellStyle 设定单元格风格;
		HSSFCellStyle style = setCellStyle(workbook, sytleObj, key,fonts,cellStyles);
		cell.setCellStyle(style);
		
		setBorder(cellObj, cellRangeAddress, sheet, workbook);
	}
	
	/**
	 * 设置边框
	 * @param cellObj
	 * @param cellRangeAddress
	 * @param sheet
	 * @param wb
	 */
	public static void setBorder(JSONObject cellObj, CellRangeAddress cellRangeAddress, HSSFSheet sheet, HSSFWorkbook wb) {
		// 设置合并边框
		String bd = cellObj.getJSONObject("style").getString("border");
		if ("bdBottom".equalsIgnoreCase(bd) || "bdAll".equalsIgnoreCase(bd)) {
			RegionUtil.setBorderBottom(1, cellRangeAddress, sheet, wb);
		}
		if ("bdTop".equalsIgnoreCase(bd) || "bdAll".equalsIgnoreCase(bd)) {
			RegionUtil.setBorderTop(1, cellRangeAddress, sheet, wb);
		}
		if ("bdLeft".equalsIgnoreCase(bd) || "bdAll".equalsIgnoreCase(bd)) {
			RegionUtil.setBorderLeft(1, cellRangeAddress, sheet, wb);
		}
		if ("bdRight".equalsIgnoreCase(bd) || "bdAll".equalsIgnoreCase(bd)) {
			RegionUtil.setBorderRight(1, cellRangeAddress, sheet, wb);
		}
	}

	private static HSSFCellStyle setCellStyle(HSSFWorkbook workbook, JSONObject sytleObj, String key, Map<String, HSSFFont> fonts, Map<String, HSSFCellStyle> cellStyles) {
		HSSFCellStyle cellStyle = null;
		if (cellStyles == null) 
		{
			cellStyles = new HashMap<String, HSSFCellStyle>();
		}
		else 
		{
			cellStyle = cellStyles.get(key);
		}
		
		if (cellStyle == null)
		{
			cellStyle = workbook.createCellStyle();
			cellStyles.put(key, cellStyle);
		} 
		else 
		{
			cellStyle = cellStyles.get(key);
		}

		// 单元格垂直对齐方式 0 居上 1 居中 2 居下 3 正当
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		// 单元格水平对齐方式 0 普通 1 左对齐 2 居中 3 右对齐 4 填充 5 正当 6 居中选择
		short align = 0;
		String textAlign = sytleObj.getString("textAlign");
		if ("left".equalsIgnoreCase(textAlign)) {
			align = HSSFCellStyle.ALIGN_LEFT;
		} else if ("right".equalsIgnoreCase(textAlign)) {
			align = HSSFCellStyle.ALIGN_RIGHT;
		} else if ("center".equalsIgnoreCase(textAlign)) {
			align = HSSFCellStyle.ALIGN_CENTER;
		}
		cellStyle.setAlignment(align);
		// 设置字体
		HSSFFont font = setFont(workbook,sytleObj,key,fonts);
		cellStyle.setFont(font);

		return cellStyle;
	}
	/**
	 * 设置HSSFFont 样式
	 * @param sytleObj
	 * @param fonts 
	 * @return
	 */
	private static HSSFFont setFont(HSSFWorkbook workbook, JSONObject sytleObj, String key, Map<String, HSSFFont> fonts) {
		if (fonts == null) 
		{
			fonts = new HashMap<String, HSSFFont>();
		}
		HSSFFont font;
		font = fonts.get(key);
		if (font != null)
		{
			return font;
		}
		
		font = workbook.createFont();
		fonts.put(key, font);
		
		font.setFontName("宋体");
		// HSSFFont 创建 xls 中的字体;
		font.setFontHeightInPoints((short) sytleObj.getInt("size")); //字体高度
        //font.setColor(HSSFFont.COLOR_RED); //字体颜色
		font.setBoldweight(sytleObj.getBoolean("bold") ? HSSFFont.BOLDWEIGHT_BOLD : HSSFFont.BOLDWEIGHT_NORMAL); //粗细
        font.setFontName(sytleObj.getString("family")); //字体
		// 设定为斜体;
		font.setItalic(sytleObj.getBoolean("italic"));
		// 设定文字删除线;
		//font.setStrikeout(true);
		//下划线
		font.setUnderline(sytleObj.getBoolean("underLine") ? HSSFFont.U_SINGLE : HSSFFont.U_NONE);
		return font;
	}
	
	/**
	 * 设置每列列宽
	 * @param colsJson 
	 */
	public static void setColumnWidth(HSSFSheet sheet, JSONArray colsJson) {
		for (int i = 0, j = colsJson.size(); i < j; i++) {
			int v = HSSFUtil.cmToSheetWidthUnit(HSSFUtil.pxTo40CM(colsJson.getJSONObject(i).getInt("width")));
			sheet.setColumnWidth(i, v);
		}
	}
	
	/**
	 * 设置模版打印基础信息
	 */
	public static void initPrintSytle(HSSFSheet sheet, JSONObject setsJson) 
	{
		sheet.setMargin(HSSFSheet.TopMargin, HSSFUtil.mmToSheetMarginUnit(setsJson.getInt("toppadding")));// 页边距（上）
		sheet.setMargin(HSSFSheet.BottomMargin, HSSFUtil.mmToSheetMarginUnit(setsJson.getInt("bottompadding")));// 页边距（下）
		sheet.setMargin(HSSFSheet.LeftMargin, HSSFUtil.mmToSheetMarginUnit(setsJson.getInt("leftpadding")));// 页边距（左）
		sheet.setMargin(HSSFSheet.RightMargin, HSSFUtil.mmToSheetMarginUnit(setsJson.getInt("rightpadding")));// 页边距（右）
		// 设置打印方向，true：横向，false：纵向(默认)
		HSSFPrintSetup ps = sheet.getPrintSetup();
		ps.setLandscape(setsJson.getInt("printdirection") == 1) ; 
		// // 实施自定义,使用打印机默认打印纸
		// ps.setPaperSize(HSSFPrintSetup.PRINTER_DEFAULT_PAPERSIZE) ; // 纸张类型
		// 打印比例
		ps.setScale((short)setsJson.getInt("paperscale"));
	}

	/**
	 * 标签打印样式设置
	 * @param workbook
	 * @param sheet
	 * @param cell
	 * @param cellObj
	 * @param dv_row
	 * @param dv_col
	 * @param zirow
	 * @param zicol
	 * @param cellStyles 
	 * @param fonts 
	 */
	public static void setCellStyle(HSSFWorkbook workbook, HSSFSheet sheet,
			HSSFCell cell, JSONObject cellObj, int dv_row, int dv_col,
			int zirow, int zicol, Map<String, HSSFFont> fonts,
			Map<String, HSSFCellStyle> cellStyles) {
		
		int startRow = cellObj.getInt("row") + (dv_row * zirow);
		int endRow = cellObj.getInt("endRow") + (dv_row * zirow);
		int startCol = cellObj.getInt("col") + (dv_col * zicol);
		int endCol = cellObj.getInt("endCol") + (dv_col * zicol);

		CellRangeAddress cellRangeAddress = new CellRangeAddress(startRow, endRow, startCol, endCol);
		sheet.addMergedRegion(cellRangeAddress);

		String key = cellObj.getString("row") + "-" + cellObj.getString("col");
		JSONObject sytleObj = cellObj.getJSONObject("style");
		// HSSFCellStyle 设定单元格风格;
		HSSFCellStyle style = setCellStyle(workbook, sytleObj, key, fonts, cellStyles);
		cell.setCellStyle(style);

		setBorder(cellObj, cellRangeAddress, sheet, workbook);
	}
	   
}
