/**
 * 
 */
package org.sword.ocean.web.common;

import java.util.Date;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.sword.ocean.common.security.CipherUtil;
import org.sword.ocean.common.utils.AppConfig;
import org.sword.ocean.web.utils.RequestErrorCode;


/**
 * @author ChengNing
 * @date   2015年11月25日
 */
@Service
public class AccessValidation {
	
	@Resource
	private AppConfig appConfig;
	
	private static String PRIVATE_KEY = "12345678";
	private static String DOMAIN = "SWORD";
	private static String TOKEN_SEP = "#";
	
	
	/**
	 * 
	 * @param request
	 * @return
	 */
	public static Token getToken(HttpServletRequest request){
		Token token = new Token();
		String tokenStr = request.getParameter("token");
		tokenStr = CipherUtil.DESDecrypt(tokenStr, PRIVATE_KEY);
		String[] items = StringUtils.split(tokenStr,TOKEN_SEP);	
		token.setAppId(items[0]);
		token.setDomain(items[1]);
		token.setUserName(items[2]);
		token.setTime(items[3]);

		return token;
	}
	
	/**
	 * 数据服务验证API
	 * @param request
	 * @return
	 */
	public String validDataApi(HttpServletRequest request){
		String token = request.getParameter("token");
		token = CipherUtil.DESDecrypt(token, PRIVATE_KEY);
		if(StringUtils.isBlank(token))
			return RequestErrorCode.ValidAccess.TOKEN_ERROR;
		String[] items = StringUtils.split(token,TOKEN_SEP);
		if(items.length < 4)
			return RequestErrorCode.ValidAccess.PARAM_COUNT;
		String appID = items[0];
		String domain = items[1];
		String userName = items[2];
		String time = items[3];
		
		String errorCode = validTime(time);
		if(StringUtils.isNoneBlank(errorCode))
			return errorCode;
		
		errorCode = validAppID(appID);
		if(StringUtils.isNoneBlank(errorCode))
			return errorCode;
		
		errorCode = validDomain(domain);
		if(StringUtils.isNoneBlank(errorCode))
			return errorCode;
		
		errorCode = validUser(userName);
		if(StringUtils.isNoneBlank(errorCode))
			return errorCode;
		
		
		return null;
	}
	
	/**
	 * 
	 * @param appid
	 * @return
	 */
	private String validAppID(String appid){
		String accessAppID = appConfig.getClientApp();
		if(accessAppID.contains(appid))
			return null;
		return RequestErrorCode.ValidAccess.APPID_ERROR;
		
	}
	
	/**
	 * 
	 * @param domain
	 * @return
	 */
	private String validDomain(String domain){
		if(StringUtils.equals(domain.toUpperCase(), DOMAIN))
			return null;

		return RequestErrorCode.ValidAccess.DOMAIN_ERROR;
		
	}
	
	/**
	 * 
	 * @param user
	 * @return
	 */
	private String validUser(String user){
		return null;
		
	}
	
	private String validTime(String time){
		Date now = new Date();
		long nowTime = now.getTime();
		long tokenTime = Long.valueOf(time);
		long timeSpan = nowTime - tokenTime;
		long timeout = 1000 * Long.valueOf(appConfig.getPingTimeoutSecond()) ;
		if(timeSpan < timeout)
			return null;
		return RequestErrorCode.ValidAccess.TIMEOUT_ERROR;
	}
	

}
