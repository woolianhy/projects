package org.fs.bsc.flow.editor.command;

import org.eclipse.gef.commands.Command;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowDirectionDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;

public class CreateDirectionCommand extends Command{
	
	private BscFlowAction action;
	
	private BscFlowDirectionDef direction;

	public BscFlowAction getAction() {
		return action;
	}

	public void setAction(BscFlowAction action) {
		this.action = action;
	}

	public BscFlowDirectionDef getDirection() {
		return direction;
	}

	public void setDirection(BscFlowDirectionDef direction) {
		this.direction = direction;
	}
	
	@Override
	public boolean canExecute() {
		// TODO Auto-generated method stub
		return super.canExecute();
	}
	
	@Override
	public void execute() {
		if(action instanceof BscFlowActionDef){
			((BscFlowActionDef) action).addDirection(direction);
		}else if(action instanceof BscFlowStartActionDef){
			((BscFlowStartActionDef) action).addDirection(direction);
		}
		BscFlowAction targetAction = direction.getTargetAction();
		if(targetAction instanceof BscFlowActionDef){
			((BscFlowActionDef) targetAction).addSourceDirection(direction);
		}else if(targetAction instanceof BscFlowEndActionDef){
			((BscFlowEndActionDef) targetAction).addSourceDirection(direction);
		}
	}
	
	@Override
	public void undo() {
		if(action instanceof BscFlowActionDef){
			((BscFlowActionDef) action).removeDirection(direction);
		}else if(action instanceof BscFlowStartActionDef){
			((BscFlowStartActionDef) action).removeDirection(direction);
		}
		BscFlowAction targetAction = direction.getTargetAction();
		if(targetAction instanceof BscFlowActionDef){
			((BscFlowActionDef) targetAction).removeSourceDirection(direction);
		}else if(targetAction instanceof BscFlowEndActionDef){
			((BscFlowEndActionDef) targetAction).removeSourceDirection(direction);
		}
	}
}
