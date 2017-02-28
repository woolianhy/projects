package org.fs.bsc.flow.model;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.gef.requests.CreationFactory;

public class BscFlowActionDefFactory implements CreationFactory {
	
	private BscComponentDef componentDef;
	
	private String type;
	
	public BscFlowActionDefFactory(BscComponentDef component) {
		this(component, BscFlowActionDef.ACTION_TYPE_NORMAL);
	}
	
	public BscFlowActionDefFactory(BscComponentDef component, String type) {
		this.componentDef = component;
		this.type = type;
	}

	@Override
	public Object getNewObject() {
		BscFlowActionDef action;
		try {
			action = BscFlowActionDef.class.newInstance();
			action.setType(type);
			action.setComponentCode(componentDef.getCode());
			String name = componentDef.getName();
			action.setName(name != null ? name : "");
			if(componentDef.getParams() != null){
				Map<String, Object> params = new HashMap<String, Object>();
				for(Map.Entry<String, Object> param : componentDef.getParams().entrySet()){
					String defaultValue = (String) ((Map<String, Object>) param.getValue()).get("defaultValue");
					params.put(param.getKey(), defaultValue != null ? defaultValue : "");
				}
				action.setParams(params);
			}
			if(BscFlowActionDef.ACTION_TYPE_CALL.equals(type)){
				if(null == action.getParams()){
					Map<String, Object> params = new HashMap<String, Object>();
					action.setParams(params);
				}
				action.getParams().put(BscFlowActionDef.ACTION_PARAM_ACTION_TYPE, BscFlowActionDef.ACTION_TYPE_CALL);
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return action;
	}
	
	@Override
	public Object getObjectType() {
		return BscFlowActionDef.class;
	}
}
