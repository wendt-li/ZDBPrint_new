/*
 * 文 件 名:  ResponseCodeEnum.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月23日
 */
package com.slic.enums;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public enum ResponseCodeEnum {
    SUCCESS("00","成功!"),ERROR("01","失败!"),UNKNOWHOST("02","未知服务器/端口!");
    private String responseCode;
    private String responseMsg;
    private ResponseCodeEnum(String responseCode,String responseMsg){
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
    }
    /**
     * @return 返回 responseCode
     */
    public String getResponseCode() {
        return responseCode;
    }
    /**
     * @return 返回 responseMsg
     */
    public String getResponseMsg() {
        return responseMsg;
    }
}
