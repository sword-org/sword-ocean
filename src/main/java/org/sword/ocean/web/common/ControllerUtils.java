/**
 * 
 */
package org.sword.ocean.web.common;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.sword.ocean.common.utils.XBIUtils;



/**
 * 
 * controller 界面层工具集
 * @author ChengNing
 * @date   2015年3月25日
 */
public class ControllerUtils {
	
	/**
	 * request参数转为map
	 * @param request
	 * @return
	 */
	public static Map<String, Object> getParamMap(HttpServletRequest request){
        Map<String, Object> map = new HashMap();
        Enumeration<String> names = request.getParameterNames();
        while(names.hasMoreElements()){
            String key = names.nextElement();
            Object[] value = request.getParameterValues(key);
            if(key.equals("token") || value == null){
                continue;
            }
            String s = XBIUtils.arrayToStr(value);
            map.put(key, s);
        }
        return map;
	}

	
	
}
