/**
 *  Copyright (c)  2011-2020 THFUND.COM.
 *  All rights reserved.
 */
package org.sword.ocean.web.utils;

/**
 * action结果，指定值方便定义
 * @author ChengNing
 * @date   2015年03月11日
 */
public enum Result {
	success(0),
	failed(1),
	error(2);
	
	private int value;
	private Result(int value){
		this.value = value;
	}
}
