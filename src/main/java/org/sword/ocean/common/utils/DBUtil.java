package org.sword.ocean.common.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sword.ocean.common.enumType.DataSourceEnum;



/**
 * 数据库工具， 提供常用的数据库操作
 * @author ChengNing
 * @version 1.0
 */
@Service
public class DBUtil {
	
	private static Logger logger = Logger.getLogger(DBUtil.class);

	@Autowired
	private AppConfig appConfig;
	@Autowired
	private DBAdapter adapter;

	
	/**
	 * 获取数据库连接
	 * @throws SQLException 
	 */
	private Connection getConnection(DataSourceEnum ds) throws SQLException {
		// 数据库连接
		Connection conn = adapter.getConnection(ds);
		return conn;
	}
	/**
	 * 查询执行方法
	 * @param sql
	 * @param params
	 * @return
	 */
	private List<Object> executeQuery(DataSourceEnum ds,String sql,Object...params){
		return executeQuery(ds,sql,false,params);
	}
	/**
	 * 查询执行方法
	 * @param sql
	 * @param params
	 * @return
	 */
	private List<Object> executeQuery(DataSourceEnum ds,String sql,Boolean order,Object...params){
		Connection conn = null;
		PreparedStatement stmt=null;
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		if(appConfig.getShowSQL()) {
			logSql(sql,params);
		}
		try {
			conn = getConnection(ds);
			stmt = conn.prepareStatement(sql);
			if(params !=null){
				for(int i=0;i< params.length;i++){
					stmt.setObject(i+1, params[i]);
				}
			}
			rs = stmt.executeQuery();
			if(order){
				list = getResultByList(rs);
			}
			else{
				list = getResultByMap(rs);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			close(conn,stmt,rs,null);
		}
		return list;
	}
	
	/**
	 * 使用MAP接收数据，有键值无序状态
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private static List<Object> getResultByMap(ResultSet rs) throws SQLException{
		List<Object> list = new ArrayList<Object>();
		ResultSetMetaData rsmData = rs.getMetaData();
		while(rs.next()){
			Map<Object, Object> map = new HashMap<Object, Object>();
			for (int i = 1; i <= rsmData.getColumnCount(); i++) {
				map.put(rsmData.getColumnName(i), rs.getObject(i));
			}
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 使用list接收数据，第一行保存列名称，之后开始是数据行，有序
	 * @param rs
	 * @return
	 * @throws SQLException
	 */
	private List<Object> getResultByList(ResultSet rs) throws SQLException{
		List<Object> list = new ArrayList<Object>();
		ResultSetMetaData rsmData = rs.getMetaData();
		//第一行为列名称数组
		List<Object> metaRow = new ArrayList<Object>();		
		for (int i = 1; i <= rsmData.getColumnCount(); i++) {
			metaRow.add(rsmData.getColumnName(i));
		}
		list.add(metaRow);
		//接收数据
		while(rs.next()){
			List<Object> rowList = new ArrayList<Object>();		
			for (int i = 1; i <= rsmData.getColumnCount(); i++) {
				rowList.add(rs.getObject(i));
			}
			list.add(rowList);
		}
		return list;
	}
	
	/**
	 * 数据库更新（增删改）
	 * @param sql 带参数的SQL语句
	 * @param params SQL语句参数
	 * @return int 更新操作影响行数
	 */
	private int executeUpdate(DataSourceEnum ds,String sql, Object...params) {
		Connection conn = null;
		PreparedStatement stmt=null;
		ResultSet rs = null;
		// 更新操作影响行数
		int count = 0;
		if(appConfig.getShowSQL()) logger.info(sql);
		
		try {
			// 获取连接
			conn = getConnection(ds);
			// 预编译带参数的sql语句
			stmt = conn.prepareStatement(sql);
			// 设置sql语句参数
			if(params !=null){
				for (int i = 0; i < params.length; i++) {
					stmt.setObject(i + 1, params[i]);
				}
			}
			// 执行sql语句
			count = stmt.executeUpdate();		

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn,stmt,rs,null);
		}
		return count;
	}
	
