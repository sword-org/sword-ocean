/**
 * 
 */
package com.sword.ocean.web.utils;

/**
 * @author ChengNing
 * @date   2015年11月25日
 */
public class RequestErrorCode {

	/**
	 * 访问控制错误码
	 * @author ChengNing
	 * @date   2015年11月25日
	 */
	public static class ValidAccess{
		/**
		 * 参数个数错误
		 */
		public static String PARAM_COUNT      = "00000001";
		/**
		 * token非法
		 */
		public static String TOKEN_ERROR      = "00000002";
		/**
		 * domain非法
		 */
		public static String DOMAIN_ERROR     = "00000003";
		/**
		 * user非法
		 */
		public static String USER_ERROR       = "00000004";
		/**
		 * 超时
		 */
		public static String TIMEOUT_ERROR    = "00000005";
		/**
		 * appid非法
		 */
		public static String APPID_ERROR      = "00000006";
	}
}
