package com.slic.entity;

import java.util.Arrays;

public class DataEntity{
    private String type;
    // 字段内容
    private byte[] data;
    /**
     * @return 返回 type
     */
    public String getType()
    {
        return type;
    }
    /**
     * @param 对type进行赋值
     */
    public void setType(String type)
    {
        this.type = type;
    }
    /**
     * @return 返回 data
     */
    public byte[] getData()
    {
        return data;
    }
    /**
     * @param 对data进行赋值
     */
    public void setData(byte[] data)
    {
        this.data = data;
    }
    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "ParamInner1 [type=" + type + ", data=" + Arrays.toString(data) + "]";
    }
}