	/**
	 * 销毁所有数据库连接资源
	 */
	private void close(Connection conn,Statement stmt,ResultSet rs,CallableStatement cstmt){
		// 关闭结果集
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 关闭PreparedStatement
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 关闭CallableStatement
		if(cstmt != null){
			try {
				cstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		// 关闭数据库连接
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	public String getOneValue(String sql){
		return getOneValue(sql, null);
	}
	/**
	 * 得到一个值的查询
	 * @param sql
	 * @return
	 */
	public String getOneValue(String sql,Object...params){
		return getOneValue(null,sql, null);
	}
	/**
	 * 得到一个值的查询
	 * @param sql
	 * @return
	 */
	public String getOneValue(DataSourceEnum ds,String sql,Object...params){
		String re = "";
		List<Object> list = query(ds,sql,params);
		if(list != null && list.size() > 0){

			Map<Object, Object> map = (HashMap<Object, Object>)list.get(0);
			Object key = map.keySet().toArray()[0];
			re = map.get(key).toString();
			return re;
		}
		return "";
	}
	
	/**
	 * 数据库查询
	 * @param sql 不带参数的SQL语句
	 * @return List 查询结果
	 */
	public List<Object> query(String sql) {
        return query(null,sql,null);
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object> query(String sql,Object...params) {
        return executeQuery(null,sql,params);
	}
	
	/**
	 * 数据库查询
	 * @param sql 不带参数的SQL语句
	 * @return List 查询结果
	 */
	public List<Object> query(DataSourceEnum ds,String sql) {
        return query(ds,sql,null);
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object> query(DataSourceEnum ds,String sql,Object...params) {
        return executeQuery(ds,sql,params);
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object> query(DataSourceEnum ds,String sql,boolean order,Object...params) {
        return executeQuery(ds,sql,order,params);
	}
	
	/**
	 * 
	 * @param sql
	 * @return
	 */
	public int execute(String sql){
		return execute(sql, null);
	}
	
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int execute(String sql,Object...params){
		return executeUpdate(null,sql, params);
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int execute(DataSourceEnum ds,String sql){
		return executeUpdate(ds,sql, null);
	}
	
	/**
	 * 
	 * @param sql
	 * @param params
	 * @return
	 */
	public int execute(DataSourceEnum ds,String sql,Object...params){
		return executeUpdate(ds,sql, params);
	}
	
	/**
	 * 批处理执行sql
	 * @param sqlArr  多个sql脚本的数组
	 * @return
	 */
	public int[] executeBatch(List<String> sqlArr){
		return executeBatch(null,sqlArr);
	}

	/**
	 * 批处理执行sql
	 * @param ds      数据源
	 * @param sqlArr  多个sql脚本的数组
	 * @return
	 */
	public int[] executeBatch(DataSourceEnum ds,List<String> sqlArr){
		if(sqlArr == null)
			return null;
		Connection conn = null;
		Statement  stmt=null;
		ResultSet rs = null;
		// 更新操作影响行数
		int[] count = {};
		try {
			// 获取连接
			conn = getConnection(ds);
			stmt =  conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);  
			conn.setAutoCommit(false); 
			// 预编译带参数的sql语句
			for (String sql : sqlArr) {
				if(appConfig.getShowSQL()) logger.info(sql);
                stmt.addBatch(sql);    
            }
			// 执行sql语句
			count = stmt.executeBatch();
			conn.commit();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn,stmt,rs,null);
		}
		return count;
	}
	
	/**
	 * 批处理执行sql
	 * @param ds         数据源
	 * @param sql        插入或者删除的sql
	 * @param paramList  sql参数的list，list中的每个item是一个顺序的参数数组
	 * @return  
	 */
	public int[] executeBatch(DataSourceEnum ds,String sql,List<Object[]> paramList){
		if(paramList == null)
			return null;
		Connection conn = null;
		PreparedStatement  pstmt=null;
		ResultSet rs = null;
		// 更新操作影响行数
		int[] count = {};
		try {
			// 获取连接
			conn = getConnection(ds);
			pstmt =  conn.prepareStatement(sql);
			conn.setAutoCommit(false); 
			// 预编译带参数的sql语句
			for (Object[] params : paramList) {
				for (int i = 0; i < params.length; i++) {
					pstmt.setObject(i + 1, params[i]);
				}
				pstmt.addBatch();    
            }
			// 执行sql语句
			count = pstmt.executeBatch();
			conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			close(conn,pstmt,rs,null);
		}
		return count;
	}
	
	/**
	 * 执行存储过程,不支持out参数
	 * @param spName
	 * @return
	 */
	public List<Object> executeSP(String spName,Object...params){
		return executeSP(null, spName, params);
	}
	/**
	 * 执行存储过程,不支持out参数
	 * @param ds
	 * @param spName
	 * @param params
	 * @return
	 */
	public List<Object> executeSP(DataSourceEnum ds,String spName,Object...params){
		return executeSP(ds,spName,false,params);
	}
	/**
	 * 执行存储过程,不支持out参数
	 * @param spName
	 * @return
	 */
	public List<Object> executeSP(DataSourceEnum ds,String spName,Boolean order,Object...params){
		Connection conn = null;
		CallableStatement cstmt = null;
		ResultSet rs = null;
		List<Object> list = new ArrayList<Object>();
		String proStr = "{call " + spName + "}";
		if(appConfig.getShowSQL()){
			logSql(spName,params);
		} 
		try {
			conn = getConnection(ds);
			cstmt = conn.prepareCall(proStr);
			if(params !=null){
				for(int i=0;i< params.length;i++){
					cstmt.setObject(i+1, params[i]);
				}
			}
			cstmt.execute();
			rs = cstmt.getResultSet();
			if(order){
				list = getResultByList(rs);
			}
			else{
				list = getResultByMap(rs);
			}
		} catch (Exception e) {
			logEx(e);
			e.printStackTrace();
		}
		finally{
			close(conn,null,rs,cstmt);
		}
		return list;
	}
	
	/**
	 * log sql
	 * @param sql
	 * @param params
	 */
	private void logSql(String sql,Object...params){
		String thread = Thread.currentThread().getName();
		String ps = "";
		if(params != null){
			for (Object p : params) {
	            ps+=p.toString() + ",";
	        }
		}
		logger.info("线程:==" + thread + "===" + sql + "\n" + ps);
	}
	
	/**
	 * log exception
	 * @param e
	 */
	private void logEx(Exception e){
		String thread = Thread.currentThread().getName();
		logger.error("线程:==" + thread + "====" + e.getClass().getName() + ":" + e.getMessage());
	}
	
}
