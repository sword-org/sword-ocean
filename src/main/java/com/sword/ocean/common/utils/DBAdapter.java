/**
 * 
 */
package com.sword.ocean.common.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.sword.ocean.common.enumType.DataSourceEnum;




/**
 * DB适配器
 * @author ChengNing
 * @date   2015年3月23日
 */
@Service
public class DBAdapter {
    
	@Resource
    private DataSource dataSource;//
	@Resource
	private DataSource oceanDataSource;
	@Resource
	private JdbcTemplate jdbcTemplate;
    
	public DBAdapter() {
	}

	public Connection getConnection(DataSourceEnum ds) throws SQLException {
//		this.datasource = jdbcTemplate.getDataSource();
		Connection conn = null;
		if(ds == null)
			return this.dataSource.getConnection();
		switch (ds) {
			case OCEAN:{
				conn=this.oceanDataSource.getConnection();
				break;
			}
			default:
				conn=this.dataSource.getConnection();;
				break;
		}
		return conn;
	}

}
