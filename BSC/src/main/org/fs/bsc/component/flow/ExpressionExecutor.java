package org.fs.bsc.component.flow;

import org.fs.bsc.context.BscContext;

public interface ExpressionExecutor {
	
	public boolean execute(String expression, BscContext context);
}
