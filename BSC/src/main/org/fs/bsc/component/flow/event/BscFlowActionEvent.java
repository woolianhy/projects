package org.fs.bsc.component.flow.event;

import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.event.BscEvent;

public class BscFlowActionEvent extends BscEvent {
	public static final String INIT = "init";
	public static final String START = "start";
	public static final String END = "end";
	public static final String EXCEPTION = "exception";
	public static final String DESTROY = "destroy";
	
	private BscFlowAction action;
	
	@Deprecated
	public BscFlowActionEvent() {
		super();
	}
	
	public BscFlowActionEvent(BscFlowAction action, String code) {
		super(code);
		this.action = action;
	}
	
	public BscFlowActionEvent(BscFlowAction action, String code, Object event) {
		super(code, event);
		this.action = action;
	}
	
	public BscFlowAction getAction() {
		return action;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{action:" + getAction() + ",code:" + getCode() + (getEvent() != null ? (",event:" + getEvent()) : "") + "}";
	}
}
