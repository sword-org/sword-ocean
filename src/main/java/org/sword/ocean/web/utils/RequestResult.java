/**
 * 
 */
package org.sword.ocean.web.utils;

/**
 * aciton请求json结果协议
 * @author ChengNing
 * @date   2015年3月11日
 */
public class RequestResult {
	
	private String result;
	private String msg;
	private Object data; //成功时成功对象，失败时候返回失败对象
	
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	
	
}
