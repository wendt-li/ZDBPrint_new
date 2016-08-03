package com.slic.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slic.print.config.PrintConstants;
import com.slic.print.enity.PrintMargin;
import com.slic.print.enity.PrintPage;
import com.slic.print.enity.PrintPageDetail;
import com.slic.print.enity.PrintPageFooter;
import com.slic.print.enity.PrintPageHeader;
import com.slic.print.enity.PrintPageRow;
import com.slic.print.enity.PrintPageSummary;
import com.slic.utils.ElementUtils;

/**
 * 打印模版工具Service
 * 
 * @author li
 * @Date  2016年7月22日
 * @notes   
 *
 */
public class TemplateService{

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 新增/修改模版
	 * @param pattern
	 * @param templateName
	 * @param addType
	 * @param eid
	 * @return
	 * @throws Exception
	 */
	public String formatTemplate(String pattern, String templateName,
			String addType, String eid) throws Exception {

		// 修改模板需要判断
		String tempPath = getTemplatePath() + eid;

		// 判断该用户下是否有相应的目录
		File file = new File(tempPath);
		if (!file.exists()) {
			file.mkdirs();
		}

		// 判断是新增还是修改
		if ("new".equals(addType)) {
			if (templateName != null && templateName.indexOf("_") > -1) 
			{
				templateName = templateName.substring(0,templateName.indexOf("_"));
			}
			templateName += "_" + System.currentTimeMillis();
		}
		String filepath = tempPath+"/"+templateName+".json";
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(filepath));
		bw.write(pattern);
		bw.close();
		return templateName;

	}
	
	/**
	 * 获取模版信息
	 * @param templateName
	 * @param eid
	 * @return
	 * @throws Exception
	 */
	public String parseTemplate(String templateName,String eid) throws Exception 
	{
		String tempPath = getTemplatePath() + eid + "/" + templateName + ".json";
		// 判断该用户下是否有相应的目录
		File file = new File(tempPath);
		if (!file.exists())
		{
			if (templateName != null && templateName.indexOf("_") > -1) 
			{
				tempPath = getTemplatePath() + eid + "/" + templateName.substring(0,templateName.indexOf("_")) + ".json";
			}
			else {
				tempPath = getTemplatePath() + eid + "/" + templateName + ".json";
			}
		}
		file = new File(tempPath);
		// 判断用户目录下是否有对应的文件
		if (!file.exists())
		{
			// 没有对应的文件就读取默认模板
			if (templateName != null && templateName.indexOf("_") > -1)
			{
				tempPath = getTemplatePath() + templateName.substring(0,templateName.indexOf("_")) + ".json";
			}
			else {
				tempPath = getTemplatePath() + templateName + ".json";
			}
		}
		
		BufferedReader br = new BufferedReader(new FileReader(tempPath));
		String data = "";
		char[] chs = new char[1024];
		int len = 0;
		while ((len = br.read(chs)) != -1) {
			data += new String(chs, 0, len);
		}
		br.close();
		return data;
	}

	/** 
	 * 删除模板
	 * @param templateName
	 * @param eid
	 * @return
	 */
	public void deleteTemplate(String templateName, String eid) {
		String tempPath = getTemplatePath()+eid + "/" + templateName+".json";;
		File file =  new File(tempPath);
		if(file.exists()){
			file.delete();
		}
	}
	
	/**********************************************************************/
	
	public Element getRootElement(String templateName, JSONObject userJson)
			throws Exception {
		SAXBuilder builder = new SAXBuilder();
		// 获取打印模版xml
		File file = getFile(templateName, userJson.getString("eid"));
		// 解析xml文件
		Document parse = builder.build(file);
		// 获取xml文件根节点
		Element pageXml = parse.getRootElement();
		return pageXml;
	}

	/**
	 * 获取打印模板文件对象 
	 * @param templateName 模板名称
	 * @param eid 企业编号
	 * @return
	 */
	public File getFile(String templateName, String eid)
	{
		String tempPath = getTemplatePath() + eid;
		// 判断该用户下是否有相应的目录
		File file = new File(tempPath);
		String filepath = "";
		if (file.exists())
		{
			// 读相应用户目录下的
			filepath = tempPath + "/" + templateName + ".xml";
		}
		else 
		{
			if (templateName != null && templateName.indexOf("_") > -1) 
			{
				templateName = templateName.substring(0,
						templateName.indexOf("_"));
			}
			filepath = getTemplatePath() + templateName + ".xml";
		}

		file = new File(filepath);
		// 判断用户目录下是否有对应的文件
		if (!file.exists())
		{
			// 没有对应的文件就读取默认模板
			if (templateName != null && templateName.indexOf("_") > -1)
			{
				templateName = templateName.substring(0,
						templateName.indexOf("_"));
			}
			filepath = getTemplatePath() + templateName + ".xml";
			file = new File(filepath);
		}
		
		return file;

	}

	/**
	 * 获取模板路径
	 * @return
	 */
	public String getTemplatePath() 
	{
		File file = new File(ExportExcelService.class.getClassLoader().getResource("").getPath());
		return file.getParentFile().getParentFile().getAbsolutePath() + "/print_template/";
	}
	
	/**
	 * 设置模版信息
	 * @return
	 * @throws Exception
	 */
	public PrintPage setTemplate(Element pageXml) throws Exception {
		// TODO Auto-generated method stub
		logger.info("============= 初始化模版信息 - start =============");
		// 取纸张边距单位 mm
		Double dou = ElementUtils.getValueDou(pageXml, "toppadding", 0d) * 10d;
		int top = dou.intValue();

		dou = ElementUtils.getValueDou(pageXml, "bottompadding", 0d) * 10d;
		int bottom = dou.intValue();

		dou = ElementUtils.getValueDou(pageXml, "leftpadding", 0d) * 10d;
		int left = dou.intValue();

		dou = ElementUtils.getValueDou(pageXml, "rightpadding", 0d) * 10d;
		int right = dou.intValue();

		if (bottom < PrintConstants.MIN_MARGIN_BOTTON) {
			bottom = PrintConstants.MIN_MARGIN_BOTTON;
		}
		// 设置页边距
		PrintMargin margin = new PrintMargin(top, right, bottom, left);

		dou = ElementUtils.getValueDou(pageXml, "paperwidth", PrintConstants.PAGE_DEFAULT_WIDTH) * 100d;
		int paperwidth = dou.intValue(); // 2100
		dou = ElementUtils.getValueDou(pageXml, "paperheight", PrintConstants.PAGE_DEFAULT_HEIGHT) * 100d;
		int paperheight = dou.intValue(); // 1390

		// 设置每页高度、宽度、内间距
		PrintPage tplPrintPage = new PrintPage(paperheight, paperwidth, margin);

		PrintPageRow rowtt = new PrintPageRow();
		rowtt.setHeight(tplPrintPage.getMargin().getTop());
		tplPrintPage.setTopRow(rowtt);
		rowtt.setHeight(tplPrintPage.getMargin().getBottom());
		tplPrintPage.setBottomRow(rowtt);

		// 打印比率设置
		int printScale = ElementUtils.getValueInt(pageXml, "paperscale", PrintConstants.PAGE_DEFAULT_SCALE);
		tplPrintPage.setPrintScale(printScale);
		// 打印方向 0- 纵向 1-横向
		int printdirection = ElementUtils.getValueInt(pageXml, "printdirection", PrintConstants.PAGE_DEFAULT_PRINTDIRECTION);
		tplPrintPage.setPrintDirection(printdirection);
		logger.info("============= 初始化模版信息 - end =============");
		return tplPrintPage;
		
	}

	/**
	 * 打印模版信息
	 * @throws Exception
	 */
	public void PrintTemplateData(PrintPage tplPrintPage) throws Exception {
		// TODO Auto-generated method stub
		logger.debug("打印模板信息\n");
		if (tplPrintPage == null)
		{
			logger.debug("模板为空\n");
			return;
		}
		logger.debug( "模板宽高(0.1mm) pageWidth={},pageHeight={},isPagination={} PrintScale = {}\n", tplPrintPage.getWidth(), tplPrintPage.getHeight(),
				tplPrintPage.isPagination(), tplPrintPage.getPrintScale());
		
		logger.debug("模板边距(0.1mm) top={},bottom={},left={},right={}\n", tplPrintPage.getMargin().getTop(), tplPrintPage.getMargin().getBottom(), tplPrintPage.getMargin().getLeft(),
				tplPrintPage.getMargin().getRight());
		
		logger.debug("模板打印方向: 0- 纵向 1-横向 printdiret={},bottom={},left={},right={}\n", tplPrintPage.getPrintDirection());
		
		logger.debug("模板打印 topHeight={},bottomHeight={},left={},right={}\n", tplPrintPage.getTopRow().getHeight(), tplPrintPage.getBottomRow().getHeight());

		List<PrintPageRow> rows = tplPrintPage.getHeader().getRows();
		PrintPageRow row;
		
		logger.debug("打印页头每行数据： height={}  是否重打：isPrintOnNewPage = {}\n", tplPrintPage.getHeader().getHeight(), tplPrintPage.getHeader().isPrintOnNewPage());
		for (int i = 0; i < rows.size(); i++) 
		{
			row = rows.get(i);
			logger.debug(" 第{}行;height={} width={} {}", i, row.getHeight(),row.getWidth(), row);
		}
		logger.debug("打印页头结束 \n");

		rows = tplPrintPage.getFooter().getRows();
		logger.debug("打印页脚每行数据 height={} 是否重打：isPrintOnNewPage = {}\n", tplPrintPage.getFooter().getHeight(), tplPrintPage.getFooter().isPrintOnNewPage());
		for (int i = 0; i < rows.size(); i++)
		{
			row = rows.get(i);
			logger.debug(" 第{}行;height={} width={}", i, row.getHeight(),row.getWidth(), row);
		}
		logger.debug("打印页脚结束 \n");

		rows = tplPrintPage.getDetail().getRows();
		logger.debug("打印页明细每行数据 height={} \n",tplPrintPage.getDetail().getHeight());
		for (int i = 0; i < rows.size(); i++)
		{
			row = rows.get(i);
			logger.debug(" 第{}行;height={} width={} {}", i, row.getHeight(), row.getWidth(), row);
		}
		logger.debug("打印页明细结束 \n");

		rows = tplPrintPage.getSummary().getRows();
		logger.debug("打印页汇总每行数据 height={} isPrintOnNewPage = {}\n", tplPrintPage.getSummary().getHeight());
		for (int i = 0; i < rows.size(); i++) 
		{
			row = rows.get(i);
			logger.debug(" 第{}行;height={} width={} {}", i, row.getHeight(), row.getWidth(), row);
		}
		logger.debug("打印页汇总结束 \n");
		
		logger.debug("打印模板信息结束 \n");
	}

	/**
	 *  设置模版内容信息：header footer detail summary
	 * @param pageXml
	 * @param tplPrintPage
	 * @return
	 * @throws Exception
	 */
	public PrintPage setTemplateData(Element pageXml, PrintPage tplPrintPage) throws Exception {
		// TODO Auto-generated method stub
		logger.info("============= 初始化打印内容 - strat =============");
		// 标准行高
		int gridRowHeight = PrintConstants.PAGE_BASE_ROW_HEIGHT;
		int titleRowHeight = (int) (gridRowHeight * 1.5);
		int otherRowHeight = (int) (gridRowHeight * 1.0);

		PrintPageHeader header = new PrintPageHeader();
		header.setRows(new ArrayList<PrintPageRow>());
		PrintPageDetail detail = new PrintPageDetail();
		detail.setRows(new ArrayList<PrintPageRow>());
		PrintPageSummary summary = new PrintPageSummary();
		summary.setRows(new ArrayList<PrintPageRow>());
		PrintPageFooter footer = new PrintPageFooter();
		footer.setRows(new ArrayList<PrintPageRow>());

		// 是否重复打印
		boolean isPrintOnNewPage = false;
		// 有效行
		int valiRows = 0;

		// 解析行数据
		List<Element> rows = pageXml.getChildren();
		
		if (rows == null || rows.isEmpty()) {
			throw new Exception("空模板,不允许打印");
		}
		// 是否header部
		boolean falg = true;
		for (int i = 0; i < rows.size(); i++) {
			// 节点
			Element rowEL = rows.get(i);
			// 如果为空节点直接跳过此次循环
			if (rowEL == null) {
				continue;
			}
			PrintPageRow printPageRow = new PrintPageRow();
			printPageRow.setRowEle(rowEL);
			valiRows++;

			// 第一第二行的高度为1.5标题倍
			if (i < 2) {
				printPageRow.setHeight(titleRowHeight);
			} else {
				printPageRow.setHeight(otherRowHeight);
			}

			// 判断是否包含表格(表格不可能为前两行)
			if (rowHasTable(rowEL)) {
				// 表格不允许重打
				isPrintOnNewPage = false;
				printPageRow.setPrintOnNewPage(isPrintOnNewPage);
				printPageRow.setRowType(PrintConstants.RowType.DETAIL);
				printPageRow.setHeight(gridRowHeight);
				detail.getRows().add(printPageRow);
				
				i++;
				
				Element el = rows.get(i);
				// 是否有页合计
				if (rowHasPageSum(el)) {
					printPageRow = new PrintPageRow();
					printPageRow.setRowEle(rows.get(i));
					printPageRow.setHeight(otherRowHeight);
					printPageRow.setPrintOnNewPage(isPrintOnNewPage);
					printPageRow.setRowType(PrintConstants.RowType.SUMMARY);
					summary.getRows().add(0,printPageRow);
					i++;
				}
				// 添加汇总行
				printPageRow = new PrintPageRow();
				printPageRow.setRowEle(rows.get(i));
				printPageRow.setHeight(otherRowHeight);
				printPageRow.setPrintOnNewPage(isPrintOnNewPage);
				printPageRow.setRowType(PrintConstants.RowType.SUMMARY);
				summary.getRows().add(0,printPageRow);
				
				falg = false;
				continue;
			}
			/*// 是否有页合计
			if (rowHasPageSum(rowEL)) {
				printPageRow.setPrintOnNewPage(false);
				printPageRow.setRowType(PrintConstants.RowType.SUMMARY);
				summary.getRows().add(0,printPageRow);
				continue;
			}
			// 是否有总合计
			if (rowHasAllSum(rowEL)) {
				printPageRow.setPrintOnNewPage(false);
				printPageRow.setRowType(PrintConstants.RowType.SUMMARY);
				summary.getRows().add(0,printPageRow);
				continue;
			}*/
			
			String repeatflag = rowEL.getAttributeValue("repeatflag");
			isPrintOnNewPage = repeatflag != null && "true".equals(repeatflag);
			printPageRow.setPrintOnNewPage(isPrintOnNewPage);
			
			if (falg)
			{
				printPageRow.setRowType(PrintConstants.RowType.HEDER);
				header.getRows().add(printPageRow);
			}
			else
			{
				printPageRow.setRowType(PrintConstants.RowType.FOOTER);
				footer.getRows().add(printPageRow);
			}
		}
		tplPrintPage.setHeader(header);
		tplPrintPage.setDetail(detail);
		tplPrintPage.setSummary(summary);
		tplPrintPage.setFooter(footer);
		
		if (valiRows == 0)
		{
			throw new Exception("行文本内容不存在或为空,不允许打印！");
		}
		logger.info("============= 初始化打印内容 - end =============");
		return tplPrintPage;
	}
	
	/**
	 * 行是否包含表格
	 * @param prevObj
	 * @return
	 */
	private boolean rowHasTable(Element currentRow)
	{
		if (currentRow == null)
		{
			return false;
		}
		List<Element> childs = currentRow.getChildren("table");
		return !(childs == null || childs.isEmpty());
	}
	
	/**
	 * 行是否页合计
	 * @param prevObj
	 * @return
	 */
	private boolean rowHasPageSum(Element currentRow)
	{
		if (currentRow == null)
		{
			return false;
		}
		List<Element> childs = currentRow.getChildren("cell");
		String str = childs.get(0).getAttributeValue("systemcode");
		int i = str.indexOf("pageSum");
		return i > -1;
	}
	
	/**
	 * 行是否页合计
	 * @param prevObj
	 * @return
	 *//*
	private boolean rowHasAllSum(Element currentRow)
	{
		if (currentRow == null)
		{
			return false;
		}
		List<Element> childs = currentRow.getChildren("cell");
		String str = childs.get(0).getAttributeValue("systemcode");
		int i = str.indexOf("allSum");
		return i > -1;
	}*/
}
