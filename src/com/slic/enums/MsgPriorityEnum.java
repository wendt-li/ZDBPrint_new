/*
 * 文 件 名:  MsgPriorityEnum.java
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
public enum MsgPriorityEnum {
    MP_LOWEST(1),MP_LOW(2),MP_NORMAL(3),MP_HIGH(4),MP_URGENT(5);
    
    private int value ;
    private MsgPriorityEnum(int value){
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
    }}
