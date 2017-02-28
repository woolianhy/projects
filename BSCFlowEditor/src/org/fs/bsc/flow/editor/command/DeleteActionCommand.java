package org.fs.bsc.flow.editor.command;

import java.util.List;

import org.eclipse.gef.commands.Command;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.component.flow.BscFlowDirection;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowDef;
import org.fs.bsc.flow.model.BscFlowDirectionDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;

public class DeleteActionCommand extends Command {
	
	private BscFlowDef flow;
	
	private BscFlowAction action;
	
	private List<BscFlowDirection> directions;
	
	private List<BscFlowDirection> sourceDirections;
	
	public BscFlowDef getFlow() {
		return flow;
	}

	public void setFlow(BscFlowDef flow) {
		this.flow = flow;
	}

	public BscFlowAction getAction() {
		return action;
	}

	public void setAction(BscFlowAction action) {
		this.action = action;
		this.directions = action.getDirections();
		if(action instanceof BscFlowActionDef){
			this.sourceDirections = ((BscFlowActionDef) action).getSourceDirections();
		}else if(action instanceof BscFlowEndActionDef){
			this.sourceDirections = ((BscFlowEndActionDef) action).getSourceDirections();
		}
	}

	@Override
	public void execute() {
		flow.removeAction(action);
		if(sourceDirections != null){
			for(BscFlowDirection direction : sourceDirections){
				BscFlowAction sourceAction = ((BscFlowDirectionDef) direction).getSourceAction();
				if(sourceAction instanceof BscFlowActionDef){
					((BscFlowActionDef) sourceAction).removeDirection(direction);
				}else if(sourceAction instanceof BscFlowStartActionDef){
					((BscFlowStartActionDef) sourceAction).removeDirection(direction);
				}
			}
		}
		if(directions != null){
			for(BscFlowDirection direction : directions){
				BscFlowAction targetAction = ((BscFlowDirectionDef) direction).getTargetAction();
				if(targetAction instanceof BscFlowActionDef){
					((BscFlowActionDef) targetAction).removeSourceDirection(direction);
				}else if(targetAction instanceof BscFlowEndActionDef){
					((BscFlowEndActionDef) targetAction).removeSourceDirection(direction);
				}
			}
		}
	}
	
	@Override
	public void undo() {
		flow.addAction(action);
		if(sourceDirections != null){
			for(BscFlowDirection direction : sourceDirections){
				BscFlowAction sourceAction = ((BscFlowDirectionDef) direction).getSourceAction();
				if(sourceAction instanceof BscFlowActionDef){
					((BscFlowActionDef) sourceAction).addDirection(direction);
				}else if(sourceAction instanceof BscFlowStartActionDef){
					((BscFlowStartActionDef) sourceAction).addDirection(direction);
				}
			}
		}
		if(directions != null){
			for(BscFlowDirection direction : directions){
				BscFlowAction targetAction = ((BscFlowDirectionDef) direction).getTargetAction();
				if(targetAction instanceof BscFlowActionDef){
					((BscFlowActionDef) targetAction).addSourceDirection(direction);
				}else if(targetAction instanceof BscFlowEndActionDef){
					((BscFlowEndActionDef) targetAction).addSourceDirection(direction);
				}
			}
		}
	}
}
