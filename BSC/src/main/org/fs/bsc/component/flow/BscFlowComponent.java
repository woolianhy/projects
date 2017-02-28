package org.fs.bsc.component.flow;

import org.fs.bsc.component.AbstractBscComponent;
import org.fs.bsc.context.BscContext;

/**
 * BSC流程组件
 * @author Jingxin.Gao
 *
 */
public class BscFlowComponent extends AbstractBscComponent {
	
	private BscFlowResolver flowResolver;
	
	private BscFlow flow;
	
	@Override
	public String execute(BscContext context) {
		String result = null;
		result = flowResolver.executeFlow(this, context);
		return result;
	}

	public BscFlowResolver getFlowResolver() {
		return flowResolver;
	}

	public void setFlowResolver(BscFlowResolver flowResolver) {
		this.flowResolver = flowResolver;
	}

	public BscFlow getFlow() {
		return flow;
	}

	public void setFlow(BscFlow flow) {
		this.flow = flow;
	}
}
