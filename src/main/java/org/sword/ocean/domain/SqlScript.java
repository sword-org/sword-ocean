/**
 * 
 */
package org.sword.ocean.domain;

import java.util.List;

/**
 * @author ChengNing
 * @date   2015年3月25日
 */
public class SqlScript {
	private long id;
	private long type;
	private String name;
	private String script;
	private String description;
	private String flag;
	private List<ScriptParam> params;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getType() {
		return type;
	}
	public void setType(long type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public List<ScriptParam> getParams() {
		return params;
	}
	public void setParams(List<ScriptParam> params) {
		this.params = params;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	
	
}
