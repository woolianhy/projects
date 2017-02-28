package org.fs.bsc.flow.editor.policy;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.editor.command.DeleteActionCommand;
import org.fs.bsc.flow.model.BscFlowDef;

public class ActionEditPolicy extends ComponentEditPolicy {
	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		DeleteActionCommand command = new DeleteActionCommand();
		command.setFlow((BscFlowDef) getHost().getParent().getModel());
		command.setAction((BscFlowAction) getHost().getModel());
		return command;
	}
}
