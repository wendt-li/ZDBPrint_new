package com.slic.entity;

public class DataBlockEntity{
    // 字段类型
    private int type;
    // 
    private DataEntity data;
    // 字段长度
    private int _data_len;
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
     * @return 返回 data
     */
    public DataEntity getData() {
        return data;
    }
    /**
     * @param 对data进行赋值
     */
    public void setData(DataEntity data) {
        this.data = data;
    }
    /**
     * @return 返回 _data_len
     */
    public int get_data_len() {
        return _data_len;
    }
    /**
     * @param 对_data_len进行赋值
     */
    public void set_data_len(int _data_len) {
        this._data_len = _data_len;
    }
    /** {@inheritDoc} */
     
    @Override
    public String toString() {
        return "ParamInner [type=" + type + ", data=" + data + ", _data_len=" + _data_len + "]";
    }
    
}