/**
 * 
 */
package com.sword.ocean.common.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author ChengNing
 * @date   2015年6月24日
 */
@Service
public class AppConfig {
	@Value("${show_sql}")
	private boolean showSQL = false;
	@Value("${client.appid}")
	private String clientApp;
	@Value("${ping.timeout.second}")
	private String pingTimeoutSecond;
	
	
	public boolean getShowSQL() {
		return showSQL;
	}
	public void setShowSQL(boolean showSQL) {
		this.showSQL = showSQL;
	}
	public String getClientApp() {
		return clientApp;
	}
	public void setClientApp(String clientApp) {
		this.clientApp = clientApp;
	}
	public String getPingTimeoutSecond() {
		return pingTimeoutSecond;
	}
	public void setPingTimeoutSecond(String pingTimeoutSecond) {
		this.pingTimeoutSecond = pingTimeoutSecond;
	}



	
	
	
}
