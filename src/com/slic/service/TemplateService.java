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
	
	/**
	 * 获取模板路径
	 * @return
	 */
	public String getTemplatePath() 
	{
		File file = new File(ExportExcelService.class.getClassLoader().getResource("").getPath());
		return file.getParentFile().getParentFile().getAbsolutePath() + "/print_template/";
	}
	
	
}
