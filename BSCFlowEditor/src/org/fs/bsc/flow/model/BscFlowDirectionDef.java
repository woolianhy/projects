package org.fs.bsc.flow.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.component.flow.BscFlowDirection;

public class BscFlowDirectionDef extends BscFlowDirection implements IPropertySource {
	
	public static final String EVENT_DIRECTION_CHANGE = "direction change";
	private BscFlowAction sourceAction;
	private BscFlowAction targetAction;
	
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	public BscFlowDirectionDef() {
		setActionCode("");
		setExpression("");
		setDesc("");
	}
	
	public BscFlowAction getSourceAction() {
		return sourceAction;
	}

	public void setSourceAction(BscFlowAction sourceAction) {
		this.sourceAction = sourceAction;
	}

	public BscFlowAction getTargetAction() {
		return targetAction;
	}

	public void setTargetAction(BscFlowAction targetAction) {
		this.targetAction = targetAction;
	}
	
	public void notifyChange(Object oldValue, Object newValue){
		listeners.firePropertyChange(EVENT_DIRECTION_CHANGE, oldValue, newValue);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}

	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
		propertyDescriptors.add(new TextPropertyDescriptor("actionCode", "目标组件标识"));
		propertyDescriptors.add(new TextPropertyDescriptor("expression", "表达式"));
		propertyDescriptors.add(new TextPropertyDescriptor("desc", "描述"));
		IPropertyDescriptor[] arr = new IPropertyDescriptor[propertyDescriptors.size()];
		return propertyDescriptors.toArray(arr);
	}

	@Override
	public Object getPropertyValue(Object key) {
		if("actionCode".equals(key)){
			return getActionCode();
		}else if("expression".equals(key)){
			return getExpression();
		}else if("desc".equals(key)){
			return getDesc();
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object key) {
		if("expression".equals(key) || "desc".equals(key)){
			return true;
		}
		return false;
	}

	@Override
	public void resetPropertyValue(Object arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyValue(Object key, Object value) {
		if(!isPropertySet(key)){
			return;
		}
		if("expression".equals(key)){
			String oldValue = getExpression();
			setExpression((String) value);
			notifyChange(oldValue, value);
			return;
		}else if("desc".equals(key)){
			String oldValue = getDesc();
			setDesc((String) value);
			notifyChange(oldValue, value);
			return;
		}
	}
}
