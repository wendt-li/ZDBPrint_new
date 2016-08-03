package com.slic.servlet;

import java.io.File ;
import java.io.FileInputStream ;
import java.io.IOException ;
import java.io.InputStream ;
import java.io.OutputStream ;

import javax.servlet.ServletException ;
import javax.servlet.http.HttpServlet ;
import javax.servlet.http.HttpServletRequest ;
import javax.servlet.http.HttpServletResponse ;

public class DownloadTemplateServlet extends HttpServlet{
	/** {@inheritDoc} */
	 
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		req.setCharacterEncoding("utf-8");
		// 导出名称
		String exportName = req.getParameter("exportName");
		// 导出类型
		String exportType = req.getParameter("exportType");
		
		String fileName = exportName+"."+exportType;
		
		// 设置返回excel
        resp.setContentType("application/vnd.ms-excel;charset=UTF-8");  
        resp.setHeader("Content-disposition", "attachment;filename=\""+ new String((fileName).getBytes("UTF-8"),"ISO-8859-1") + "\"");
        
        String path = getTemplatePath()+fileName;
        File file = new File(path);
        InputStream in = new FileInputStream(file);
        OutputStream out = resp.getOutputStream();
        
        byte[] buffer = new byte[1024];
        int index = -1;
        while( (index = in.read(buffer)) > -1 ){
        	out.write(buffer,0,index);
        }
        out.close();
        in.close();
	}
	
	/** {@inheritDoc} */
	 
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doPost(req, resp) ;
	}
	
	/**
     * 获取模板路径<一句话功能简述>
     * <功能详细描述>
     * @return
     * @see [类、类#方法、类#成员]
     */
    private String getTemplatePath(){
        File file = new File(this.getClass().getClassLoader().getResource("").getPath());
        return file.getParentFile().getParentFile().getAbsolutePath()+"/download/";
    }
}