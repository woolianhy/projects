package org.fs.bsc.exec;

import org.fs.bsc.component.BscComponent;
import org.fs.bsc.component.flow.ExpressionExecutor;
import org.fs.bsc.context.BscContext;
import org.fs.bsc.exception.BscException;

public class DefaultBscExecuteManager implements BscExecuteManager {
	
	private BscComponentProvider componentProvider;
	
	private BscExecutor executor;
	
	private ExpressionExecutor expressionExecutor;
	
	public void setComponentProvider(BscComponentProvider componentProvider) {
		this.componentProvider = componentProvider;
	}
	
	public BscComponentProvider getComponentProvider() {
		return componentProvider;
	}
	
	public BscExecutor getExecutor() {
		return executor;
	}

	public void setExecutor(BscExecutor executor) {
		this.executor = executor;
	}

	public ExpressionExecutor getExpressionExecutor() {
		return expressionExecutor;
	}

	public void setExpressionExecutor(ExpressionExecutor expressionExecutor) {
		this.expressionExecutor = expressionExecutor;
	}

	@Override
	public String execute(String componentCode, BscContext context){
		BscComponent component = componentProvider.getComponent(componentCode);
		if(null == component){
			throw new RuntimeException("Can not found component [" + componentCode + "] !");
		}
		String result = executor.execute(component, context);
		return result;
	}
	
	@Override
	public boolean checkCondition(String expression, BscContext context) {
		if(expression == null || "".equals(expression.trim())){
			return true;
		}
		try {
			return expressionExecutor.execute(expression, context);
		} catch (Exception e) {
			throw new BscException("流程条件计算异常", e);
		}
	}
	
	@Override
	public BscComponent getComponent(String componentCode) {
		return componentProvider.getComponent(componentCode);
	}
}
