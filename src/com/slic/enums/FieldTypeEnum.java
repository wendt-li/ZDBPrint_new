/*
 * 文 件 名:  FieldTypeEnum.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 字段类型
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public enum FieldTypeEnum {
    NORMAL(0),SENSITIVE(1),REBACK(2);
    
    private int value;
    private FieldTypeEnum(int value){
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

/**
 * 参数类型
 * <功能详细描述>
 * @return
 * @see [类、类#方法、类#成员]
 */
//public Map<String,String> paramType(){
//    Map<String,String> paramType = new HashMap<>();
//    paramType.put("普通", "0");
//    paramType.put("敏感数据 ", "1");
//    paramType.put("原样返回参数", "2");
//    return paramType;
//}