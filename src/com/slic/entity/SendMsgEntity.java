/*
 * 文 件 名:  SendMsgEntity.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.entity;

import java.util.List;

/**
 * 发送消息对象
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class SendMsgEntity {
    private HeaderEntity header;
    private List<ParamEntity> params;
    private List<RowSetEntity> rowSets;
    /**
     * @return 返回 header
     */
    public HeaderEntity getHeader() {
        return header;
    }
    /**
     * @param 对header进行赋值
     */
    public void setHeader(HeaderEntity header) {
        this.header = header;
    }
    /**
     * @return 返回 params
     */
    public List<ParamEntity> getParams() {
        return params;
    }
    /**
     * @param 对params进行赋值
     */
    public void setParams(List<ParamEntity> params) {
        this.params = params;
    }
    /**
     * @return 返回 rowSets
     */
    public List<RowSetEntity> getRowSets() {
        return rowSets;
    }
    /**
     * @param 对rowSets进行赋值
     */
    public void setRowSets(List<RowSetEntity> rowSets) {
        this.rowSets = rowSets;
    }
    /** {@inheritDoc} */
     
    @Override
    public String toString() {
        return "SendMsgEntity [header=" + header + ", params=" + params + ", rowSets=" + rowSets + "]";
    }
    
    
}
