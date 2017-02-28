package org.fs.bsc.flow.editor.command;

import org.eclipse.gef.commands.Command;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowDirectionDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;

public class DeleteDirectionCommand extends Command {
	
	private BscFlowDirectionDef direction;
	
	public BscFlowDirectionDef getDirection() {
		return direction;
	}

	public void setDirection(BscFlowDirectionDef direction) {
		this.direction = direction;
	}

	@Override
	public void execute() {
		BscFlowAction sourceAction = direction.getSourceAction();
		BscFlowAction targetAction = direction.getTargetAction();
		if(sourceAction instanceof BscFlowActionDef){
			((BscFlowActionDef) sourceAction).removeDirection(direction);
		}else if(sourceAction instanceof BscFlowStartActionDef){
			((BscFlowStartActionDef) sourceAction).removeDirection(direction);
		}
		
		if(targetAction instanceof BscFlowActionDef){
			((BscFlowActionDef) targetAction).removeSourceDirection(direction);
		}else if(targetAction instanceof BscFlowEndActionDef){
			((BscFlowEndActionDef) targetAction).removeSourceDirection(direction);
		}
	}
	
	@Override
	public void undo() {
		BscFlowAction sourceAction = direction.getSourceAction();
		BscFlowAction targetAction = direction.getTargetAction();
		if(sourceAction instanceof BscFlowActionDef){
			((BscFlowActionDef) sourceAction).addDirection(direction);
		}else if(sourceAction instanceof BscFlowStartActionDef){
			((BscFlowStartActionDef) sourceAction).addDirection(direction);
		}
		
		if(targetAction instanceof BscFlowActionDef){
			((BscFlowActionDef) targetAction).addSourceDirection(direction);
		}else if(targetAction instanceof BscFlowEndActionDef){
			((BscFlowEndActionDef) targetAction).addSourceDirection(direction);
		}
	}
}
