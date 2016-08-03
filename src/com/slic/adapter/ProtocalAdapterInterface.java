/*
 * 文 件 名:  ProtocalAdapterInterface.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月24日
 */
package com.slic.adapter;

import java.util.Map;

/**
 * 适配器接口
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月24日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public interface ProtocalAdapterInterface {
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param key
     * @param path
     * @param source
     * @return
     * @see [类、类#方法、类#成员]
     */
    public Object getParamValue(String path,String source);
    
    
//    public Map<String,Object> getParams(String[] keys,String[] paths,String source);
}
