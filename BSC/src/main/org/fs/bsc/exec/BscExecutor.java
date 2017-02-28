package org.fs.bsc.exec;

import org.fs.bsc.component.BscComponent;
import org.fs.bsc.context.BscContext;

/**
 * BSC组件执行器
 * @author Jingxin.Gao
 *
 */
public interface BscExecutor {
	public String execute(BscComponent component, BscContext context);
}
