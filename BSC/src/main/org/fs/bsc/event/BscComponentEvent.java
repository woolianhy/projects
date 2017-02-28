package org.fs.bsc.event;

import org.fs.bsc.component.BscComponent;

public class BscComponentEvent extends BscEvent {
	public static final String INIT = "init";
	public static final String START = "start";
	public static final String FORWARD = "forward";
	public static final String END = "end";
	public static final String EXCEPTION = "exception";
	public static final String DESTROY = "destroy";
	
	public static final String EVENT_END_PARAM_STARTTIME = "startTime";
	public static final String EVENT_END_PARAM_ENDTIME = "endTime";
	public static final String EVENT_END_PARAM_RESULT = "result";
	
	private BscComponent component;
	
	public BscComponentEvent() {
		super();
	}
	
	public BscComponentEvent(String code) {
		super(code);
	}
	
	public BscComponentEvent(String code, Object event) {
		super(code, event);
	}
	
	public BscComponentEvent(BscComponent component, String code, Object event) {
		super(code, event);
		this.component = component;
	}
	
	public BscComponent getComponent() {
		return component;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{component:" + getComponent() + ",code:" + getCode() + (getEvent() != null ? (",event:" + getEvent()) : "") + "}";
	}
}
