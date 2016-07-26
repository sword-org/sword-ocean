/**
 * 
 */

package com.sword.ocean.web.controller;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.util.JSONPObject;

import com.alibaba.fastjson.JSONObject;
import com.sword.ocean.web.utils.ErrorInfo;
import com.sword.ocean.web.utils.RequestResult;
import com.sword.ocean.web.utils.Result;




/**
 * controller超类，所有的controller都需要继承
 * @author ChengNing
 * @date   2015年03月10日
 */
public class BaseController {

	public static final String CALLBACK = "callback";
	public static final String UNDEFINED = "undefined";
	protected String callback;
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotBlank(String str){
		return !isBlank(str);
	}
	
	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isBlank(String str){
		if(StringUtils.isBlank(str))
			return true;
		if(str.equals(UNDEFINED))
			return true;
		return false;
	}
	
	/**
	 * 以json格式返回ReqeustResult成功对象
	 * @param obj  java对象
	 * @return
	 */
	public JSONObject renderSuccess(Object obj){
		return renderObject(Result.success,obj);
	}
	
	/**
	 * 以json格式返回ReqeustResult失败对象
	 * @param obj java对象
	 * @return
	 */
	public JSONObject renderFailed(ErrorInfo obj){
		return renderObject(Result.failed,obj);
	}
	
	/**
	 * 以json格式返回obj对象为data数据,默认success，msg为空
	 * @param obj    返回结果数据
	 * @return
	 */
	public JSONObject renderObject(Object obj){
		return renderObject(Result.success,null,obj);
	}
	
	/**
	 * 以json格式返回ReqeustResult对象
	 * @param result 返回的结果状态
	 * @param obj    返回结果数据
	 * @return
	 */
	public JSONObject renderObject(Result result,Object obj){
		return renderObject(result,null,obj);
	}
	
	/**
	 * 以json格式返回ReqeustResult对象
	 * @param result  返回的结果状态
	 * @param msg     返回结果信息
	 * @param obj     返回结果数据
	 * @return
	 */
	public JSONObject renderObject(Result result,String msg,Object obj){
		RequestResult jsonObj = new RequestResult();
		jsonObj.setResult(result.name());
		jsonObj.setMsg(msg);
		jsonObj.setData(obj);
		return (JSONObject)JSONObject.toJSON(jsonObj);
	}
	
	/**
	 * 以json格式返回SimpleResult对象
	 * @param msg
	 * @return
	 */
	public JSONObject renderSuccess(String msg){
		return renderMsg(Result.success,msg);
	}
	
	/**
	 * 以json格式返回SimpleResult对象
	 * @param msg
	 * @return
	 */
	public JSONObject renderFailed(String msg){
		return renderMsg(Result.failed,msg);
	}
	
	
	/**
	 * 以json格式返回SimpleResult对象
	 * @param result
	 * @param resultInfo
	 * @return
	 */
	public JSONObject renderMsg(Result result,String resultInfo){
		RequestResult jsonObj = new RequestResult();
		jsonObj.setResult(result.name());
		jsonObj.setMsg(resultInfo);
		return (JSONObject)JSONObject.toJSON(jsonObj);
	}
	
	public JSONObject renderJson(Object obj){
		return renderObject(Result.success, obj);
	}
	
	/**
	 * 以json格式返回SimpleResult对象
	 * @param obj
	 * @param callback
	 * @return
	 */
	public JSONPObject renderJsonP(Object obj,String callback){
		JSONObject jsonObject = renderJson(obj);
		JSONPObject jsonp = new JSONPObject(callback,jsonObject);
		return jsonp;
	}
	
	/**
	 * 返回json对象，如果callback存在则支持jsonp，如果不存在则返回json
	 * @param obj
	 * @return
	 */
	public Object render(Object obj){
		String callback = this.callback;
		if(StringUtils.isNotBlank(callback))
			return renderJsonP(obj,callback);
		else {
			return renderJson(obj);
		}
	}
	
	/**
	 * 返回json对象，如果callback存在则支持jsonp，如果不存在则返回json
	 * @param success
	 * @param msg
	 * @return
	 */
	public Object render(boolean success,String msg){
		JSONObject obj = new JSONObject();
		if(success)
			obj = renderSuccess(msg);
		else {
			obj = renderFailed(msg);
		}
		if(StringUtils.isNotBlank(callback)){
		    JSONPObject jsonp = new JSONPObject(callback,obj);
			return jsonp;
		}
		return obj;
	}
	
	/**
	 * 简单返回成功或者失败
	 * @param success
	 * @return
	 */
	public Object render(boolean success){
		return render(success, null);
	}

}
