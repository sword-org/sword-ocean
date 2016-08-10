/**
 * 
 */
package org.sword.ocean.common.utils;

import org.apache.commons.lang3.StringUtils;


/**
 * @author ChengNing
 * @date   2015年4月27日
 */
public class XBIUtils {
	
	public static final String SEP = ",";
	
	/**
	 * 数组转成字符串，用逗号分隔
	 * @param arr
	 * @return
	 */
	public static String arrayToStr(Object[] arr){
		return arrayToStr(arr, null);
	}
	
	/**
	 * 数组转成字符串
	 * @param arr
	 * @param sep 分隔符
	 * @return
	 */
	public static String arrayToStr(Object[] arr,String sep){
		if(sep == null)
			sep = ",";
		StringBuilder sb = new StringBuilder();
		for (Object obj : arr) {
	        sb.append(obj.toString() + sep);
        }
		return StringUtils.stripEnd(sb.toString(), sep);
	}
	
	
}
