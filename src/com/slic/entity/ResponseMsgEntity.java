/*
 * 文 件 名:  ResponseMsgEntity.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月23日
 */
package com.slic.entity;

import net.sf.json.JSONObject;

import com.slic.enums.ResponseCodeEnum;

/**
 * 响应数据
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ResponseMsgEntity {
    private String resCode;
    private String resMsg;
    // 响应结果
    private String responseText ;
    
    public ResponseMsgEntity(){}
    
    public ResponseMsgEntity(ResponseCodeEnum response){
        this.resCode = response.getResponseCode();
        this.resMsg = response.getResponseMsg();
    }
    
    public void setResposne(ResponseCodeEnum response){
        this.resCode = response.getResponseCode();
        this.resMsg = response.getResponseMsg();
    }
    /**
     * @return 返回 resCode
     */
    public String getResCode() {
        return resCode;
    }
    /**
     * @param 对resCode进行赋值
     */
    public void setResCode(String resCode) {
        this.resCode = resCode;
    }
    /**
     * @return 返回 resMsg
     */
    public String getResMsg() {
        return resMsg;
    }
    /**
     * @param 对resMsg进行赋值
     */
    public void setResMsg(String resMsg) {
        this.resMsg = resMsg;
    }

   
    /**
     * @return 返回 responseText
     */
    public String getResponseText() {
        return responseText;
    }

    /**
     * @param 对responseText进行赋值
     */
    public void setResponseText(String responseText) {
        this.responseText = responseText;
    }

    /** {@inheritDoc} */
     
    @Override
    public String toString() {
        return "ResponseMsgEntity [resCode=" + resCode + ", resMsg=" + resMsg + ", responseText=" + responseText + "]";
    }

}
