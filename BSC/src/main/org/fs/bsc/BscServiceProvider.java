package org.fs.bsc;

import org.fs.bsc.conf.BscConfiguration;
import org.fs.bsc.context.BscContextManager;
import org.fs.bsc.event.BscEventManager;
import org.fs.bsc.exec.BscExecuteManager;

/**
 * BSC 服务提供者
 * @author Jingxin.Gao
 *
 */
public class BscServiceProvider {
	
	private BscExecuteManager executeManager;
	private BscContextManager contextManager;
	private BscEventManager eventManager;
	
	public BscExecuteManager getBscExecuteManager(){
		return executeManager;
	}
	
	public BscContextManager getBscContextManager() {
		return contextManager;
	}
	
	public BscEventManager getEventManager() {
		return eventManager;
	}
	
	public void init(BscConfiguration config){
		config.configure(this);
		executeManager = config.getBscExecuteManager();
		contextManager = config.getBscContextManager();
		eventManager = config.getBscEventManager();
	}
}
