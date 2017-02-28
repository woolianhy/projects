package org.fs.bsc.flow.editor.policy;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.CreateRequest;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.editor.command.CreateActionCommand;
import org.fs.bsc.flow.editor.command.MoveActionCommand;
import org.fs.bsc.flow.editor.part.ActionEditPart;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;
import org.fs.bsc.flow.model.DisplayDef;

public class FlowEditPartPolicy extends XYLayoutEditPolicy {
	
	@Override
	protected Command createAddCommand(EditPart child, Object constraint) {
		return null;
	}
	
	@Override
	protected Command getDeleteDependantCommand(Request request) {
		return null;
	}
	
	protected Command createChangeConstraintCommand(EditPart child,
			Object constraint) {
		if(constraint instanceof Rectangle && child instanceof ActionEditPart){
			BscFlowAction action = (BscFlowAction) child.getModel();
			DisplayDef oldDisplay = null;
			if(action instanceof BscFlowActionDef){
				oldDisplay = ((BscFlowActionDef) action).getDisplay();
			}else if(action instanceof BscFlowStartActionDef){
				oldDisplay = ((BscFlowStartActionDef) action).getDisplay();
			}else if(action instanceof BscFlowEndActionDef){
				oldDisplay = ((BscFlowEndActionDef) action).getDisplay();
			}
			Rectangle r = (Rectangle) constraint;
			DisplayDef display = new DisplayDef();
			if(oldDisplay != null){
//				display.setWidth(oldDisplay.getWidth());
//				display.setHeight(oldDisplay.getHeight());
				display.setWidth(r.width());
				display.setHeight(r.height());
			}else{
				display.setWidth(100);
				display.setHeight(40);
			}
			display.setX(r.x());
			display.setY(r.y());
			MoveActionCommand command = new MoveActionCommand();
			command.setDisplay(display);
			command.setAction(action);
			return command;
		}
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if(request.getNewObject() instanceof BscFlowAction){
			BscFlowDef flow = (BscFlowDef) getHost().getModel();
			BscFlowAction action = (BscFlowAction) request.getNewObject();
			Rectangle r = (Rectangle) getConstraintFor(request);
			DisplayDef display = new DisplayDef();
			display.setX(r.x());
			display.setY(r.y());
			if(-1 == r.width()){
				display.setWidth(100);
			}else{
				display.setWidth(r.width());
			}
			if(-1 == r.height()){
				display.setHeight(40);
			}else{
				display.setHeight(r.height());
			}
			if(action instanceof BscFlowActionDef){
				((BscFlowActionDef) action).setDisplay(display);
			}else if(action instanceof BscFlowStartActionDef){
				if(flow.getStartAction() != null){
					return null;
				}
				((BscFlowStartActionDef) action).setDisplay(display);
			}else if(action instanceof BscFlowEndActionDef){
				((BscFlowEndActionDef) action).setDisplay(display);
			}else{
				return null;
			}
			CreateActionCommand command = new CreateActionCommand();
			command.setAction(action);
			command.setFlow(flow);
			return command;
		}
		return null;
	}
	
}
