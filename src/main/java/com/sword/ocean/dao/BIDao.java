package com.sword.ocean.dao;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.sword.ocean.common.enumType.DataSourceEnum;
import com.sword.ocean.common.utils.DBUtil;



@Service
public class BIDao {

	
	@Resource
	private DBUtil db;
	
	
	/**
	 * 执行查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object> query(DataSourceEnum ds,String sql,Object...params){
		return db.query(ds,sql, params);
	}
	
	public List<Object> query(String sql,Object...params){
		return query(null,sql, params);
	}
	
	
}
