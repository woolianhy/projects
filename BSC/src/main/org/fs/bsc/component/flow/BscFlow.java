package org.fs.bsc.component.flow;

import java.util.List;

/**
 * BSC流程
 * @author Jingxin.Gao
 *
 */
public class BscFlow {
	private String code;
	private String name;
	private String desc;
	private BscFlowStartAction startAction;
	private List<BscFlowAction> actions;
	private List<BscFlowEndAction> endActions;
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
	public BscFlowAction getStartAction() {
		return startAction;
	}
	public void setStartAction(BscFlowStartAction startAction) {
		this.startAction = startAction;
	}
	public List<BscFlowAction> getActions() {
		return actions;
	}
	public void setActions(List<BscFlowAction> actions) {
		this.actions = actions;
	}
	public List<BscFlowEndAction> getEndActions() {
		return endActions;
	}
	public void setEndActions(List<BscFlowEndAction> endActions) {
		this.endActions = endActions;
	}
	@Override
	public String toString() {
		return getClass().getSimpleName() + "[" + getCode() + "]";
	}
}
