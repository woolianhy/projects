package org.fs.bsc.context;

/**
 * BSC上下文管理器
 * @author Jingxin.Gao
 *
 */
public class BscContextManager {
	public BscContext newContext(){
		return new SimpleBscContext();
	}
}
