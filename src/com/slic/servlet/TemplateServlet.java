package com.slic.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.slic.entity.ResponseMsgEntity;
import com.slic.enums.ResponseCodeEnum;
import com.slic.service.TemplateService;

public class TemplateServlet extends HttpServlet {
	
	private TemplateService templateService = new TemplateService();
	
	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// 操作类型,格式化/解析
		String optType = req.getParameter("optType");
		// 模板名称
		String templateName = req.getParameter("templateName");
		// 用户编号
		String eid = req.getParameter("eid");
		// 设置响应为json
		resp.setCharacterEncoding("UTF-8");  
		resp.setContentType("application/json; charset=utf-8");
		ResponseMsgEntity responseMsgEntity = null;
		if ("format".equals(optType)) {
	        String pattern = req.getParameter("patternstr");
	        String addType = req.getParameter("addType");
			try {
				// 新增/修改模版
				String newTemplateName = templateService.formatTemplate(pattern,templateName,addType,eid);
				responseMsgEntity = new ResponseMsgEntity(ResponseCodeEnum.SUCCESS);
				responseMsgEntity.setResponseText(newTemplateName);
			} catch (Exception e) {
				e.printStackTrace();
				responseMsgEntity = new ResponseMsgEntity(ResponseCodeEnum.ERROR);
			}
		} else if("parse".equals(optType)) {
			// 读取模板
		    //String isDefault = req.getParameter("isDefault");
			try {
				String resText = templateService.parseTemplate(templateName,eid);
				responseMsgEntity = new ResponseMsgEntity(ResponseCodeEnum.SUCCESS);
                responseMsgEntity.setResponseText(resText);
			} catch (Exception e) {
				e.printStackTrace();
				responseMsgEntity = new ResponseMsgEntity(ResponseCodeEnum.ERROR);
			}
		}else if("delete".equals(optType)){
			// 删除模板
			try {
				templateService.deleteTemplate(templateName,eid);
				responseMsgEntity = new ResponseMsgEntity(ResponseCodeEnum.SUCCESS);
                responseMsgEntity.setResponseText("删除打印模板成功!");
			} catch (Exception e) {
				e.printStackTrace();
				responseMsgEntity = new ResponseMsgEntity(ResponseCodeEnum.ERROR);
			}
		}
		
		PrintWriter writer = resp.getWriter();
		writer.print(JSONObject.fromObject(responseMsgEntity).toString());
		writer.close();
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

}
