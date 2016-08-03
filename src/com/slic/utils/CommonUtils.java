/*
 * 文 件 名:  CommonUtils.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年8月10日
 */
package com.slic.utils;

import java.util.List;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年8月10日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class CommonUtils {
    public static boolean isNull(Object obj){
        return obj == null;
    }
    public static boolean isNotNull(Object obj){
        return obj != null;
    }
    public static boolean strIsEmpty(String str){
        if(str == null || "".equals(str.trim())){
            return true;
        }
        return false;
    }
    public static boolean strIsNotEmpty(String str){
        if(str != null && !"".equals(str.trim())){
            return true;
        }
        return false;
    }
    public static boolean listIsEmpty(List list){
        if(list == null || list.size() == 0){
            return true;
        }
        return false;
    }
    public static boolean listIsNotEmpty(List list){
        if(list != null && list.size() > 0){
            return true;
        }
        return false;
    }
}
