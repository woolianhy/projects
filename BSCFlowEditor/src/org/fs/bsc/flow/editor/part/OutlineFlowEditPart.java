package org.fs.bsc.flow.editor.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.fs.bsc.flow.model.BscFlowDef;

public class OutlineFlowEditPart extends AbstractTreeEditPart implements PropertyChangeListener {
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refreshChildren();
	}
	
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
}
