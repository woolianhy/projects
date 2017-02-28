package org.fs.bsc.context;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleBscContext extends LinkedHashMap<String, Object> implements BscContext {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4894094014025318746L;
	
	private Map<String, Object> extParams = new HashMap<String, Object>();

	@Override
	public void setExtParam(String key, Object value) {
		extParams.put(key, value);
	}

	@Override
	public Object getExtParam(String key) {
		return extParams.get(key);
	}

	
}
