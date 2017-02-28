package org.fs.bsc.flow.editor.command;

import java.util.UUID;

import org.eclipse.gef.commands.Command;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.model.BscFlowDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;

public class CreateActionCommand extends Command{
	
	protected BscFlowAction action;
	
	protected BscFlowDef flow;
	
	@Override
	public void execute() {
		String prefix = action.getComponentCode();
		if(action instanceof BscFlowStartActionDef){
			prefix = "start";
		}else if(action instanceof BscFlowEndActionDef){
			prefix = "end";
		}
		action.setCode(prefix + "." + UUID.randomUUID().toString());
		flow.addAction(action);
	}
	
	@Override
	public String getLabel() {
		return "add action";
	}
	
	@Override
	public void redo() {
		execute();
	}
	
	@Override
	public void undo() {
		flow.removeAction(action);
	}

	public BscFlowAction getAction() {
		return action;
	}

	public void setAction(BscFlowAction action) {
		this.action = action;
	}

	public BscFlowDef getFlow() {
		return flow;
	}

	public void setFlow(BscFlowDef flow) {
		this.flow = flow;
	}
	
}
