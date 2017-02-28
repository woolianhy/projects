package org.fs.bsc.component;

import java.util.List;

import org.fs.bsc.def.BscFieldDef;

public abstract class AbstractBscComponent implements BscComponent {
	public static final String RESULT_SUCCESS = "success"; 
	public static final String RESULT_FAIL = "fail";
	public static final String RESULT_EXCEPTION = "exception";
	protected String code;
	protected String name;
	protected String desc;
	protected List<BscFieldDef> inputFields;
	protected List<BscFieldDef> outputFields;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public List<BscFieldDef> getInputFields() {
		return inputFields;
	}
	public void setInputFields(List<BscFieldDef> inputFields) {
		this.inputFields = inputFields;
	}
	public List<BscFieldDef> getOutputFields() {
		return outputFields;
	}
	public void setOutputFields(List<BscFieldDef> outputFields) {
		this.outputFields = outputFields;
	}
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getCode() + "]";
	}
}
