package org.fs.bsc.exec;

import org.fs.bsc.component.BscComponent;
import org.fs.bsc.context.BscContext;

/**
 * BSC执行管理器
 * @author Jingxin.Gao
 *
 */
public interface BscExecuteManager {
	public String execute(String componentCode, BscContext context);
	public BscComponent getComponent(String componentCode);
	public boolean checkCondition(String expression, BscContext context);
}
