package org.fs.bsc.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.fs.bsc.flow.editor.command.DeleteDirectionCommand;
import org.fs.bsc.flow.model.BscFlowDirectionDef;

public class DirectionEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteDirectionCommand command = new DeleteDirectionCommand();
		command.setDirection((BscFlowDirectionDef) getHost().getModel());
		return command;
	}
}
