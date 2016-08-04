package com.slic.servlet;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import test.we.sss;

import com.slic.dao.InitDao;
import com.slic.dao.SocketClient;
import com.slic.enums.ResponseCodeEnum;
import com.slic.print.config.PrintConstants;
import com.slic.service.ExportExcelService;
import com.slic.utils.HSSFUtil;

public class ExportExcelServlet extends HttpServlet {
	private Logger logger = LoggerFactory.getLogger(getClass());

	// 初始化获取中间件配置信息
	private static Properties configs;
	private ExportExcelService exportExcelService = new ExportExcelService();

	@Override
	public void init() throws ServletException 
	{
		try 
		{
			configs = InitDao.init();
			PrintConstants.PAGE_SWITCH_HEIGHT = Integer.parseInt(configs.getProperty("PAGE_SWITCH_HEIGHT",PrintConstants.PAGE_SWITCH_HEIGHT + ""));
			PrintConstants.NOPAGE_CORRECT_HEIGHT = Integer.parseInt(configs.getProperty("NOPAGE_CORRECT_HEIGHT",PrintConstants.NOPAGE_CORRECT_HEIGHT + ""));
			PrintConstants.NOPAGE_GOODROW_CORRECT_HEIGHT = Integer.parseInt(configs.getProperty( "NOPAGE_GOODROW_CORRECT_HEIGHT", PrintConstants.NOPAGE_GOODROW_CORRECT_HEIGHT + ""));
			PrintConstants.MIN_MARGIN_BOTTON = Integer.parseInt(configs .getProperty("MIN_MARGIN_BOTTON", PrintConstants.MIN_MARGIN_BOTTON + ""));

			logger.debug("应用模式==ISDEBUG={}", configs.getProperty("ISDEBUG", "false"));
			logger.debug("DEBUG模式IP=={},port={}", configs.getProperty("ip"), configs.getProperty("port"));
			logger.debug("纸张调节参数 页面换纸张固定高度={}mm10倍 ", PrintConstants.PAGE_SWITCH_HEIGHT, PrintConstants.NOPAGE_GOODROW_CORRECT_HEIGHT);
			logger.debug("最小底部页边距 min_margin_botton={}", PrintConstants.MIN_MARGIN_BOTTON);

			logger.debug("DEBUG模式IP=={},port={}", configs.getProperty("ip"), configs.getProperty("port"));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			logger.error("初始化配置文件失败.......");
		}
		super.init();
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
	{
		req.setCharacterEncoding("utf-8");
		// 导出模板
		String templateName = req.getParameter("templateName");
		// 查询数据
		String queryData = req.getParameter("queryData");

		String[] interSendData = queryData.split("##");
		// 获取登录用户信息
		String loginUserInfo = req.getParameter("loginUser");
		String mainSiteServerIp = req.getParameter("mainSiteServerIp");
		String mainSiteServerPort = req.getParameter("mainSiteServerPort");
		String isdebug = configs.getProperty("ISDEBUG", "false");
		
		if ("true".equals(isdebug)) 
		{
//			mainSiteServerIp = configs.getProperty("ip", "192.168.10.230");
//			mainSiteServerPort = configs.getProperty("port", "9601");
			mainSiteServerIp = configs.getProperty("ip", "127.0.0.1");
			mainSiteServerPort = configs.getProperty("port", "9601");
		}
		// 主站ip和port
		logger.info("ISDEBUG={},主站ip={}和port={}:", isdebug, mainSiteServerIp, mainSiteServerPort);
		
		if (loginUserInfo != null)
		{
			loginUserInfo = loginUserInfo.toLowerCase();
		}

		// 127.0.0.1 192.168.10.187
		// 创建工作薄
		HSSFWorkbook workbook = null;
		for (String sendData : interSendData) {
			// 设置返回excel
			resp.setContentType("application/vnd.ms-excel;charset=UTF-8");
			resp.setHeader( "Content-disposition", "attachment;filename=\"" + new String((System.currentTimeMillis() + ".xls").getBytes("GBK"), "ISO-8859-1") + "\"");
			try 
			{
				int port = Integer.parseInt(mainSiteServerPort);
				logger.info("发送报文:" + sendData);
				String source = SocketClient.send(mainSiteServerIp, port, sendData);
				// String source = SocketClient.send("192.168.10.196", 9601, sendData) ;
				logger.info("返回报文:" + source);
				
				JSONObject resultData = JSONObject.fromObject(source);
				if (ResponseCodeEnum.SUCCESS.getResponseCode().equals(resultData.getString("resCode"))) 
				{
					JSONObject userJson = JSONObject.fromObject(loginUserInfo);
					exportExcelService.initPrintPageTPL(templateName, userJson, configs);
					exportExcelService.printExecl(resultData);
					workbook = exportExcelService.getWorkbook();
					// exportExcelService.createExcel(templateName, resultData,
					// workbook,userJson,configs) ;
					// liurong 2016-05-16 还原打印(打印分页与即打即停之前
					// 页高
					double _heigth;
					String msg;
					// 1--即打即停  0分页打印,
					int printFlag;
					
					if (1 == exportExcelService.getSetsJson().getInt("printType")) {
						int num = exportExcelService.getDetailTotalRows();
						int rh = exportExcelService.getRowsHeight();
						int dv_col = exportExcelService.getSetsJson().getJSONObject("printPriceTicket").getInt("col");
						printFlag = 1;
						msg = "标签打印返回的页面高度为";
						_heigth = (Math.ceil((num -1)*1.0/dv_col)+1) * (HSSFUtil.pxToHeightCM(rh)) * 10;
					}
					else {
						if (exportExcelService.getSetsJson().getBoolean("stopFight")) 
						{
							// 返回计算后的高度 即打即停多加一行
							JSONObject backDatas = JSONObject.fromObject(resultData.get("responseText"));
							JSONArray totalData = backDatas.getJSONArray("item_0");
							// 分页行高度
							double fh = 0.5 * totalData.size();
							double dh = HSSFUtil.pxToHeightCM(exportExcelService.getDatailHeight());
							int num = exportExcelService.getDetailTotalRows();
							// 数据明细高度
							double mx = dh * (num - 1);
							_heigth = (exportExcelService.getPaperheight() + mx + fh) * 10;
							// 超过550毫米,按照550计算
							msg = "即打即停返回的页面高度为";
							printFlag = 1;
						} 
						else 
						{
							printFlag = 0;
							msg = "分页打印返回的页面高度为";
							_heigth = exportExcelService.getPaperheight() * 10;
						}
					}

					if (_heigth > PrintConstants.PAGE_MAX_HEIGHT)
					{
						_heigth = PrintConstants.PAGE_MAX_HEIGHT;
					} 
					else if (_heigth < PrintConstants.PAGE_MIN_HEIGHT) 
					{
						// 最小页面高度
						_heigth = PrintConstants.PAGE_MIN_HEIGHT;
					}
					
					// 页高
					resp.setHeader("selfpaperheight", _heigth + "");
					double _width = exportExcelService.getPaperwidth() * 10;
					resp.setHeader("selfpaperwidth", _width + "");
					resp.setHeader("printflag", printFlag + "");

					logger.debug(msg + "=====返回客户端页面宽*高={}*{}", _width, _heigth);
				}
				else 
				{
					logger.error("查询订单信息异常....");
					logger.error(
							"打印异常 查询订单信息异常,  原因: \n templateName={} \n queryData={} \n mainSiteServerIp={} \n port={}",
							templateName, queryData, mainSiteServerIp,
							mainSiteServerPort);
				}
			}
			catch (Exception e) 
			{
				e.printStackTrace();
				logger.error(
						"打印异常,  原因: \n err={},\n templateName={} \n queryData={} \n ip={} \n port={}",
						e.getMessage(), templateName, queryData,
						mainSiteServerIp, mainSiteServerPort);
			}
		}
        
		if (workbook != null) 
		{
			/*JSONObject userJson = JSONObject.fromObject(loginUserInfo);
			String path = ExportExcelServlet.class.getClassLoader().getResource("").getPath();
	        path = path.split("classes")[0]+"temp/"+userJson.getString("eid");
			FileOutputStream fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			fileOut.close();*/
			
			OutputStream out = resp.getOutputStream();
			// 输出excel
			workbook.write(out);
			out.flush();
			out.close();
			workbook.close();
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException 
	{
		doGet(req, resp);
	}

	public static void main(String[] args)
	{
		String sendData = "{'msgId':260,'AreaID':441904,'datalist_HeadFields':['RCType'],'datalist':[{'RCType':5}]}";
		String source = SocketClient.send("192.168.10.196", 9601, sendData);
		System.out.println(source);
	}
}
