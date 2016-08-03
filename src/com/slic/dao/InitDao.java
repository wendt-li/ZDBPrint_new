/*
 * 文 件 名:  InitDao.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年9月14日
 */
package com.slic.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年9月14日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class InitDao {
    private static Logger logger = LoggerFactory.getLogger(InitDao.class);
    private InitDao(){}
    public static Properties init() throws IOException{
        InputStream in = InitDao.class.getClassLoader().getResourceAsStream("config.properties");
        Properties props = new Properties();
        props.load(in);
        return props;
    }
    
    public static void main(String[] args){
        logger.debug("初始化配置文件config.properties结束......");
    }
    
}
