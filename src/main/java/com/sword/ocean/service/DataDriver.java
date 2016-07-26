/**
 * 
 */
package com.sword.ocean.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.sword.ocean.common.enumType.DataSourceEnum;
import com.sword.ocean.dao.DataDriverDao;
import com.sword.ocean.domain.ScriptParam;
import com.sword.ocean.domain.SqlScript;


/**
 * 数据驱动器
 * 针对数据驱动型服务不作任何逻辑处理，连接前端和数据库端，构建快速响应模型
 * 对于数据分析类需求，使用此驱动器加view或者过程进行快速开发
 * @author ChengNing
 * @date   2015年3月10日
 */
@Service
public class DataDriver {
	private static Logger logger = Logger.getLogger(DataDriver.class);
	
	public static final String SCRIPTNAME = "type";   //脚本名称的前端参数
	public static final String MUTI_STR = "...";      //in查询中的多个参数的写法符号
	public static final String MUTI_SEP = ",";        //多个参数的分隔符 get过个的多个参数分隔符
	public static final String IN_CHAR = "?";         //参数符号
	public static final String BLANK_SPACE = " ";     //空格
	
	@Resource
	private DataDriverDao dataDriverDao;
	private boolean order;

	
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
	public List<Object> query(Map paramMap,boolean order){
		return query(paramMap,null,order);
	}	
	
	/**
	 * 根据外部输入的参数进行对应脚本查询
	 * @param paramMap   前端传入的参数map
	 * @return
	 */
	public List<Object> query(Map paramMap,DataSourceEnum ds){
		boolean order = false;
		return query(paramMap, ds, order);
	}
	
	/**
	 * 根据外部输入的参数进行对应脚本查询
	 * @param paramMap   前端传入的参数map
	 * @param ds         需要访问的数据源
	 * @param order      是否有序，有序，则使用list传出，属于value list，无序则每一行用map表示，属于key-value
	 * @return
	 */
	public List<Object> query(Map paramMap,DataSourceEnum ds,boolean order){
		try {
			this.order = order;
			String scriptName = paramMap.get(SCRIPTNAME).toString();
			if(StringUtils.isBlank(scriptName))
				return null;
			paramMap.remove(SCRIPTNAME);//去掉第一个参数
			SqlScript sqlScript = dataDriverDao.getScript(scriptName,ds);
			if(sqlScript == null)
				return null;
			if(sqlScript.getType() == 1){
				return queryBySql(paramMap,sqlScript,ds);
			}else if(sqlScript.getType() == 2){
				return queryByProcedure(paramMap,sqlScript,ds);
			}
			logger.info("数据执行脚本类型不正确type:" + sqlScript.getType() + ",数据id：" + sqlScript.getId());
        } catch (Exception e) {
        	logger.error("脚本执行异常:" + e.getMessage());
        }
		return null;
	}
	
