/*
 * 文 件 名:  PropertyTypeEnum.java
 * 版    权:  SKYLINK INFOMATION CO.,LTD
 * 描    述:  <描述>
 * 创 建 人:  liangweilun
 * 创建时间:  2015年7月22日
 */
package com.slic.enums;

/**
 * 字段类型
 * 
 * @author liangweilun
 * @version  [V1.0.0.0, 2015年7月22日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
public enum PropertyTypeEnum {
    DTUNKNOWN(0),
    DTTINYINT(16),
    DTSMALLINT(2),
    DTINTEGER(3),
    DTBIGINT(20),
    DTUNSIGNEDTINYINT(17),
    DTUNSIGNEDSMALLINT(18),
    DTUNSIGNEDINT(19),
    DTUNSIGNEDBIGINT(21),
    DTFLOAT(4),
    DTDOUBLE(5),
    DTBOOLEAN(11),
    DTDATE(7),
    DTTIME(134),
    DTDATETIME(135),
    DTCHAR(129),
    DTVARCHAR(200),
    DTLONGVARCHAR(201),
    DTWCHAR(130),
    DTWVARCHAR(202),
    DTWLONGVARCHAR(203),
    DTBINARY(128),
    DTVARBINARY(204),
    DTLONGVARBINARY(205),
    DTMSG(151),
    DTMEDIUMMSG(152),
    DTLONGMSG(153),
    DTROWSET(154),
    DTMEDIUMROWSET(155),
    DTLONGROWSET(156);
    
    private int value;
    private PropertyTypeEnum(int value){
        this.value = value;
    }
    /**
     * @return 返回 value
     */
    public int getValue() {
        return value;
    }
    /**
     * @param 对value进行赋值
     */
    public void setValue(int value) {
        this.value = value;
    }
    
}

//dtUnknown         : 0,
//dtTinyInt         : 16,
//dtSmallInt        : 2, 
//dtInteger         : 3,
//dtBigInt          : 20,
//dtUnsignedTinyInt : 17,
//dtUnsignedSmallInt: 18,
//dtUnsignedInt     : 19,
//dtUnsignedBigInt  : 21,
//dtFloat           : 4,
//dtDouble          : 5 ,
//dtBoolean         : 11,
//dtDate            : 7,
//dtTime            : 134,        
//dtDateTime        : 135,
//dtChar            : 129,                
//dtVarChar         : 200,            
//dtLongVarChar     : 201,    
//dtWChar           : 130,                
//dtWVarChar        : 202,        
//dtWLongVarChar    : 203,
//dtBinary          : 128,            
//dtVarBinary       : 204,        
//dtLongVarBinary   : 205,   
//dtMsg             : 151,    
//dtMediumMsg       : 152,    
//dtLongMsg         : 153,      
//dtRowset          : 154,
//dtMediumRowset    : 155,
//dtLongRowset      : 156,