package org.fs.bsc.component.flow;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fs.bsc.BscServiceProvider;
import org.fs.bsc.component.flow.event.BscFlowActionEvent;
import org.fs.bsc.component.flow.event.BscFlowDirectionEvent;
import org.fs.bsc.component.flow.event.BscFlowEvent;
import org.fs.bsc.component.flow.inst.BscFlowInst;
import org.fs.bsc.context.BscContext;
import org.fs.bsc.def.BscFieldDef;
import org.fs.bsc.def.BscFieldRelDef;
import org.fs.bsc.event.BscComponentEvent;
import org.fs.bsc.exception.BscException;

/**
 * 
 * BSC流程执行器
 * @author Jingxin.Gao
 *
 */
public class BscFlowResolver {
	
	private BscServiceProvider provider;
	
	public void setBscServiceProvider(BscServiceProvider provider) {
		this.provider = provider;
	}
	
	public BscServiceProvider getProvider() {
		return provider;
	}
	
	public String executeFlow(BscFlowComponent flowComponent, BscContext context){
		BscFlow flow = flowComponent.getFlow();
		BscContext flowContext = provider.getBscContextManager().newContext();
		BscFlowInst flowInst = new BscFlowInst();
		flowInst.setFlowResolver(this);
		flowInst.setFlow(flow);
		flowInst.setContext(flowContext);
		provider.getEventManager().publishEvent(new BscFlowEvent(flow, BscFlowEvent.INIT));
		flowInst.init();
		String result;
		List<BscFieldDef> inputFields = flowComponent.getInputFields();
		List<BscFieldDef> outputFields = flowComponent.getOutputFields();
		if(null == inputFields || inputFields.isEmpty()){
			flowContext.putAll(context);
		}else{
			for(BscFieldDef fieldDef : inputFields){
				flowContext.put(fieldDef.getCode(), context.get(fieldDef.getCode()));
			}
		}
		try{
			result = flowInst.forward(flow.getStartAction());
			provider.getEventManager().publishEvent(
					new BscFlowEvent(flow, BscFlowEvent.END, result));
		}catch(RuntimeException e){
			provider.getEventManager().publishEvent(
					new BscFlowEvent(flow, BscFlowEvent.EXCEPTION, e));
			throw e;
		}finally{
			provider.getEventManager().publishEvent(new BscFlowEvent(flow, BscFlowEvent.DESTROY));
			flowInst.destroy();
			if(null == outputFields || outputFields.isEmpty()){
				context.putAll(flowContext);
			}else{
				for(BscFieldDef fieldDef : outputFields){
					context.put(fieldDef.getCode(), flowContext.get(fieldDef.getCode()));
				}
			}
		}
		return result;
	}
	
	public String executeAction(BscFlow flow, BscFlowInst flowInst,
			BscFlowAction action, BscContext context){
		if(action instanceof BscFlowEndAction){
			return BscFlowAction.RESULT_SUCCESS;
		}
		if(action instanceof BscFlowStartAction){
			provider.getEventManager().publishEvent(new BscFlowEvent(flow, BscFlowEvent.START));
			provider.getEventManager().publishEvent(
					new BscFlowActionEvent(action, BscFlowActionEvent.START));
		}
		BscContext actionContext = null;
		List<BscFieldRelDef> inputs = action.getInputs();
		List<BscFieldRelDef> outputs = action.getOutputs();
		String result;
		try{
			long start = System.currentTimeMillis();
			if(action instanceof BscFlowStartAction){
				result = BscFlowAction.RESULT_SUCCESS;
			}else{
				actionContext = provider.getBscContextManager().newContext();
				actionContext.setExtParam(BscFlowAction.CONTEXT_PARAM_NAME, action.getParams());
				if(null == inputs || inputs.isEmpty()){
					actionContext.putAll(context);
				}else{
					for(BscFieldRelDef fieldRef : inputs){
						actionContext.put(fieldRef.getTo(), context.get(fieldRef.getFrom()));
					}
				}
				result = provider.getBscExecuteManager().execute(
						action.getComponentCode(), actionContext);
			}
			long end = System.currentTimeMillis();
			Map<String, Object> event = new HashMap<String, Object>();
			event.put(BscComponentEvent.EVENT_END_PARAM_STARTTIME, start);
			event.put(BscComponentEvent.EVENT_END_PARAM_ENDTIME, end);
			event.put(BscComponentEvent.EVENT_END_PARAM_RESULT, result);
			provider.getEventManager().publishEvent(
					new BscFlowActionEvent(action, BscFlowActionEvent.END, event));
		}catch(RuntimeException e){
			provider.getEventManager().publishEvent(
					new BscFlowActionEvent(action, BscFlowActionEvent.EXCEPTION, e));
			throw e;
		}finally{
			if(actionContext != null){
				if(null == outputs || outputs.isEmpty()){
					context.putAll(actionContext);
				}else{
					for(BscFieldRelDef fieldRef : outputs){
						context.put(fieldRef.getTo(), actionContext.get(fieldRef.getFrom()));
					}
				}
			}
		}
		BscFlowAction nextAction = null;
		if(action.getDirections() != null && (!action.getDirections().isEmpty())){
			for(BscFlowDirection direction : action.getDirections()){
				provider.getEventManager().publishEvent(
						new BscFlowDirectionEvent(direction, BscFlowDirectionEvent.CHECK));
				if(provider.getBscExecuteManager().checkCondition(direction.getExpression(), context)){
					provider.getEventManager().publishEvent(
							new BscFlowDirectionEvent(direction, BscFlowDirectionEvent.ACCEPT));
					nextAction = getActionByCode(flow, direction.getActionCode());
					break;
				}
			}
		}
		if(nextAction != null){
			provider.getEventManager().publishEvent(new BscFlowEvent(flow, BscFlowEvent.FORWARD, nextAction));
			flowInst.forward(nextAction);
		}else{
			throw new BscException("can not found next action !");
		}
		return BscFlowAction.RESULT_SUCCESS;
	}
	
	private BscFlowAction getActionByCode(BscFlow flow, String code){
		if(null == code){
			return null;
		}
		for(BscFlowEndAction action : flow.getEndActions()){
			if(code.equals(action.getCode())){
				return action;
			}
		}
		for(BscFlowAction action : flow.getActions()){
			if(code.equals(action.getCode())){
				return action;
			}
		}
		return null;
	}
}
