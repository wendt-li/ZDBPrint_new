package com.slic.entity;
/*
 * 文 件 名:  ParamEntity.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */

/**
 * 普通搜索参数
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class ParamEntity
{
    // 字段名字
    private String name;
    // 数据
    private DataBlockEntity data;
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
     * @return 返回 data
     */
    public DataBlockEntity getData() {
        return data;
    }
    /**
     * @param 对data进行赋值
     */
    public void setData(DataBlockEntity data) {
        this.data = data;
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
        return "ParamEntity [name=" + name + ", data=" + data + ", attr=" + attr + "]";
    }
    
    
}
