package org.fs.bsc.flow.editor.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.gef.editparts.AbstractTreeEditPart;
import org.eclipse.swt.graphics.Image;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;

public class OutlineActionEditPart extends AbstractTreeEditPart implements PropertyChangeListener {

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
	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();
	}
	
	@Override
	protected void refreshVisuals() {
		setWidgetImage(new Image(null, this.getClass().getResourceAsStream("/icons/sample.gif")));
		setWidgetText(((BscFlowAction) getModel()).getCode());
	}

}
