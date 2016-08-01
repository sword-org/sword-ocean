package org.sword.ocean.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.sword.ocean.common.enumType.DataSourceEnum;
import org.sword.ocean.common.utils.XBIUtils;
import org.sword.ocean.dao.BIDao;


/**
 * XBI 核心逻辑
 * @author ChengNing
 * @date   2015年4月27日
 */
@Service
public class BIService {
	
	private static final String TABLE = "o";
	private static final String X_DATA = "x";
	private static final String VALUE = "y";
	private static final String LEGEND = "z";
	private static final String SQL = "sql";
	private static final String SQL_P = "param";
	
	
	
	@Resource
	private BIDao biDao;
	
	/**
	 * 根据外部输入的参数进行对应脚本查询
	 * @param paramMap   前端传入的参数map
	 * @return
	 */
	public List<Object> query(Map paramMap){
		return query(paramMap,null);
	}
	
	/**
	 * 根据外部输入的参数进行对应脚本查询
	 * @param paramMap   前端传入的参数map
	 * @return
	 */
	public List<Object> query(Map paramMap,DataSourceEnum ds){
		Map<String, Object> sqlMap = builderSQL(paramMap);
		String sqlString = sqlMap.get(SQL).toString();
		List params = (List)sqlMap.get(SQL_P);
		return biDao.query(sqlString,params.toArray());
	}
	
	/**
	 * 构建SQL
	 * @param paramMap
	 * @return
	 */
	private Map<String, Object> builderSQL(Map paramMap){
		Map<String, Object> sqlMap = new HashMap<String, Object>();
		String x = paramMap.get(X_DATA).toString();
		String y = paramMap.get(VALUE).toString();
		String legend = paramMap.get(LEGEND).toString();
		String table = paramMap.get(TABLE).toString();
		boolean isnull = true;//是否判空
		//前端增加统计函数，针对每列的统计函数
		String sql = "select " + x + ",sum(" + y + ") " + y + "," + legend + " from " + table + 
				" where " + x + " is not null  and trim(" + x + ") <> '' "
						+ " and " + legend + " is not null and trim(" + legend + ") <> ''";
		List<String> list = new ArrayList<String>();
		
		sqlMap.put(SQL, sql);
		sqlMap.put(SQL_P, list);
		sqlMap = builderCondition(paramMap,sqlMap);
		sql = sqlMap.get(SQL).toString();
		sql += " group by " + x + "," + legend;
		sqlMap.put(SQL, sql);
		
		return sqlMap;
	}
	
	/**
	 * BI传入的参数是否是条件参数
	 * @param key
	 * @return
	 */
	private boolean isNotWhere(String key){
		String [] arr = {X_DATA,VALUE,LEGEND,TABLE};
		if(ArrayUtils.contains(arr, key)){
			return true;
		}
		return false;
	}
	
	/**
	 * 构建SQL参数
	 * @param paramMap
	 * @return
	 */
	private Map<String, Object> builderCondition(Map paramMap,Map<String, Object> sqlMap){
		StringBuilder sb = new StringBuilder();
		Set<String> keySet = paramMap.keySet();
		for(String key : keySet){
			if(isNotWhere(key))
				continue;
			String value = paramMap.get(key).toString();
			sqlMap = paramToWhere(key,value,sqlMap);
		}
		return sqlMap;
	}
	
	/**
	 * 针对每个http参数构建一个where条件
	 * @param param
	 * @return
	 */
	private Map<String, Object> paramToWhere(String key,String param,Map<String, Object> sqlMap){
		String sql = sqlMap.get(SQL).toString();
		String ps[] = StringUtils.split(param, XBIUtils.SEP);
		List<String> list =  (List<String>)sqlMap.get(SQL_P);
		if(ps.length > 1){
			String inStr = "";
			for(String p : ps){
				inStr += "?" + XBIUtils.SEP;
				list.add(p);
			}
			inStr = StringUtils.stripEnd(inStr, XBIUtils.SEP);
			sql += " and " + key + " in (" + inStr  + ") "; 
		}
		else if(ps.length == 1){
			sql += " and " + key + " = ? ";
			list.add(param);
		}
		sqlMap.put(SQL, sql);
		return sqlMap;
	}
}
