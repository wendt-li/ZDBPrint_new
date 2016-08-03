/*
 * 文 件 名:  HeaderEntity.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.entity;

import com.slic.enums.MsgPriorityEnum;
import com.slic.enums.MsgTypeEnum;

/**
 * <一句话功能简述>
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public class HeaderEntity {
    
    private int version=4;                                                                    
    private int domain=0;                                                                     
    private int msgType=MsgTypeEnum.MT_REQUEST.getValue();                                                    
    private int msgId=0;                                                                      
    private int msgVersion=0;     ///< 消息协议版本 = 1                                       
    private int cmdSerial=0;        ///< 命令字                                               
    private int srcType=0;                                                                    
    private int hiSrcID=0; ///< 源ID高32位                                                    
    private int srcID=0;                                                                      
    private int destType=0;                                                                   
    private int hiDestID=0; ///< 目标ID高32位                                                 
    private int destID=0;                                                                     
    private int msgAttr=0; ///< 消息属性                                                      
    private int msgPriority=MsgPriorityEnum.MP_NORMAL.getValue();              ///< 消息优先级别             
    private int timestamp=0;            ///< 时间戳                                           
    private int charset=0;                  ///< 字符集编码                                   
    private int totalLen=0;                 ///< 包总长度                                     
    private int extAttr=0;           ///< 扩展属性:可用于其它属性设置(如用于时序控制的数值)    
    /**
     * @return 返回 version
     */
    public int getVersion() {
        return version;
    }
    /**
     * @param 对version进行赋值
     */
    public void setVersion(int version) {
        this.version = version;
    }
    /**
     * @return 返回 domain
     */
    public int getDomain() {
        return domain;
    }
    /**
     * @param 对domain进行赋值
     */
    public void setDomain(int domain) {
        this.domain = domain;
    }
    /**
     * @return 返回 msgType
     */
    public int getMsgType() {
        return msgType;
    }
    /**
     * @param 对msgType进行赋值
     */
    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }
    /**
     * @return 返回 msgId
     */
    public int getMsgId() {
        return msgId;
    }
    /**
     * @param 对msgId进行赋值
     */
    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }
    /**
     * @return 返回 msgVersion
     */
    public int getMsgVersion() {
        return msgVersion;
    }
    /**
     * @param 对msgVersion进行赋值
     */
    public void setMsgVersion(int msgVersion) {
        this.msgVersion = msgVersion;
    }
    /**
     * @return 返回 cmdSerial
     */
    public int getCmdSerial() {
        return cmdSerial;
    }
    /**
     * @param 对cmdSerial进行赋值
     */
    public void setCmdSerial(int cmdSerial) {
        this.cmdSerial = cmdSerial;
    }
    /**
     * @return 返回 srcType
     */
    public int getSrcType() {
        return srcType;
    }
    /**
     * @param 对srcType进行赋值
     */
    public void setSrcType(int srcType) {
        this.srcType = srcType;
    }
    /**
     * @return 返回 hiSrcID
     */
    public int getHiSrcID() {
        return hiSrcID;
    }
    /**
     * @param 对hiSrcID进行赋值
     */
    public void setHiSrcID(int hiSrcID) {
        this.hiSrcID = hiSrcID;
    }
    /**
     * @return 返回 srcID
     */
    public int getSrcID() {
        return srcID;
    }
    /**
     * @param 对srcID进行赋值
     */
    public void setSrcID(int srcID) {
        this.srcID = srcID;
    }
    /**
     * @return 返回 destType
     */
    public int getDestType() {
        return destType;
    }
    /**
     * @param 对destType进行赋值
     */
    public void setDestType(int destType) {
        this.destType = destType;
    }
    /**
     * @return 返回 hiDestID
     */
    public int getHiDestID() {
        return hiDestID;
    }
    /**
     * @param 对hiDestID进行赋值
     */
    public void setHiDestID(int hiDestID) {
        this.hiDestID = hiDestID;
    }
    /**
     * @return 返回 destID
     */
    public int getDestID() {
        return destID;
    }
    /**
     * @param 对destID进行赋值
     */
    public void setDestID(int destID) {
        this.destID = destID;
    }
    /**
     * @return 返回 msgAttr
     */
    public int getMsgAttr() {
        return msgAttr;
    }
    /**
     * @param 对msgAttr进行赋值
     */
    public void setMsgAttr(int msgAttr) {
        this.msgAttr = msgAttr;
    }
    /**
     * @return 返回 msgPriority
     */
    public int getMsgPriority() {
        return msgPriority;
    }
    /**
     * @param 对msgPriority进行赋值
     */
    public void setMsgPriority(int msgPriority) {
        this.msgPriority = msgPriority;
    }
    /**
     * @return 返回 timestamp
     */
    public int getTimestamp() {
        return timestamp;
    }
    /**
     * @param 对timestamp进行赋值
     */
    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }
    /**
     * @return 返回 charset
     */
    public int getCharset() {
        return charset;
    }
    /**
     * @param 对charset进行赋值
     */
    public void setCharset(int charset) {
        this.charset = charset;
    }
    /**
     * @return 返回 totalLen
     */
    public int getTotalLen() {
        return totalLen;
    }
    /**
     * @param 对totalLen进行赋值
     */
    public void setTotalLen(int totalLen) {
        this.totalLen = totalLen;
    }
    /**
     * @return 返回 extAttr
     */
    public int getExtAttr() {
        return extAttr;
    }
    /**
     * @param 对extAttr进行赋值
     */
    public void setExtAttr(int extAttr) {
        this.extAttr = extAttr;
    }
    /** {@inheritDoc} */
     
    @Override
    public String toString() {
        return "HeaderEntity [version=" + version + ", domain=" + domain + ", msgType=" + msgType + ", msgId=" + msgId
            + ", msgVersion=" + msgVersion + ", cmdSerial=" + cmdSerial + ", srcType=" + srcType + ", hiSrcID="
            + hiSrcID + ", srcID=" + srcID + ", destType=" + destType + ", hiDestID=" + hiDestID + ", destID=" + destID
            + ", msgAttr=" + msgAttr + ", msgPriority=" + msgPriority + ", timestamp=" + timestamp + ", charset="
            + charset + ", totalLen=" + totalLen + ", extAttr=" + extAttr + "]";
    }

}
