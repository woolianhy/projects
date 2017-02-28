package org.fs.bsc.component.flow.event;

import org.fs.bsc.component.flow.BscFlow;
import org.fs.bsc.event.BscEvent;

public class BscFlowEvent extends BscEvent {
	
	private BscFlow flow;
	
	@Deprecated
	public BscFlowEvent() {
		super();
	}
	
	public BscFlowEvent(BscFlow flow, String code) {
		super(code);
		this.flow = flow;
	}
	
	
	public BscFlowEvent(BscFlow flow, String code, Object event) {
		super(code, event);
		this.flow = flow;
	}

	public BscFlow getFlow() {
		return flow;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{flow:" + getFlow() + ",code:" + getCode() + (getEvent() != null ? (",event:" + getEvent()) : "") + "}";
	}

	public static final String INIT = "init";
	public static final String START = "start";
	public static final String FORWARD = "forward";
	public static final String END = "end";
	public static final String EXCEPTION = "exception";
	public static final String DESTROY = "destroy";
}
