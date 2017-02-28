package org.fs.bsc.flow.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.component.flow.BscFlowDirection;
import org.fs.bsc.flow.editor.helper.ComponentHelper;
import org.fs.bsc.flow.editor.views.ButtonPropertyDescriptor;
import org.fs.bsc.flow.editor.views.ParamDialog;

public class BscFlowActionDef extends BscFlowAction implements IPropertySource {
	
	public static final String ACTION_TYPE_NORMAL = "normal";
	
	public static final String ACTION_TYPE_CALL = "call";
	
	public static final String EVENT_ACTION_CHANGE = "action change";
	
	public static final String ACTION_PARAM_ACTION_TYPE = "_ACTION_TYPE";
	
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	private DisplayDef display;
	
	private List<BscFlowDirection> sourceDirections;
	
	private String type;
	
	public BscFlowActionDef() {
		setCode("");
		setComponentCode("");
		setName("");
		setDesc("");
	}
	
	public DisplayDef getDisplay() {
		return display;
	}

	public void setDisplay(DisplayDef display) {
		this.display = display;
	}

	public List<BscFlowDirection> getSourceDirections() {
		return sourceDirections;
	}

	public void setSourceDirections(List<BscFlowDirection> sourceDirections) {
		this.sourceDirections = sourceDirections;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public void setCode(String code) {
//		String oldCode = getCode();
		super.setCode(code);
//		listeners.firePropertyChange(EVENT_ACTION_CHANGE, oldCode, code);
	}
	
	public void notifyChange(Object oldValue, Object newValue){
		listeners.firePropertyChange(EVENT_ACTION_CHANGE, oldValue, newValue);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}
	
	public void addDirection(BscFlowDirection direction){
		List<BscFlowDirection> directions = getDirections();
		if(null == directions){
			directions = new ArrayList<BscFlowDirection>();
			setDirections(directions);
		}
		directions.add(direction);
		listeners.firePropertyChange(EVENT_ACTION_CHANGE, null, direction);
	}
	
	public boolean removeDirection(BscFlowDirection direction){
		List<BscFlowDirection> directions = getDirections();
		if(directions != null && directions.contains(direction)){
			directions.remove(direction);
			listeners.firePropertyChange(EVENT_ACTION_CHANGE, null, direction);
			return true;
		}
		return false;
	}
	
	public void addSourceDirection(BscFlowDirection direction){
		List<BscFlowDirection> directions = getSourceDirections();
		if(null == directions){
			directions = new ArrayList<BscFlowDirection>();
			setSourceDirections(directions);
		}
		directions.add(direction);
		listeners.firePropertyChange(EVENT_ACTION_CHANGE, null, direction);
	}
	
	public boolean removeSourceDirection(BscFlowDirection direction){
		List<BscFlowDirection> directions = getSourceDirections();
		if(directions != null && directions.contains(direction)){
			directions.remove(direction);
			listeners.firePropertyChange(EVENT_ACTION_CHANGE, null, direction);
			return true;
		}
		return false;
	}

	// ------------------------------------------------------------------------
	// Abstract methods from IPropertySource
	
	@Override
	public Object getEditableValue() {
		return this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		List<IPropertyDescriptor> propertyDescriptors = new ArrayList<IPropertyDescriptor>();
		if(ACTION_TYPE_CALL.equals(type)){
			propertyDescriptors.add(new TextPropertyDescriptor("componentCode", "调用组件代码"));
		}
		propertyDescriptors.add(new PropertyDescriptor("code", "组件标识"));
		propertyDescriptors.add(new TextPropertyDescriptor("name", "组件名称"));
		propertyDescriptors.add(new TextPropertyDescriptor("desc", "组件描述"));
		propertyDescriptors.add(new ButtonPropertyDescriptor("input_outputs", "输入输出", ParamDialog.class, this));
		
		BscComponentDef component = ComponentHelper.getComponent(getComponentCode());
		if(component != null && component.getParams() != null){
			for(Map.Entry<String, Object> entry : component.getParams().entrySet()){
				Map<String, Object> paramMap = (Map<String, Object>) entry.getValue();
				String name = (String) paramMap.get("name");
				if(null == name || name.trim().equals("")){
					name = (String) paramMap.get("code");
				}
				propertyDescriptors.add(new TextPropertyDescriptor("param." + entry.getKey(), name));
			}
		}
		IPropertyDescriptor[] arr = new IPropertyDescriptor[propertyDescriptors.size()];
		return propertyDescriptors.toArray(arr);
	}

	@Override
	public Object getPropertyValue(Object key) {
		if("componentCode".equals(key)){
			return getComponentCode();
		}else if("code".equals(key)){
			return getCode();
		}else if("name".equals(key)){
			return getName();
		}else if("desc".equals(key)){
			return getDesc();
		}else if("input_outputs".equals(key)){
			return new BscFlowActionDefWrapper(this);
		}else if(key instanceof String && ((String) key).startsWith("param.")){
			if(getParams() != null){
				String paramKey = ((String) key).substring("param.".length());
				Object obj = getParams().get(paramKey);
				return obj != null ? obj : "";
			}
		}
		return "";
	}

	@Override
	public boolean isPropertySet(Object key) {
		if("componentCode".equals(key) && ACTION_TYPE_CALL.equals(type)){
			return true;
		}
		if("name".equals(key) || "desc".equals(key) || "input_outputs".equals(key)){
			return true;
		}
		if(key instanceof String && ((String) key).startsWith("param.")){
			String paramKey = ((String) key).substring("param.".length());
			BscComponentDef component = ComponentHelper.getComponent(getComponentCode());
			if(component != null && component.getParams() != null){
				return component.getParams().containsKey(paramKey);
			}
		}
		return false;
	}

	@Override
	public void resetPropertyValue(Object key) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setPropertyValue(Object key, Object value) {
		if(!isPropertySet(key)){
			return;
		}
		if("componentCode".equals(key) && ACTION_TYPE_CALL.equals(type)){
			setComponentCode((String) value);
			return;
		}else if("name".equals(key)){
			setName((String) value);
			notifyChange(null, null);
			return;
		}else if("desc".equals(key)){
			setDesc((String) value);
			return;
		}
		if(key instanceof String && ((String) key).startsWith("param.")){
			BscComponentDef component = ComponentHelper.getComponent(getComponentCode());
			if(component != null && component.getParams() != null){
				if(null == getParams()){
					setParams(new HashMap<String, Object>());
				}
				String paramKey = ((String) key).substring("param.".length());
				getParams().put(paramKey, value);
				return;
			}
		}
		
	}
	
	public static class BscFlowActionDefWrapper{
		public BscFlowActionDef action;
		public BscFlowActionDefWrapper(BscFlowActionDef action) {
			this.action = action;
		}
	}
}
