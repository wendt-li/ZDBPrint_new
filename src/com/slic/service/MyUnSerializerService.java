/*
 * 文 件 名:  MyUnSerializerService.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月23日
 */
package com.slic.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.json.JSONObject;
import net.sf.json.groovy.JsonGroovyBuilder;

import com.slic.entity.Constant;
import com.slic.entity.HeaderEntity;
import com.slic.enums.PropertyTypeEnum;

/**
 * 反序列化
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class MyUnSerializerService {
    
    /**
     * 解析头<一句话功能简述>
     * <功能详细描述>
     * @param header
     * @return
     * @throws Exception 
     * @see [类、类#方法、类#成员]
     */
    public static HeaderEntity unSerializerHeader(byte[] header)
        throws Exception {
        HeaderEntity headerEntity = new HeaderEntity();
        int index = 0;
        headerEntity.setVersion(header[index++]);
        
        headerEntity.setTotalLen(read32Be(index, header));
        index += 4;
        
        headerEntity.setDomain(read32Be(index, header));
        index += 4;
        
        headerEntity.setMsgType(header[index++]);
        
        headerEntity.setMsgId(read32Be(index, header));
        index += 4;
        
        headerEntity.setMsgVersion(read16Be(index, header));
        index += 2;
        
        headerEntity.setCmdSerial(read32Be(index, header));
        index += 4;
        
        headerEntity.setSrcType(header[index++]);
        
        headerEntity.setHiSrcID(read32Be(index, header));
        index += 4;
        
        headerEntity.setSrcID(read32Be(index, header));
        index += 4;
        
        headerEntity.setDestType(header[index++]);
        
        headerEntity.setHiDestID(read32Be(index, header));
        index += 4;
        
        headerEntity.setDestID(read32Be(index, header));
        index += 4;
        
        headerEntity.setMsgAttr(read32Be(index, header));
        index += 4;
        
        headerEntity.setMsgPriority(header[index++]);
        
        headerEntity.setTimestamp(read32Be(index, header));
        index += 4;
        
        headerEntity.setCharset(header[index++]);
        
        headerEntity.setExtAttr(read32Be(index, header));
        
        return headerEntity;
    }
   
    /**
     * 解析包体<一句话功能简述>
     * <功能详细描述>
     * @param body
     * @return
     * @throws Exception
     * @see [类、类#方法、类#成员]
     */
    public static String unSerializerBody(byte[] body)
        throws Exception {
        // 返回结果集
        JSONObject bodyList = new JSONObject();
        
        int index = 0;
        // 读取param字段数
        int paramNum = read16Be(index, body);
        index += 2;
        // 普通参数解析
        for (int i = 0; i < paramNum; i++) {
            ////////////////////////////////////////////////////////////////////////////////////////////////////
            int nameLength = body[index++];
            nameLength = nameLength < 0 ? nameLength + 256 : nameLength;
            // 根据字段长度读取字段名
            byte[] nameByte = new byte[nameLength];
            System.arraycopy(body, index, nameByte, 0, nameLength);
//            System.out.println("name:" + new String(nameByte, Constant.ENCODING));
            index += nameLength;
            
            // 值类型
            int type = body[index++];
            type = type < 0 ? (type + 256) : type;
            
            int attr = read16Be(index, body);
            index += 2;
            ////////////////////////////////////////////////////////////////////////////////////////////////////
            
            int dataType = getDataTypeLength(type);
            int valueLength = 0;// 值长度
            // 
            switch (dataType) {
                case 1:
                    valueLength = body[index ++];
                    break;
                case 2:
                    valueLength = read16Be(index, body);
                    index += 2;
                    break;
                case 4:
                    valueLength = read32Be(index, body);
                    index += 4;
                    break;
            }
            
            // 值
            byte[] valueByte = new byte[valueLength];
            System.arraycopy(body, index, valueByte, 0, valueLength);
//            System.out.println("value:" + new String(valueByte, Constant.ENCODING));
            index += valueLength;
            // param 数据封装
            bodyList.put(new String(nameByte, Constant.ENCODING).toLowerCase(), new String(valueByte, Constant.ENCODING));
        }
        
        // 行集参数解析
        
        // 读取行集记录数
        int rowSetNum = read16Be(index, body);
        index += 2;
        
        for(int i=0;i<rowSetNum;i++){
            // 2字节的行集序号 
            int rst_id = read16Be(index, body);
            index += 2;
            // 2字节保留属性 
            int rst_reserved = read16Be(index, body);
            index += 2;
            // 4字节行集记录总数 
            int rst_row_cnt = read32Be(index, body);
            index += 4;
            // 2字节行集字段总数
            int rst_fld_num = read16Be(index, body);
            index += 2;
            
            // 字段名集合
            String[] nameArray = new String[rst_fld_num];
            
            // 值类型
            int[] type = new int[rst_fld_num];
            // 字段解析
            for(int j=0;j<rst_fld_num;j++){
                ////////////////////////////////////////////////////////////////////////////////////////////////////
                int fieldLength = body[index ++];
                fieldLength = fieldLength < 0 ? fieldLength + 256 : fieldLength;
                // 根据字段长度读取字段名
                byte[] nameByte = new byte[fieldLength];
                System.arraycopy(body, index, nameByte, 0, fieldLength);
//                System.out.println("name:" + new String(nameByte, Constant.ENCODING));
                nameArray[j] = new String(nameByte, Constant.ENCODING).toLowerCase();
                index += fieldLength;
                
                // 值类型
                type[j] = body[index++];
                type[j] = type[j] < 0 ? (type[j] + 256) : type[j];
                
                int attr = read16Be(index, body);
                index += 2;
                
                ////////////////////////////////////////////////////////////////////////////////////////////////////
            }
            
            // hang
            List<JSONObject> rowSetList = new ArrayList<JSONObject>();
            
            // 值解析
            for(int j=0;j<rst_row_cnt;j++){
                JSONObject rowSetSingle = new JSONObject();
                for(int k=0;k<rst_fld_num;k++){
                    
                    int dataType = getDataTypeLength(type[k]);
                    int valueLength = 0;// 值长度
                    // 
                    switch (dataType) {
                        case 1:
                            valueLength = body[index ++];
                            break;
                        case 2:
                            valueLength = read16Be(index, body);
                            index += 2;
                            break;
                        case 4:
                            valueLength = read32Be(index, body);
                            index += 4;
                            break;
                    }
                    
                    // 值
                    byte[] valueByte = new byte[valueLength];
                    System.arraycopy(body, index, valueByte, 0, valueLength);
//                    System.out.println("----value:" + new String(valueByte, Constant.ENCODING));
                    rowSetSingle.put(nameArray[k], new String(valueByte, Constant.ENCODING));
                    index += valueLength;
                    
                }
                rowSetList.add(rowSetSingle);
            }
            
            bodyList.put("item_"+i, rowSetList);
        }
        
//        System.out.println("---------"+bodyList.toString());
        
        String result = bodyList.toString();
        int nullIndex = 0;
        while( (nullIndex = result.indexOf("\\u") ) > 0){
            result = result.substring(0, nullIndex)+result.substring(nullIndex+6);
        }
//        System.out.println("---------"+result);
        return result;
    }
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param totalLen
     * @param index
     * @param header
     * @see [类、类#方法、类#成员]
     */
    private static int read16Be(int index, byte[] header)
        throws Exception {
        int r = 0;
        for (int i = index; i < index + 2; i++) {
            r <<= 8;
            r |= (header[i] & 0x00ff);
        }
        return r;
    }
    
    /** <一句话功能简述>
     * <功能详细描述>
     * @param totalLen
     * @param index
     * @param header
     * @see [类、类#方法、类#成员]
     */
    private static int read32Be(int index, byte[] header)
        throws Exception {
        int r = 0;
        for (int i = index; i < index + 4; i++) {
            r <<= 8;
            r |= (header[i] & 0x000000ff);
        }
        return r;
    }
    
    private static int getDataTypeLength(int type) {
        switch (type) {
            case 200:
            case 202:
            case 204:
            case 152:
            case 155:
                return 2;
            case 201:
            case 203:
            case 205:
            case 153:
            case 156:
            case 0: /// 0
            case 1:///RSF_BINARY UMX2 
                return 4;
        }
        ;
        return 1;
    }
}
