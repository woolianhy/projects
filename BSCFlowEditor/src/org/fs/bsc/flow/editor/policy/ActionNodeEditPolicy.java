package org.fs.bsc.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.GraphicalNodeEditPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.editor.command.CreateDirectionCommand;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowDirectionDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;

public class ActionNodeEditPolicy extends GraphicalNodeEditPolicy {

	@Override
	protected Command getConnectionCompleteCommand(
			CreateConnectionRequest request) {
		BscFlowAction action = (BscFlowAction) getHost().getModel();
		CreateDirectionCommand command = (CreateDirectionCommand) request.getStartCommand();
		if(command != null
				&& (action instanceof BscFlowActionDef || action instanceof BscFlowEndActionDef)){
			command.getDirection().setTargetAction(action);
			command.getDirection().setActionCode(action.getCode());
			return command;
		}
		return null;
	}

	@Override
	protected Command getConnectionCreateCommand(
			CreateConnectionRequest request) {
		Object model = getHost().getModel();
		if(model instanceof BscFlowActionDef || model instanceof BscFlowStartActionDef){
			BscFlowAction action = (BscFlowAction) model;
			CreateDirectionCommand command = new CreateDirectionCommand();
			command.setAction(action);
			BscFlowDirectionDef direction = new BscFlowDirectionDef();
			direction.setSourceAction(action);
			command.setDirection(direction);
			request.setStartCommand(command);
			return command;
		}
		return null;
	}

	@Override
	protected Command getReconnectSourceCommand(
			ReconnectRequest reconnectrequest) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Command getReconnectTargetCommand(
			ReconnectRequest reconnectrequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
