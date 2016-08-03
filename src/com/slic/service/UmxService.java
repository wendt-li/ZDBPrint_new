/*
 * 文 件 名:  UmxService.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.service;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.slic.entity.Constant;
import com.slic.entity.FieldsEntity;
import com.slic.entity.ParamEntity;
import com.slic.entity.DataBlockEntity;
import com.slic.entity.DataEntity;
import com.slic.entity.RowSetEntity;
import com.slic.entity.SendMsgEntity;
import com.slic.enums.FieldTypeEnum;
import com.slic.enums.PropertyTypeEnum;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class UmxService
{
    private SendMsgEntity sendMsgEntity = new SendMsgEntity();
    
    /**
     * @return 返回 sendMsgEntity
     */
    public SendMsgEntity getSendMsgEntity(JSONObject obj) throws Exception{
        setParam(obj);
        return sendMsgEntity;
    }

    /**
     * 请求参数封装
     * <功能详细描述>
     * @param obj
     * @return
     * @see [类、类#方法、类#成员]
     */
    private void setParam(JSONObject obj) throws Exception{
        List<RowSetEntity> rowSets = new ArrayList<RowSetEntity>();
        Set keys = obj.keySet();
        List<String> listKeys = new ArrayList<String>();
        // 获取数组类型参数 行集数据
        for(Object key:keys){
            if(key != null && !"".equals(key.toString())){
                if(key.toString().toLowerCase().indexOf("_headfields") >= 0){
                    String _temp = key.toString().toLowerCase();
                    // 字段key
                    listKeys.add(_temp);
                    int index = _temp.indexOf("_headfields");
                    // 字段对应的列表key
                    listKeys.add(_temp.substring(0,index));
                    
                    JSONArray fields = obj.getJSONArray(key.toString());
                    String _tempKey = key.toString();
                    JSONArray datas = obj.getJSONArray(_tempKey.substring(0,index));
                    
                    RowSetEntity rowSetEntity = new RowSetEntity();
                    rowSetEntity.setMeta(new int[]{0,0,datas.size(),fields.size()});
                    
                    List<FieldsEntity> _fileds = getFieldsList(fields);
                    rowSetEntity.setFields(_fileds);
                    
                    List<List<DataBlockEntity>> _datas = null;
                    try {
                        // 获取行集数据
                        _datas = getDataList(datas);
                    }
                    catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    rowSetEntity.setData(_datas);
                    
                    rowSets.add(rowSetEntity);
                }
            }
        }
        sendMsgEntity.setRowSets(rowSets);
        //System.out.println(JSONArray.fromObject(rowSets).toString());
        
        List<ParamEntity> params = new ArrayList<ParamEntity>();
        // 普通数据
        for(Object key:keys){
            try{
                if(key != null && !"".equals(key.toString())){
                    String _temp = key.toString().toLowerCase();
                    boolean fieldTypeFlag = false;
                    if(listKeys.size() > 0){
                        // 过滤行集中已封装过的数据字段
                        for(String listKey:listKeys){
                            if(listKey.equals(_temp)){
                                fieldTypeFlag = true;
                                break;
                            }
                        }
                    }
                    if(!fieldTypeFlag){
                        // 普通字段封装
                        params.add(createSingleParam(key,obj.get(key))); 
                    }
                    
                }
            }catch (UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }
        
        sendMsgEntity.setParams(params);
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param datas
     * @return
     * @throws UnsupportedEncodingException 
     * @see [类、类#方法、类#成员]
     */
    private List<List<DataBlockEntity>> getDataList(JSONArray datas) throws Exception {
        List<List<DataBlockEntity>> _result = new ArrayList<List<DataBlockEntity>>();
        for(int i=0;i<datas.size();i++){
            JSONObject _temp = datas.getJSONObject(i);
            List<DataBlockEntity> _params = getParamList(_temp);
            _result.add(_params);
        }
        return _result;
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param _temp
     * @return
     * @throws UnsupportedEncodingException 
     * @see [类、类#方法、类#成员]
     */
    private List<DataBlockEntity> getParamList(JSONObject _temp) throws Exception {
        List<DataBlockEntity> _params = new ArrayList<DataBlockEntity>();
        Set keySet = _temp.keySet();
        for(Object obj : keySet){
            DataBlockEntity paramInner = new DataBlockEntity();
            paramInner.setType(PropertyTypeEnum.DTVARCHAR.getValue());
            
            DataEntity paramInner1 = new DataEntity();
            paramInner1.setType("Buffer");
            Object value = _temp.get(obj);
            
            byte[] values = value.toString().getBytes(Constant.ENCODING);
            byte[] newValues = Arrays.copyOf(values, values.length+1);
            newValues[newValues.length - 1] = '\0';// 字段结束符
            // 字段内容编码
            paramInner1.setData(newValues);
            
            paramInner.setData(paramInner1);
            // 设置字段长度
            paramInner.set_data_len(paramInner1.getData().length);
            
            _params.add(paramInner);
        }
        
        return _params;
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param fields
     * @return
     * @see [类、类#方法、类#成员]
     */
    private List<FieldsEntity> getFieldsList(JSONArray fields) throws Exception{
        List<FieldsEntity> _fields = new ArrayList<FieldsEntity>();
        
        for(int i=0;i<fields.size();i++){
            FieldsEntity fieldsEntity = new FieldsEntity();
            fieldsEntity.setAttr(FieldTypeEnum.NORMAL.getValue());
            fieldsEntity.setName(fields.getString(i));
            fieldsEntity.setType(PropertyTypeEnum.DTVARCHAR.getValue());
            _fields.add(fieldsEntity);
        }
        
        return _fields;
    }

    /** <一句话功能简述>
     * <功能详细描述>
     * @param key
     * @param object
     * @throws UnsupportedEncodingException 
     * @see [类、类#方法、类#成员]
     */
    private ParamEntity createSingleParam(Object key, Object value) throws Exception{
        if(key == null){
            return null;
        }
        ParamEntity paramEntity = new ParamEntity();
        paramEntity.setName(key.toString());
        
        DataBlockEntity paramInner = new DataBlockEntity();
        paramInner.setType(PropertyTypeEnum.DTLONGVARCHAR.getValue());
        
        DataEntity paramInner1 = new DataEntity();
        paramInner1.setType("Buffer");
        // 添加结束符
        byte[] values = (value.toString() + '\0').getBytes(Constant.ENCODING);
//        byte[] newValues = Arrays.copyOf(values, values.length+1);
//        newValues[newValues.length - 1] = ;// 字段结束符
        // 字段内容编码
        paramInner1.setData(values);
        
        paramInner.setData(paramInner1);
        // 设置字段长度
        paramInner.set_data_len(paramInner1.getData().length);
        paramEntity.setData(paramInner);
        if(Constant.HANDLEID_NAME.equals(key.toString().toLowerCase())){
            paramEntity.setAttr(FieldTypeEnum.REBACK.getValue());// 字段类型 handelid 需要返回2
        }else{
            paramEntity.setAttr(FieldTypeEnum.NORMAL.getValue());
        }
        
        return paramEntity;
    }
    
    public static void main(String[] args){
        String param = "{'msgId':7154,'handerid':'nazvINfubcGFYQF4EXlN','EId':'24229','UserID':'18','datalist_HeadFields':['CoEId','GoodsID','BulkQty','PackQty'],'datalist':[{'CoEId':'8002','GoodsID':'10478','BulkQty':0,'PackQty':1},{'CoEId':'8000','GoodsID':'10021','BulkQty':0,'PackQty':1},{'CoEId':'8000','GoodsID':'10002','BulkQty':0,'PackQty':1},{'CoEId':'8002','GoodsID':'10495','BulkQty':0,'PackQty':1},{'CoEId':'8002','GoodsID':'10432','BulkQty':0,'PackQty':1}]}";
        UmxService umxService = new UmxService();
        try {
            System.out.println(JSONObject.fromObject(umxService.getSendMsgEntity(JSONObject.fromObject(param))).toString());
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
