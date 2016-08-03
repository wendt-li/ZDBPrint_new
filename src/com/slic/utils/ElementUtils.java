package com.slic.utils;

import org.jdom.Element;



/**
 * 
 * @author LiuRong
 *2016-5-26
 * 类说明:xml元素操作工具
 */
public class ElementUtils {

	/**
	 * 判断数据是否为空 空值或空字符串都会返回true
	 * @param ele
	 * @param name
	 * @return 
	 */
	public static boolean isEmpty(Element ele,String name){
		String value = ele.getAttributeValue(name);
		return value==null || "".equals(value) ;
	}
	
	/**
	 * 属性转换为整形
	 * @param ele
	 * @param name
	 * @param defalueValue
	 * @return
	 */
	public static Integer getValueInt(Element ele,String name,Integer defalueValue){
		
		if(isEmpty(ele, name)){
			return defalueValue;
		}
		String value = ele.getAttributeValue(name);
		try {
			return Integer.parseInt(value);
		} catch (Exception e) {
			return defalueValue ;
		}
	}
	
	/**
	 * 属性转换为浮点型
	 * @param ele
	 * @param name
	 * @param defalueValue
	 * @return
	 */
	public static Double getValueDou(Element ele,String name,Double defalueValue){
		if(isEmpty(ele, name)){
			return defalueValue;
		}
		String value = ele.getAttributeValue(name);
		try {
			return Double.parseDouble(value);
		} catch (Exception e) {
			return defalueValue ;
		}
	}
	
	/**
	 * 属性转换为常整形
	 * @param ele
	 * @param name
	 * @param defalueValue
	 * @return
	 */
	public static Long getValueLong(Element ele,String name,Long defalueValue){
		if(isEmpty(ele, name)){
			return defalueValue;
		}
		String value = ele.getAttributeValue(name);
		try {
			return Long.parseLong(value);
		} catch (Exception e) {
			return defalueValue ;
		}
	}
	
	/**
	 * 属性转换为字符串，如果是空 则返回空串
	 * @param ele
	 * @param name
	 * @param defalueValue
	 * @return
	 */
	public static String getValueStr(Element ele,String name){
		String value =ele.getAttributeValue(name);
		if(value==null){
			return "";
		}
		return value;
	}

}
