/*
 * 文 件 名:  MsgTypeEnum.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.enums;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public enum MsgTypeEnum {
    // 5 请求包 6 确认包 7 通知包 8 响应包 
    MT_UNKOWN(0),MT_REQUEST(5),MT_CONFIRMATION(6),MT_INDICATION(7),MT_RESPONSE(8);
    
    private int value ;
    private MsgTypeEnum(int value){
        this.value = value;
    }
    /**
     * @return 返回 value
     */
    public int getValue() {
        return value;
    }
    /**
     * @param 对value进行赋值
     */
    public void setValue(int value) {
        this.value = value;
    }
    
    
}
