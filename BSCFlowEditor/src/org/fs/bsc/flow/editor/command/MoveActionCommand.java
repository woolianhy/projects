package org.fs.bsc.flow.editor.command;

import org.eclipse.gef.commands.Command;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;
import org.fs.bsc.flow.model.DisplayDef;

public class MoveActionCommand extends Command{
	protected BscFlowAction action;
	
	protected DisplayDef display;
	
	protected DisplayDef oldDisplay;

	public BscFlowAction getAction() {
		return action;
	}

	public void setAction(BscFlowAction action) {
		this.action = action;
	}
	
	public DisplayDef getDisplay() {
		return display;
	}

	public void setDisplay(DisplayDef display) {
		this.display = display;
	}

	@Override
	public void execute() {
		if(action instanceof BscFlowActionDef){
			oldDisplay = ((BscFlowActionDef) action).getDisplay();
			((BscFlowActionDef) action).setDisplay(display);
			((BscFlowActionDef) action).notifyChange(null, null);
		}else if(action instanceof BscFlowStartActionDef){
			oldDisplay = ((BscFlowStartActionDef) action).getDisplay();
			((BscFlowStartActionDef) action).setDisplay(display);
			((BscFlowStartActionDef) action).notifyChange(null, null);
		}else if(action instanceof BscFlowEndActionDef){
			oldDisplay = ((BscFlowEndActionDef) action).getDisplay();
			((BscFlowEndActionDef) action).setDisplay(display);
			((BscFlowEndActionDef) action).notifyChange(null, null);
		}
	}
	
	@Override
	public void undo() {
		if(action instanceof BscFlowActionDef){
			((BscFlowActionDef) action).setDisplay(oldDisplay);
			((BscFlowActionDef) action).notifyChange(null, null);
		}else if(action instanceof BscFlowStartActionDef){
			((BscFlowStartActionDef) action).setDisplay(oldDisplay);
			((BscFlowStartActionDef) action).notifyChange(null, null);
		}else if(action instanceof BscFlowEndActionDef){
			((BscFlowEndActionDef) action).setDisplay(oldDisplay);
			((BscFlowEndActionDef) action).notifyChange(null, null);
		}
	}
}
