package org.fs.bsc.event;

public class BscEvent {
	private String code;
	
	private Object event;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public Object getEvent() {
		return event;
	}
	
	public void setEvent(Object event) {
		this.event = event;
	}
	
	public BscEvent() {
	}

	public BscEvent(String code) {
		this.code = code;
	}

	public BscEvent(String code, Object event) {
		super();
		this.code = code;
		this.event = event;
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName() + "{code:" + code + (event != null ? (",event:" + event) : "") + "}";
	}
}
