/*
 * 文 件 名:  RowSetEntity.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.entity;

import java.util.Arrays;
import java.util.List;

/**
 * 行集
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class RowSetEntity {
    //元数据 描述 new Array("RST_ID","RST_RESERVED","RST_ROW_CNT","RST_FLD_NUM");
    private int[] meta = new int[4];
    // 字段列表
    private List<FieldsEntity> fields;
    // 数据列表
    private List<List<DataBlockEntity>> data;
    /**
     * @return 返回 meta
     */
    public int[] getMeta() {
        return meta;
    }
    /**
     * @param 对meta进行赋值
     */
    public void setMeta(int[] meta) {
        this.meta = meta;
    }
    /**
     * @return 返回 fields
     */
    public List<FieldsEntity> getFields() {
        return fields;
    }
    /**
     * @param 对fields进行赋值
     */
    public void setFields(List<FieldsEntity> fields) {
        this.fields = fields;
    }
    /**
     * @return 返回 data
     */
    public List<List<DataBlockEntity>> getData() {
        return data;
    }
    /**
     * @param 对data进行赋值
     */
    public void setData(List<List<DataBlockEntity>> data) {
        this.data = data;
    }
    /** {@inheritDoc} */
     
    @Override
    public String toString() {
        return "RowSetEntity [meta=" + Arrays.toString(meta) + ", fields=" + fields + ", data=" + data + "]";
    }
    
}
