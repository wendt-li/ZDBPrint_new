/*
 * 文 件 名:  SocketClient.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.dao;

import java.io.ByteArrayInputStream ;
import java.io.ByteArrayOutputStream ;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream ;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.GZIPInputStream ;
import java.util.zip.Inflater ;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.slic.entity.Constant;
import com.slic.entity.FieldsEntity;
import com.slic.entity.HeaderEntity;
import com.slic.entity.ParamEntity;
import com.slic.entity.DataBlockEntity;
import com.slic.entity.ResponseMsgEntity;
import com.slic.entity.RowSetEntity;
import com.slic.entity.SendMsgEntity;
import com.slic.enums.ResponseCodeEnum;
import com.slic.service.MySerializerService;
import com.slic.service.MyUnSerializerService;
import com.slic.service.UmxService;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SocketClient 
{
//    private static final String IP = "127.0.0.1";//192.168.10.43
//    private static final int PROT = 9601;
    
    private static Socket socket ;
    
    private static int compress_threshold_ = 64*1024; ///< 自动压缩阈值.单位:字节
    public static final int BUFFER = 1024;  
    private static final int COMPRESS_MASK	= 0x0200;	///< 是否压缩,压缩采用内部算法.
    
//    public static void main(String[] args){
//        ResponseMsgEntity responseMsgEntity = new ResponseMsgEntity();
//        
//        try
//        {
//          //String param = "{'msgId':7154,'handerid':'nazvINfubcGFYQF4EXlN','EId':'24229','UserID':'18','datalist_HeadFields':['CoEId','GoodsID','BulkQty','PackQty'],'datalist':[{'CoEId':'8002','GoodsID':'10478','BulkQty':0,'PackQty':1},{'CoEId':'8000','GoodsID':'10021','BulkQty':0,'PackQty':1},{'CoEId':'8000','GoodsID':'10002','BulkQty':0,'PackQty':1},{'CoEId':'8002','GoodsID':'10495','BulkQty':0,'PackQty':1},{'CoEId':'8002','GoodsID':'10432','BulkQty':0,'PackQty':1}]}";
//            //String param = "{'EId':'24229','UserID':'18','msgId':7152}";
//            String param = "{msgId: 5400, EId: 100001, UserID: 2}";//"{'msgId':7069,'EId':24229,'QryMode':4,'QryVenderFlag':0,'UserID':'18','flag':1,'GoodsInfo':'','#PAGIZE.FLAG':1,'#PAGIZE.PAGE_NO':1,'#PAGIZE.PAGE_SIZE':20,'#PAGIZE.COUNT_FLAG':1,'#PAGIZE.PAGE_NO_DUP':0}";
//            _send(IP,PROT,param);
//            JSONObject response = read();
//            if(Constant.BACK_ACCESS_SUCCESS_FLAG.equals(response.getString(Constant.BACK_ACCESS_FLAG_FIELD))){
//                responseMsgEntity.setResposne(ResponseCodeEnum.SUCCESS);
//                responseMsgEntity.setResponseText(response.toString());
//            }else{
//                responseMsgEntity.setResposne(ResponseCodeEnum.ERROR);
//                responseMsgEntity.setResponseText(response.toString());
//            }
//        }catch (UnknownHostException e){
//            e.printStackTrace();
//            responseMsgEntity.setResposne(ResponseCodeEnum.UNKNOWHOST);
//        }catch (Exception e){
//            e.printStackTrace();
//            responseMsgEntity.setResposne(ResponseCodeEnum.ERROR);
//        }finally{
//            close();
//        }
//        
////        System.out.println(JSONObject.fromObject(responseMsgEntity).toString());
//        
//    }
    
    public static String send(String host,int port,String sendData){
        ResponseMsgEntity responseMsgEntity = new ResponseMsgEntity();
        try
        {
        	// 发送报文
            _send(host,port,sendData);
            // 接受报文
            JSONObject response = read();
            // 判断是否成功
            if(Constant.BACK_ACCESS_SUCCESS_FLAG.equals(response.getString(Constant.BACK_ACCESS_FLAG_FIELD))){
                responseMsgEntity.setResposne(ResponseCodeEnum.SUCCESS);
                responseMsgEntity.setResponseText(response.toString());
            }else{
                responseMsgEntity.setResposne(ResponseCodeEnum.ERROR);
                responseMsgEntity.setResponseText(response.toString());
            }
        }catch (UnknownHostException e){
            e.printStackTrace();
            responseMsgEntity.setResposne(ResponseCodeEnum.UNKNOWHOST);
        }catch (Exception e){
            e.printStackTrace();
            responseMsgEntity.setResposne(ResponseCodeEnum.ERROR);
        }finally{
        	// 关闭连接
            close();
        }
        // 返回接口调用结果
        return JSONObject.fromObject(responseMsgEntity).toString();
    }
    
    /**
     * 发送消息<一句话功能简述>
     * <功能详细描述>
     * @param host
     * @param port
     * @param sendData
     * @throws Exception 
     * @throws UnknownHostException 
     * @see [类、类#方法、类#成员]
     */
    public static void _send(String host,int port,String sendData) throws UnknownHostException, Exception{
        if(socket == null){
            socket = new Socket(host, port);
        }
        // 设置超时时间 10s
//        socket.setSoTimeout(60 * 1000);
        // 序列化发送报文
        MySerializerService mySerializerService = new MySerializerService();
        byte[] sendDatas = mySerializerService.getSerializableData(sendData);
        // 发送消息
        socket.getOutputStream().write(sendDatas);
    }
    
    /**
     * 获取消息<一句话功能简述>
     * <功能详细描述>
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public static JSONObject read() throws Exception{
        InputStream in = socket.getInputStream();
        // 头部读取
        byte[] resHeader = new byte[Constant.HEADER_LENGTH];
        in.read(resHeader);
        // 解析头部
        HeaderEntity headerEntity = MyUnSerializerService.unSerializerHeader(resHeader);
        // 报文体接受容器
        byte[] receive = new byte[headerEntity.getTotalLen() - Constant.HEADER_LENGTH];// 整个报文
        // 缓冲区
        byte[] pool = new byte[headerEntity.getTotalLen()];
        int index = -1;
        int position = 0;
        // 读取后续报文
        while(position < headerEntity.getTotalLen()- Constant.HEADER_LENGTH){
        	index = in.read(pool);
        	int temp = position + index;
        	if(position > headerEntity.getTotalLen()){
        		index = temp - position + headerEntity.getTotalLen() ; 
        	}
        	System.arraycopy(pool, 0, receive, position, index);
        	position += index;
        }
        
        String body = null;
        // 判断是否需要使用gzip解压
        if(headerEntity.getMsgAttr() == COMPRESS_MASK){
        	int leng = headerEntity.getTotalLen() - 4 - Constant.HEADER_LENGTH;
        	byte[] test1 = new byte[leng];
        	System.arraycopy(receive, 4, test1, 0, leng);
        	byte[] test = decompress(test1);
        	
        	body = MyUnSerializerService.unSerializerBody(test);
        }else{
        	// 反序列化报文
            body = MyUnSerializerService.unSerializerBody(receive);
        }
        
        JSONObject bodyJson = JSONObject.fromObject(body);
        bodyJson.put("msgid", headerEntity.getMsgId());
        return bodyJson;
    }
    
    /** 
     * 解压缩 
     *  
     * @param data 
     *            待压缩的数据 
     * @return byte[] 解压缩后的数据 
     */  
    public static byte[] decompress(byte[] data) {  
        byte[] output = new byte[0];  
  
        Inflater decompresser = new Inflater();  
        decompresser.reset();  
        decompresser.setInput(data);  
  
        ByteArrayOutputStream o = new ByteArrayOutputStream(data.length);  
        try {  
            byte[] buf = new byte[1024];  
            while (!decompresser.finished()) {  
                int i = decompresser.inflate(buf);  
                o.write(buf, 0, i);  
            }  
            output = o.toByteArray();  
        } catch (Exception e) {  
            output = data;  
            e.printStackTrace();  
        } finally {  
            try {  
                o.close();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
  
        decompresser.end();  
        return output;  
    } 
    
    /**
     * 关闭链接<一句话功能简述>
     * <功能详细描述>
     * @see [类、类#方法、类#成员]
     */
    public static void close(){
        if(socket !=null){
            try {
                socket.close();
                socket = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
}
