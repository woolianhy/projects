package org.fs.bsc.flow.editor.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.draw2d.ChopboxAnchor;
import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.editor.figure.ActionFigure;
import org.fs.bsc.flow.editor.policy.ActionEditPolicy;
import org.fs.bsc.flow.editor.policy.ActionNodeEditPolicy;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;
import org.fs.bsc.flow.model.DisplayDef;

public class ActionEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener,NodeEditPart {
	
	@Override
	protected IFigure createFigure() {
		return new ActionFigure();
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new ActionEditPolicy());
		installEditPolicy(EditPolicy.GRAPHICAL_NODE_ROLE, new ActionNodeEditPolicy());
	}
	
	@Override
	public void performRequest(Request request) {
		// TODO Auto-generated method stub
		super.performRequest(request);
	}
	
	@Override
	public void activate() {
		super.activate();
		BscFlowAction model = (BscFlowAction) getModel();
		if(model instanceof BscFlowActionDef){
			((BscFlowActionDef) model).addPropertyChangeListener(this);
		}else if(model instanceof BscFlowStartActionDef){
			((BscFlowStartActionDef) model).addPropertyChangeListener(this);
		}else if(model instanceof BscFlowEndActionDef){
			((BscFlowEndActionDef) model).addPropertyChangeListener(this);
		}
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		BscFlowAction model = (BscFlowAction) getModel();
		if(model instanceof BscFlowActionDef){
			((BscFlowActionDef) model).removePropertyChangeListener(this);
		}else if(model instanceof BscFlowStartActionDef){
			((BscFlowStartActionDef) model).removePropertyChangeListener(this);
		}else if(model instanceof BscFlowEndActionDef){
			((BscFlowEndActionDef) model).removePropertyChangeListener(this);
		}
	}

	@Override
	protected void refreshVisuals() {
		String text = "";
		BscFlowAction action = (BscFlowAction) getModel();
		DisplayDef display = null;
		if(action instanceof BscFlowActionDef){
			BscFlowActionDef actionDef = ((BscFlowActionDef) action);
			String name = actionDef.getName();
			if(null == name || "".equals(name.trim())){
				name = actionDef.getComponentCode();
			}
			text = "[" + name + "]\n" + action.getCode();
			display = actionDef.getDisplay();
		}else if(action instanceof BscFlowStartActionDef){
			display = ((BscFlowStartActionDef) action).getDisplay();
			text = "[开始]\n" + action.getCode();
		}else if(action instanceof BscFlowEndActionDef){
			display = ((BscFlowEndActionDef) action).getDisplay();
			text = "[结束]\n" + action.getCode();
		}
		ActionFigure figure = (ActionFigure) getFigure();
		figure.setText(text);
		Point loc = new Point();
		Dimension d = null;
		if(display != null){
			loc.setX(display.getX());
			loc.setY(display.getY());
			d = new Dimension(display.getWidth(), display.getHeight());
		}else{
			loc.setX(10);
			loc.setY(10);
			d = new Dimension(100, 40);
		}
		Rectangle r = new Rectangle(loc, d);
		((GraphicalEditPart) getParent()).setLayoutConstraint(this, figure, r);
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if(BscFlowActionDef.EVENT_ACTION_CHANGE.equals(evt.getPropertyName())
				|| BscFlowStartActionDef.EVENT_ACTION_CHANGE.equals(evt.getPropertyName())
				|| BscFlowEndActionDef.EVENT_ACTION_CHANGE.equals(evt.getPropertyName())){
			refreshVisuals();
			refreshTargetConnections();
			refreshSourceConnections();
		}
	}
	
	// ------------------------------------------------------------------------
	// Abstract methods from NodeEditPart

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart part) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart part) {
		return new ChopboxAnchor(getFigure());
	}

	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		return new ChopboxAnchor(getFigure());
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelSourceConnections() {
		BscFlowAction action = (BscFlowAction) getModel();
		return action.getDirections();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	protected List getModelTargetConnections() {
		BscFlowAction action = (BscFlowAction) getModel();
		if(action instanceof BscFlowActionDef){
			return ((BscFlowActionDef) action).getSourceDirections();
		}else if(action instanceof BscFlowEndActionDef){
			return ((BscFlowEndActionDef) action).getSourceDirections();
		}
		return super.getModelTargetConnections();
	}
}
