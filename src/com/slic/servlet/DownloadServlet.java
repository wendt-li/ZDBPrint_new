/*
 * 文 件 名:  DownloadServlet.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月21日
 */
package com.slic.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.slic.dao.SocketClient;
import com.slic.utils.ExcelUtils1;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月21日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class DownloadServlet extends HttpServlet
{
   /** {@inheritDoc} */
 
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        req.setCharacterEncoding("utf-8");
        // 导出模板
        String exportPattern = req.getParameter("exportPattern");
        // 查询数据
        String queryData = req.getParameter("queryData");
        
        // 设置返回excel
        resp.setContentType("application/vnd.ms-excel;charset=UTF-8");  
        resp.setHeader("Content-disposition", "attachment;filename=\""+ new String(("服务项目.xls").getBytes("GBK"),"ISO-8859-1") + "\"");
        
        // 映射关系
        Map<String,String> map = new HashMap<String,String>();
        map.put("grid", "response.item_0");
        
        String source = SocketClient.send("127.0.0.1", 9601, queryData);//127.0.0.1 192.168.10.187
        
        ExcelUtils1.createExcel("测试导出",exportPattern,map,source,resp.getOutputStream());
        
    }
    
    /** {@inheritDoc} */
         
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException
    {
        this.doGet(req, resp);
    }
}
