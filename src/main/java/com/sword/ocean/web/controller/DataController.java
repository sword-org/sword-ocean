/**
 * 
 */
package com.sword.ocean.web.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;






import com.alibaba.fastjson.JSONObject;
import com.sword.ocean.common.enumType.DataSourceEnum;
import com.sword.ocean.common.utils.AppConfig;
import com.sword.ocean.service.BIService;
import com.sword.ocean.service.DataDriver;
import com.sword.ocean.web.common.AccessValidation;
import com.sword.ocean.web.common.ControllerUtils;
import com.sword.ocean.web.common.Token;
import com.sword.ocean.web.utils.BaseController;




/**
 * 
 * 数据json服务窗口
 * @author ChengNing
 * @date   2015年3月25日
 */
@Controller
@RequestMapping(value="data")
public class DataController extends BaseController {
	
	private static Logger logger = Logger.getLogger(DataController.class);
	
	@Resource
	private DataDriver dataDriver;
	@Resource
	private BIService biService;	
	@Resource
	private AppConfig appConfig;
	
	
	/**
	 * 数据访问权限
	 * @param type
	 * @param user
	 * @return
	 */
	private boolean validAuth(HttpServletRequest request,DataSourceEnum ds){
		String type = request.getParameter(PARAM_TYPE);
		Token token = AccessValidation.getToken(request);
		String userName = token.getUserName();
		
		Map paramsMap = new HashMap<String, String>();
		paramsMap.put(PARAM_TYPE, "userAuth");
		paramsMap.put("dataName", type);
		paramsMap.put("userName", userName);
		
		List<Object> data = dataDriver.query(paramsMap,ds);
		if(data == null || data.size() <= 0){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 数据库数据的json服务
	 * @param request
	 * @return
	 */
	@RequestMapping(value="index")
	@ResponseBody
	public JSONObject indicator(HttpServletRequest request){
		Map paramsMap = ControllerUtils.getParamMap(request);
		if(!validIndicatorInput(paramsMap))
			return renderFailed("参数输入不正确");
		
		//设置跨域
		setEnableJSONP(paramsMap);
		
		List<Object> data = dataDriver.query(paramsMap);
		if(data == null || data.size() <= 0){
			return renderFailed("查询无结果数据");
		}
		return renderObject(data);
	}
	
	/**
	 * 数据库数据的json服务
	 * @param request
	 * @return
	 */
	@RequestMapping(value="indexList")
	@ResponseBody
	public JSONObject indicatorList(HttpServletRequest request){
		Map paramsMap = ControllerUtils.getParamMap(request);
		if(!validIndicatorInput(paramsMap))
			return renderFailed("参数输入不正确");
		
		//设置跨域
		setEnableJSONP(paramsMap);
		
		List<Object> data = dataDriver.query(paramsMap,true);
		if(data == null || data.size() <= 0){
			return renderFailed("查询无结果数据");
		}
		return renderObject(data);
	}
	
	
	/**
	 * 校验indicator的参数
	 * @param paramsMap
	 * @return
	 */
	private boolean validIndicatorInput(Map paramsMap){
		if(paramsMap.size() <= 0)
			return false;
		if(StringUtils.isBlank(paramsMap.get(PARAM_TYPE).toString()))
			return false;
		
		return true;
	}
	
	/**
	 * 对跨域的支持
	 * 如果数据服务要支持跨域请求，请在sword-ocean.propertity中设置支持跨域的选项为true
	 * @param paramsMap
	 */
	private void setEnableJSONP(Map paramsMap){
		boolean enableJsonP = this.appConfig.isEnableJsonP();
		if(!enableJsonP)
			return;
		if(!StringUtils.isBlank(paramsMap.get(CALLBACK).toString())){
			this.callback = paramsMap.get(CALLBACK).toString();
			paramsMap.remove(CALLBACK);
		}
	}

}
