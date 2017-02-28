package org.fs.bsc.conf;

import org.fs.bsc.BscServiceProvider;
import org.fs.bsc.context.BscContextManager;
import org.fs.bsc.event.BscEventManager;
import org.fs.bsc.exec.BscExecuteManager;

/**
 * BSC配置
 * @author Jingxin.Gao
 *
 */
public interface BscConfiguration {
	public BscExecuteManager getBscExecuteManager();
	public BscContextManager getBscContextManager();
	public BscEventManager getBscEventManager();
	public void configure(BscServiceProvider provider);
}
