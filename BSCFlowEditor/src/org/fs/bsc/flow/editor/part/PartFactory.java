package org.fs.bsc.flow.editor.part;

import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartFactory;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowDef;
import org.fs.bsc.flow.model.BscFlowDirectionDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;

public class PartFactory implements EditPartFactory {

	@Override
	public EditPart createEditPart(EditPart editPart, Object model) {
		EditPart part = null;
		if(model instanceof BscFlowDef){
			part = new FlowEditPart();
		}else if(model instanceof BscFlowActionDef
				|| model instanceof BscFlowStartActionDef
				|| model instanceof BscFlowEndActionDef){
			part = new ActionEditPart();
		}else if(model instanceof BscFlowDirectionDef){
			part = new DirectionPart();
		}else{
			return null;
		}
		part.setModel(model);
		return part;
	}

}
