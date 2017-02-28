package org.fs.bsc.flow.editor.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FreeformLayer;
import org.eclipse.draw2d.FreeformLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.fs.bsc.flow.editor.policy.FlowEditPartPolicy;
import org.fs.bsc.flow.model.BscFlowDef;

public class FlowEditPart extends AbstractGraphicalEditPart implements PropertyChangeListener {
	
	@Override
	public void activate() {
		super.activate();
		((BscFlowDef) getModel()).addPropertyChangeListener(this);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		((BscFlowDef) getModel()).removePropertyChangeListener(this);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List getModelChildren() {
		List list = new ArrayList<Object>();
		BscFlowDef flow = (BscFlowDef) getModel();
		list.addAll(flow.getActions());
		if(flow.getStartAction() != null){
			list.add(flow.getStartAction());
		}
		list.addAll(flow.getEndActions());
		return list;
	}

	@Override
	protected IFigure createFigure() {
		Figure figure = new FreeformLayer();
		figure.setLayoutManager(new FreeformLayout());
		return figure;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.LAYOUT_ROLE, new FlowEditPartPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		String prop = evt.getPropertyName();
		if(BscFlowDef.EVENT_ADD_ACTION.equals(prop)
				|| BscFlowDef.EVENT_REMOVE_ACTION.equals(prop)){
			refreshChildren();
		}
	}

}
