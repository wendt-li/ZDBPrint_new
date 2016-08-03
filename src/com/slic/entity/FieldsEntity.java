/*
 * 文 件 名:  FiledsEntity.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.entity;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class FieldsEntity {
    private String name;
    private int type;
    private int attr;
    /**
     * @return 返回 name
     */
    public String getName() {
        return name;
    }
    /**
     * @param 对name进行赋值
     */
    public void setName(String name) {
        this.name = name;
    }
    /**
     * @return 返回 type
     */
    public int getType() {
        return type;
    }
    /**
     * @param 对type进行赋值
     */
    public void setType(int type) {
        this.type = type;
    }
    /**
     * @return 返回 attr
     */
    public int getAttr() {
        return attr;
    }
    /**
     * @param 对attr进行赋值
     */
    public void setAttr(int attr) {
        this.attr = attr;
    }
    /** {@inheritDoc} */
     
    @Override
    public String toString() {
        return "FiledsEntity [name=" + name + ", type=" + type + ", attr=" + attr + "]";
    }
    
}
