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
import com.sword.ocean.service.BIService;
import com.sword.ocean.service.DataDriver;
import com.sword.ocean.web.common.AccessValidation;
import com.sword.ocean.web.common.ControllerUtils;
import com.sword.ocean.web.common.Token;




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

	public static final String PARAM_TYPE = "type";
	public static final String AUTH_ERROR_MSG = "对此数据没有权限";
	
	
	@Resource
	private DataDriver dataDriver;
	@Resource
	private BIService biService;
	
	
	/**
	 * 数据访问权限
	 * @param type
	 * @param user
	 * @return
	 */
	private boolean validAuth(HttpServletRequest request,DataSourceEnum ds){
		String type = request.getParameter("type");
		Token token = AccessValidation.getToken(request);
		String userName = token.getUserName();
		
		Map paramsMap = new HashMap<String, String>();
		paramsMap.put("type", "userAuth");
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

}
