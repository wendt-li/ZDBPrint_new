/*
 * 文 件 名:  Constant.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月23日
 */
package com.slic.entity;

/**
 * 常量类
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月23日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface Constant {
    // 编码方式
    String ENCODING = "GBK";
    
    String HANDLEID_NAME = "handleid";
    
    // 包头长度定义
    int HEADER_LENGTH = 52;
    
    // 默认超时时间 15s
    int DEFAULT_TIMEOUT = 15000;
    
    int REQUEST_FROM = 5;
    String REQUESTFROM_NAME = "requestFrom";
    
    String SEND_TYPE_NAME = "type";
    String SEND_TYPE = "TCP";
    // 接口访问标识位字段名
    String BACK_ACCESS_FLAG_FIELD = "#sc";
    String BACK_ACCESS_SUCCESS_FLAG = "0";
    // 横向
    String PRINT_DIRCETION_HORIZONTAL = "1";
    // 纵向
    String PRINT_DIRCETION_VERTICAL = "0";
    
}