	/**
	 * 执行SQL脚本
	 * @param paramMap
	 * @return
	 */
	private List<Object> queryBySql(Map paramMap,SqlScript sqlScript,DataSourceEnum ds){
		//得到脚本
		String sql = sqlScript.getScript();
		StringBuilder sqlBuilder = new StringBuilder(sql);
		List<Object> sqlParamList = new ArrayList<Object>();
		//脚本中带有？的参数
		if(sqlScript.getFlag().equals("1")){
			sqlParamList = createParamList(paramMap, sqlScript);
		}
		else{//参数中带有脚本
			//根据参数组建脚本和脚本对应的参数，注意顺序
			List<ScriptParam> sqlParams = new ArrayList<ScriptParam>(); 
			sqlParams = sqlScript.getParams();
			if(sqlParams != null && sqlParams.size() > 0){
				for (Iterator iterator = sqlParams.iterator(); iterator.hasNext();) {
					ScriptParam param = (ScriptParam) iterator.next();
			        String scriptParaName = param.getName();
			        if(paramMap.containsKey(scriptParaName) && !scriptParaName.equals(SCRIPTNAME)){
			        	String paramContent = param.getContent();
			        	//多参数处理 in(...)
			        	if(paramContent.contains(MUTI_STR)){
			        		String[] mutiParam = paramMap.get(scriptParaName).toString().split(MUTI_SEP);
		//	        		mutiParamBuilder(mutiParam,paramContent,sqlParamList,sqlBuilder);
			        		StringBuilder sb = new StringBuilder();
			        		//根据参数个数设置?的个数和对应参数
			        		for (int i = 0; i < mutiParam.length; i++) {
			                	sqlParamList.add(mutiParam[i]);
			                    sb.append("?,");
			                }
			        		String replacement = StringUtils.strip(sb.toString(), ",");
			        		String mutiStr = StringUtils.replace(paramContent, MUTI_STR, replacement);
			            	sqlBuilder.append(BLANK_SPACE + mutiStr);
			        	}
			        	else{
			            	sqlParamList.add(paramMap.get(scriptParaName));
			            	sqlBuilder.append(BLANK_SPACE + paramContent); //添加空格，防止配置表中没有空格导致条件和脚本贴身
			        	}
			        }
		        }
			}
		}
		

		List<Object> resultMap = dataDriverDao.query(ds,sqlBuilder.toString(),this.order, sqlParamList.toArray());
		return resultMap;
	}
	
	/**
	 * 执行存储过程
	 * @param paramMap
	 * @return
	 */
	private List<Object> queryByProcedure(Map paramMap,SqlScript sqlScript,DataSourceEnum ds){
		String sql = sqlScript.getScript();
		StringBuilder sqlBuilder = new StringBuilder(sql);
		List<Object> sqlParamList = createParamList(paramMap, sqlScript);
		
		List<Object> resultMap = dataDriverDao.queryProcedure(ds,sqlBuilder.toString(),this.order, sqlParamList.toArray());
		return resultMap;
	}
	
	/**
	 * 使用参数对应脚本中的参数标识?
	 * @param paramMap
	 * @param sqlScript
	 * @return
	 */
	private List<Object> createParamList(Map paramMap,SqlScript sqlScript){
		List<Object> sqlParamList = new ArrayList<Object>();
		//根据参数组建脚本和脚本对应的参数，注意顺序
		List<ScriptParam> sqlParams = sqlScript.getParams();
		if(paramMap.size()>0 && (sqlParams == null || sqlParams.size() <= 0))
			return null;
		//超过1个数据则进行排序
		if(sqlParams != null && sqlParams.size() > 0){
			sqlParams = sortProcedureParam(sqlParams);
			for (Iterator iterator = sqlParams.iterator(); iterator.hasNext();) {
				ScriptParam param = (ScriptParam) iterator.next();
		        String scriptParaName = param.getName();
		        if(paramMap.containsKey(scriptParaName) && !scriptParaName.equals(SCRIPTNAME)){
		        	sqlParamList.add(paramMap.get(scriptParaName));
		        }
		        else{
		        	logger.error("存储过程参数传入个数不正确" + scriptParaName + "参数没有传入");
		        	return null;
		        }
	        }
		}
		return sqlParamList;
	}
	
	/**
	 * 存储过程参数表中的参数进行排序
	 * @param sqlParams
	 * @return
	 */
	private List<ScriptParam> sortProcedureParam(List<ScriptParam> sqlParams){
		Collections.sort(sqlParams,new Comparator<ScriptParam>() {
			public int compare(ScriptParam arg0,ScriptParam arg1){
				try {
					int a = Integer.parseInt(arg0.getContent());
					int b = Integer.parseInt(arg1.getContent());
					return a - b;
                } catch (Exception e) {
                	logger.error("ScriptParam表中的content配置的不是存储过程参数的顺序号，类型转换错误" + e.getMessage());
                	e.printStackTrace();
                }
				return 0;
			}
		});
		return sqlParams;
	}
	
	
}
