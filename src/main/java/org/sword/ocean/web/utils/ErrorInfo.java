package org.sword.ocean.web.utils;

/**
 * 错误信息，用于http请求返回json格式错误信息
 * @author ChengNing
 * @date   2015年3月11日
 */
public class ErrorInfo {
	private String code;
	private String msg;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
