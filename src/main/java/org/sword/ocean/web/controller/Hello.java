package org.sword.ocean.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.sword.ocean.web.utils.BaseController;

import com.alibaba.fastjson.JSONObject;

/**
 * sword 测试页
 * @author ChengNing
 * @date   2016年7月26日
 */
@Controller
@RequestMapping(value="sword")
public class Hello extends BaseController{
	
	/**
	 * sword服务运行成功
	 * @param request
	 * @return
	 */
	@RequestMapping(value="index")
	@ResponseBody
	public JSONObject index(HttpServletRequest request){
		JSONObject object = renderSuccess("sword-ocean 服务运行成功"); 
		return object;
	}
	
	/**
	 * sword包加载成功
	 * @param request
	 * @return
	 */
	@RequestMapping(value="test")
	@ResponseBody
	public String test(HttpServletRequest request){
		return "sword-ocean 包加载成功";
	}
}
