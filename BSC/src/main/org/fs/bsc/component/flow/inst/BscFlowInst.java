package org.fs.bsc.component.flow.inst;

import org.fs.bsc.component.flow.BscFlow;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.component.flow.BscFlowResolver;
import org.fs.bsc.context.BscContext;

public class BscFlowInst {
	private BscFlow flow;
	private BscContext context;
	private BscFlowAction currentAction;
	private BscFlowResolver flowResolver;
	public String init(){
		//TODO flow init
		return null;
	}
	
	public String forward(BscFlowAction action){
		currentAction = action;
		return flowResolver.executeAction(flow, this, currentAction, context);
	}
	
	public String destroy(){
		//TODO flow destroy
		return null;
	}

	public BscFlow getFlow() {
		return flow;
	}

	public void setFlow(BscFlow flow) {
		this.flow = flow;
	}

	public BscContext getContext() {
		return context;
	}

	public void setContext(BscContext context) {
		this.context = context;
	}

	public BscFlowAction getCurrentAction() {
		return currentAction;
	}

	public BscFlowResolver getFlowResolver() {
		return flowResolver;
	}

	public void setFlowResolver(BscFlowResolver flowResolver) {
		this.flowResolver = flowResolver;
	}
}
