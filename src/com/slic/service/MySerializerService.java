/*
 * 文 件 名:  MySerializerService.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月23日
 */
package com.slic.service;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONObject;

import com.slic.entity.Constant;
import com.slic.entity.DataBlockEntity;
import com.slic.entity.FieldsEntity;
import com.slic.entity.HeaderEntity;
import com.slic.entity.ParamEntity;
import com.slic.entity.RowSetEntity;
import com.slic.entity.SendMsgEntity;

/**
 * 序列化数据
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MySerializerService {
    
    // 报文长度定义
    private static int msgLength = 0;
    
    public String getUUID(){
        return UUID.randomUUID().toString();
    }
    
    public static void main(String[] args){
        //String param = "{'msgId':7154,'handerid':'nazvINfubcGFYQF4EXlN','EId':'24229','UserID':'18','datalist_HeadFields':['CoEId','GoodsID','BulkQty','PackQty'],'datalist':[{'CoEId':'8002','GoodsID':'10478','BulkQty':0,'PackQty':1},{'CoEId':'8000','GoodsID':'10021','BulkQty':0,'PackQty':1},{'CoEId':'8000','GoodsID':'10002','BulkQty':0,'PackQty':1},{'CoEId':'8002','GoodsID':'10495','BulkQty':0,'PackQty':1},{'CoEId':'8002','GoodsID':'10432','BulkQty':0,'PackQty':1}]}";
        //String param = "{'EId':'24229','UserID':'18','handerid':'tS5ETT7UM17UGRtvxTcR','msgId':7152,'requestFrom':'5','type':'TCP'}";
        String param = "{'EId':'24229','UserID':'18','msgId':7152}";
        MySerializerService mySerializerService = new MySerializerService();
        try {
            mySerializerService.getSerializableData(param);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 获取序列化后数据
     * <功能详细描述>
     * @param jsonData
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public byte[] getSerializableData(String jsonData) throws Exception{
        msgLength = 0;
        JSONObject obj = JSONObject.fromObject(jsonData);
        
        // 判断是否有handleid
        if(!obj.containsKey(Constant.HANDLEID_NAME)){
            obj.put(Constant.HANDLEID_NAME, getUUID());
        }
        // 判断是否有requestFrom
        if(!obj.containsKey(Constant.REQUESTFROM_NAME)){
            obj.put(Constant.REQUESTFROM_NAME, Constant.REQUEST_FROM);
        }
        // 判断是否有type
        if(!obj.containsKey(Constant.SEND_TYPE_NAME)){
            obj.put(Constant.SEND_TYPE_NAME, Constant.SEND_TYPE);
        }
        
        // 数据解析
        UmxService umxService = new UmxService();
        SendMsgEntity sendMsgEntity =  umxService.getSendMsgEntity(obj);
        // 新建报文头
        HeaderEntity header = new HeaderEntity();
        header.setMsgId(obj.getInt("msgId"));
        sendMsgEntity.setHeader(header);
        // 报文头长度计算
        msgLength += Constant.HEADER_LENGTH;
        
        // 计算报文体长度
        byte[] paramsLength = new byte[2];
        write16Be(sendMsgEntity.getParams().size(), 0, paramsLength);
        msgLength += 2;
        byte[] paramsByte = getParam(sendMsgEntity.getParams());
        
        byte[] rowSetLength = new byte[2];
        write16Be(sendMsgEntity.getRowSets().size(), 0, rowSetLength);
        msgLength += 2;
        byte[] rowSetByte = getRowSets(sendMsgEntity.getRowSets()); 
        
        sendMsgEntity.getHeader().setTotalLen(msgLength);
        byte[] headerByte = getHeader(sendMsgEntity.getHeader());
       
//        System.out.println("msgLength:--"+msgLength);
//        System.out.println("header:--"+Arrays.toString(headerByte));
//        
//        System.out.println("paramslength:--"+Arrays.toString(paramsLength));
//        System.out.println("paramsByte:--"+Arrays.toString(paramsByte));
//        
//        System.out.println("rowSetlength:--"+Arrays.toString(rowSetLength));
//        System.out.println("rowSetByte:--"+Arrays.toString(rowSetByte));
        
        byte[] newBuffer = new byte[msgLength];
        int index = 0;
        // 头部52位
        index = appendByte(headerByte,newBuffer,index);
        // 参数个数 2位
        index = appendByte(paramsLength,newBuffer,index);
        // 参数
        index = appendByte(paramsByte,newBuffer,index);
        // 行集个数2位
        index = appendByte(rowSetLength,newBuffer,index);
        // 行集
        index = appendByte(rowSetByte,newBuffer,index);
        
        return newBuffer;
    }
    
    /**
     * 头部数组封装
     * <功能详细描述>
     * @param headerEntity
     * @return
     * @see [类、类#方法、类#成员]
     */
    public byte[] getHeader(HeaderEntity headerEntity)  throws Exception{
        
        byte[] header = new byte[Constant.HEADER_LENGTH];
        int index = 0;
        header[index ++] = (byte)headerEntity.getVersion();
        index = write32Be(headerEntity.getTotalLen(),index,header);
        index = write32Be(headerEntity.getDomain(),index,header);
        header[index ++] = (byte)headerEntity.getMsgType();
        index = write32Be(headerEntity.getMsgId(),index,header);
        index = write16Be(headerEntity.getMsgVersion(),index,header);
        index = write32Be(headerEntity.getCmdSerial(),index,header);
        header[index ++] = (byte)headerEntity.getSrcType();
        index = write32Be(headerEntity.getHiSrcID(),index,header);
        index = write32Be(headerEntity.getSrcID(),index,header);
        header[index ++] = (byte)headerEntity.getDestType();
        index = write32Be(headerEntity.getHiDestID(),index,header);
        index = write32Be(headerEntity.getDestID(),index,header);
        index = write32Be(headerEntity.getMsgAttr(),index,header);
        header[index ++] = (byte)headerEntity.getMsgPriority();
        index = write32Be(headerEntity.getTimestamp(),index,header);
        header[index ++] = (byte)headerEntity.getCharset();
        index = write32Be(headerEntity.getExtAttr(),index,header);
        //System.out.println("header:"+index);
        return header;
    }
    // 普通字段封装
    private  byte[] getParam(List<ParamEntity> params) throws Exception{
        byte[] buffer = new byte[0];
        byte[] newBuffer = null;
        int index = 0;
        for(ParamEntity param:params){
            byte[] namebuffer = param.getName().getBytes(Constant.ENCODING);
            newBuffer = getNewByte(buffer,1);
            buffer = newBuffer;
            buffer[index ++] = (byte)namebuffer.length; // 字段名长度 1 位
            newBuffer = getNewByte(buffer,namebuffer.length);
            index = appendByte(namebuffer,newBuffer,index);// 字段
            buffer = newBuffer;
            
            newBuffer = getNewByte(buffer,1);
            buffer = newBuffer;
            buffer[index ++] = (byte) param.getData().getType();// 数据类型 1位
            
            newBuffer = getNewByte(buffer,2);
            buffer = newBuffer;
            index = write16Be(param.getAttr(), index, buffer);// 2位
            
            newBuffer = getNewByte(buffer,4);
            buffer = newBuffer;
            index = write32Be(param.getData().get_data_len(), index, buffer);// 数据的二进制长度 4 位
            
            newBuffer = getNewByte(buffer,param.getData().get_data_len());
            index = appendByte(param.getData().getData().getData(),newBuffer,index);// 数据
            buffer = newBuffer;
        }
//        System.out.println(Arrays.toString(buffer));
//        System.out.println("paramLength:"+index);
        // 计算长度
        msgLength += index;
        return buffer;
    }
    
    // 行集记录序列化
    private  byte[] getRowSets(List<RowSetEntity> rowSets) throws Exception{
         byte[] buffer = new byte[0];
         byte[] newBuffer = null;
         int index = 0;
         
         for(RowSetEntity rowSet : rowSets){
             newBuffer = getNewByte(buffer,2);
             buffer = newBuffer;
             index = write16Be(rowSet.getMeta()[0], index, buffer);// 
             
             newBuffer = getNewByte(buffer,2);
             buffer = newBuffer;
             index = write16Be(rowSet.getMeta()[1], index, buffer);//
             
             newBuffer = getNewByte(buffer,4);
             buffer = newBuffer;
             index = write32Be(rowSet.getMeta()[2], index, buffer);// 行集数
             
             newBuffer = getNewByte(buffer,2);
             buffer = newBuffer;
             index = write16Be(rowSet.getMeta()[3], index, buffer);// 字段数
             
             List<FieldsEntity> fields = rowSet.getFields();
             for(FieldsEntity field : fields){
                 byte[] namebuffer = field.getName().getBytes(Constant.ENCODING);
                 newBuffer = getNewByte(buffer,1);
                 buffer = newBuffer;
                 buffer[index ++] = (byte)namebuffer.length;// 字段长度 1位
                 
                 newBuffer = getNewByte(buffer,namebuffer.length);
                 index = appendByte(namebuffer,newBuffer,index);// 字段
                 buffer = newBuffer;
                 
                 newBuffer = getNewByte(buffer,1);
                 buffer = newBuffer;
                 buffer[index ++] = (byte)field.getType(); // 类型 1位
                 
                 newBuffer = getNewByte(buffer,2);
                 buffer = newBuffer;
                 index = write16Be(field.getAttr(), index, buffer);// 
             }
             
             List<List<DataBlockEntity>> datas = rowSet.getData();
             
             for(List<DataBlockEntity> data : datas ){
                 for(DataBlockEntity _data : data){
                     newBuffer = getNewByte(buffer,2);
                     buffer = newBuffer;
                     index = write16Be(_data.get_data_len(), index, buffer);// 数据长度 2位
                     
                     newBuffer = getNewByte(buffer,_data.get_data_len());
                     index = appendByte(_data.getData().getData(),newBuffer,index);// 数据
                     buffer = newBuffer;
                 }
             }
         }
         
//         System.out.println(Arrays.toString(buffer));
//         System.out.println("paramLength:"+index);
         // 长度统计
         msgLength += index;
         return buffer;
    }
    
    // 追加
    private  int appendByte(byte[] src,byte[] dest,int index)  throws Exception{
        for(int i=0;i<src.length;i++){
            dest[index ++] = src[i];
        }
        return index;
    }
    // 获取一个新数组
    private  byte[] getNewByte(byte[] src,int addLength)  throws Exception{
        return Arrays.copyOf(src, src.length + addLength);
    }
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param totalLen
     * @param index
     * @param header
     * @see [类、类#方法、类#成员]
     */
    private  int write16Be(int data, int index, byte[] header)  throws Exception{
        header[index ++] = (byte) ((0xff00 & data) >> 8);
        header[index ++] = (byte) (0xff & data);
        return index;
    }
    /** <一句话功能简述>
     * <功能详细描述>
     * @param totalLen
     * @param index
     * @param header
     * @see [类、类#方法、类#成员]
     */
    private  int write32Be(int data, int index, byte[] header)  throws Exception{
        header[index ++] = (byte) ((0xff000000 & data) >> 24);
        header[index ++] = (byte) ((0xff0000 & data) >> 16);
        header[index ++] = (byte) ((0xff00 & data) >> 8);
        header[index ++] = (byte) (0xff & data);
        
        return index;
    }
}
