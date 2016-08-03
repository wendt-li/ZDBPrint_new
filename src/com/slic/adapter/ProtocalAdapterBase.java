/*
 * 文 件 名:  ProtocalAdapter_7169.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月24日
 */
package com.slic.adapter;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月24日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ProtocalAdapterBase implements ProtocalAdapterInterface {

    /** {@inheritDoc} */
     
    @Override
    public Object getParamValue(String path, String source) {
        JSONObject sourceJson = JSONObject.fromObject(source);
        // 解析路径
        String[] paths = path.split("\\.");
        JSONObject _temp = sourceJson;
        for(int i=0,len=paths.length;i<len;i++){
            if(i == len - 1){
                return _temp.get(paths[i]);
            }
            _temp = _temp.getJSONObject(paths[i]);
        }
        return null;
    }

    /** {@inheritDoc} */
     
//    @Override
//    public Map<String, Object> getParams(String[] keys, String[] paths, String source) {
//        Map<String,Object> result = new HashMap<String, Object>();
//        for(int i=0;i<keys.length;i++){
//            result.put(keys[i], getParamValue(paths[i],source));
//        }
//        return result;
//    }
    
}
