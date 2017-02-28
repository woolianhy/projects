package org.fs.bsc.context;

import java.util.Map;

/**
 * BSC上下文
 * @author Jingxin.Gao
 *
 */
public interface BscContext extends Map<String, Object> {
	public void setExtParam(String key, Object value);
	public Object getExtParam(String key);
}
