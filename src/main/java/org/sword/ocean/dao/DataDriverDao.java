/**
 * 
 */
package org.sword.ocean.dao;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.sword.ocean.common.enumType.DataSourceEnum;
import org.sword.ocean.common.utils.DBUtil;
import org.sword.ocean.domain.ScriptParam;
import org.sword.ocean.domain.SqlScript;


/**
 * 数据驱动器访问
 * @author ChengNing
 * @date   2015年3月10日
 */
@Service
public class DataDriverDao {
	
	@Resource
	private DBUtil db;
	
	/**
	 * 根据名称得到对应执行的脚本
	 * @param name
	 * @return
	 */
	public SqlScript getScript(String name){
		return getScript(name, null);
	}
	
	/**
	 * 根据名称得到对应执行的脚本
	 * @param name
	 * @return
	 */
	public SqlScript getScript(String name,DataSourceEnum ds){
		String sql = "select a.ID,a.TYPE,a.NAME,a.SCRIPT,a.DESCRIPTION,a.FLAG from sys_script a where a.name=? ";//去掉limit 1 适应oracle
		List<Object> list = db.query(ds,sql, name);
		if(list.size() <= 0)
			return null;
		Map map = (Map)list.get(0);

		SqlScript script = mapToScript(map);
		String sqlParam = "select a.ID,a.SCRIPTID,a.NAME,a.CONTENT from sys_scriptparam a where a.scriptid=? ";
		List<Object> listParam = db.query(ds,sqlParam, script.getId());
		if(listParam != null && listParam.size() > 0){
			List<ScriptParam> params = new ArrayList<ScriptParam>();
			for (Iterator iterator = listParam.iterator(); iterator.hasNext();) {
	            Map paramMap = (Map) iterator.next();
	            ScriptParam param = mapToScriptParam(paramMap);
	            params.add(param);
            }
			script.setParams(params);
		}
		return script;
	}
	
	/**
	 * 查询结果映射到SqlScript对象
	 * @param map
	 * @return
	 */
	public SqlScript mapToScript(Map map){
		SqlScript script = new SqlScript();
		script.setId(Long.parseLong(map.get("ID").toString()));
		script.setName(ObjectUtils.toString(map.get("NAME"), ""));
		script.setScript(ObjectUtils.toString(map.get("SCRIPT"), ""));
		script.setType(Long.parseLong(map.get("TYPE").toString()));
		script.setDescription(ObjectUtils.toString(map.get("DESCRIPTION"), ""));
		script.setFlag(ObjectUtils.toString(map.get("FLAG"), ""));
		
		return script;
	}
	
	/**
	 * 查询结果映射到ScriptParam对象
	 * @param map
	 * @return
	 */
	public ScriptParam mapToScriptParam(Map map){
		ScriptParam scriptParam = new ScriptParam();
		scriptParam.setId(Long.parseLong(map.get("ID").toString()));
		scriptParam.setName(ObjectUtils.toString(map.get("NAME"), ""));
		scriptParam.setScriptId(Long.parseLong(map.get("SCRIPTID").toString()));
		scriptParam.setContent(ObjectUtils.toString(map.get("CONTENT"), ""));
		return scriptParam;
	}
	
	/**
	 * 执行查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object> query(DataSourceEnum ds,String sql,Object...params){
		return db.query(ds,sql, params);
	}
	
	/**
	 * 执行查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object> query(DataSourceEnum ds,String sql,boolean order,Object...params){
		return db.query(ds,sql,order, params);
	}
	
	/**
	 * 执行查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object> queryProcedure(DataSourceEnum ds,String procedure,Object...params){
		return db.executeSP(ds,procedure,params);
	} 
	/**
	 * 执行查询
	 * @param sql
	 * @param params
	 * @return
	 */
	public List<Object> queryProcedure(DataSourceEnum ds,String procedure,boolean order,Object...params){
		return db.executeSP(ds,procedure,order,params);
	} 
	/**
	 * 一个值的查询
	 * @param sql
	 * @return
	 */
	public String getOneValue(String sql){
		return db.getOneValue(sql);
	}

}
