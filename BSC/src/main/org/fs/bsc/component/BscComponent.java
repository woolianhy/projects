package org.fs.bsc.component;

import java.util.List;

import org.fs.bsc.context.BscContext;
import org.fs.bsc.def.BscFieldDef;

/**
 * BSC组件
 * @author Jingxin.Gao
 *
 */
public interface BscComponent {
	public String getCode();
	public void setCode(String code);
	public String getName();
	public String getDesc();
	public List<BscFieldDef> getInputFields();
	public List<BscFieldDef> getOutputFields();
	
	public String execute(BscContext context);
}
