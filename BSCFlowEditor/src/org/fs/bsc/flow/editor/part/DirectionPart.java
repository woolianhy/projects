package org.fs.bsc.flow.editor.part;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.draw2d.BendpointConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractConnectionEditPart;
import org.eclipse.gef.editpolicies.ConnectionEndpointEditPolicy;
import org.fs.bsc.flow.editor.policy.DirectionEditPolicy;
import org.fs.bsc.flow.model.BscFlowDirectionDef;

public class DirectionPart extends AbstractConnectionEditPart implements PropertyChangeListener{
	
	private Label label;
	
	@Override
	protected IFigure createFigure() {
		PolylineConnection conn = new PolylineConnection();
		conn.setTargetDecoration(new PolygonDecoration());
		conn.setConnectionRouter(new BendpointConnectionRouter());
		label = new Label(getLableText());
		conn.add(label, new MidpointLocator(conn, 0){
			@Override
			protected Point getReferencePoint() {
				Point p = super.getReferencePoint();
				Point sourcePoint = getConnection().getSourceAnchor().getLocation(new Point());
				Point targetPoint = getConnection().getTargetAnchor().getLocation(new Point());
				if(targetPoint.y > sourcePoint.y && targetPoint.x > sourcePoint.x
						|| targetPoint.y < sourcePoint.y && targetPoint.x < sourcePoint.x){
					return p.getTranslated(10, -10);
				}else{
					return p.getTranslated(-10, -10);
				}
			}
		});
		return conn;
	}

	@Override
	protected void createEditPolicies() {
		installEditPolicy(EditPolicy.COMPONENT_ROLE, new DirectionEditPolicy());
		installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new ConnectionEndpointEditPolicy());
	}
	
	@Override
	public void activate() {
		super.activate();
		BscFlowDirectionDef direction = (BscFlowDirectionDef) getModel();
		direction.addPropertyChangeListener(this);
	}
	
	@Override
	public void deactivate() {
		super.deactivate();
		BscFlowDirectionDef direction = (BscFlowDirectionDef) getModel();
		direction.removePropertyChangeListener(this);
	}
	
	@Override
	protected void refreshVisuals() {
		super.refreshVisuals();
		if(label != null){
			label.setText(getLableText());
		}
	}
	
	protected String getLableText(){
		String desc = null;
		BscFlowDirectionDef direction = (BscFlowDirectionDef) getModel();
		if (direction != null) {
			desc = direction.getDesc();
			if(null == desc || desc.trim().equals("")){
				desc = direction.getExpression();
			}
		}
		if(null == desc){
			desc = "";
		}
		return desc;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		refreshVisuals();
	}

}
