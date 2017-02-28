package org.fs.bsc.component.flow.event;

import org.fs.bsc.component.flow.BscFlowDirection;
import org.fs.bsc.event.BscEvent;

public class BscFlowDirectionEvent extends BscEvent {
	public static final String CHECK = "check";
	public static final String ACCEPT = "accept";
	private BscFlowDirection direction;
	
	@Deprecated
	public BscFlowDirectionEvent() {
		super();
	}
	
	public BscFlowDirectionEvent(BscFlowDirection direction, String code) {
		super(code);
		this.direction = direction;
	}
	
	public BscFlowDirectionEvent(BscFlowDirection direction, String code, Object event) {
		super(code, event);
		this.direction = direction;
	}

	public BscFlowDirection getDirection() {
		return direction;
	}
	
	public void setDirection(BscFlowDirection direction) {
		this.direction = direction;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{direction:" + getDirection() + ",code:" + getCode() + (getEvent() != null ? (",event:" + getEvent()) : "") + "}";
	}
}
