package org.fs.bsc.component.flow;

/**
 * BSC流程导向
 * @author Jingxin.Gao
 *
 */
public class BscFlowDirection {
	private String actionCode;
	private String expression;
	private String desc;
	public String getActionCode() {
		return actionCode;
	}
	public void setActionCode(String actionCode) {
		this.actionCode = actionCode;
	}
	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[actionCode=" + actionCode + ",expression=" + expression + "]";
	}
}
