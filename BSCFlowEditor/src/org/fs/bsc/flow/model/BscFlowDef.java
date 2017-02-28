package org.fs.bsc.flow.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Iterator;

import org.fs.bsc.component.flow.BscFlow;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.component.flow.BscFlowEndAction;

public class BscFlowDef extends BscFlow {
	
	public static final String EVENT_ADD_ACTION = "add action";
	public static final String EVENT_REMOVE_ACTION = "remove action";
	
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	private DisplayDef display;

	public DisplayDef getDisplay() {
		return display;
	}

	public void setDisplay(DisplayDef display) {
		this.display = display;
	}

	public void addAction(BscFlowAction action) {
		if(action instanceof BscFlowActionDef){
			getActions().add(action);
		}else if(action instanceof BscFlowStartActionDef){
			setStartAction((BscFlowStartActionDef) action);
		}else if(action instanceof BscFlowEndActionDef){
			getEndActions().add((BscFlowEndAction) action);
		}
		listeners.firePropertyChange(EVENT_ADD_ACTION, null, action);
	}
	
	public boolean removeAction(BscFlowAction action){
		if(action instanceof BscFlowActionDef){
			Iterator<BscFlowAction> ite = getActions().iterator();
			while(ite.hasNext()){
				if(action.equals(ite.next())){
					ite.remove();
					listeners.firePropertyChange(EVENT_REMOVE_ACTION, action, null);
					return true;
				}
			}
		}else if(action instanceof BscFlowStartActionDef){
			if(action.equals(getStartAction())){
				setStartAction(null);
				listeners.firePropertyChange(EVENT_REMOVE_ACTION, action, null);
				return true;
			}
		}else if(action instanceof BscFlowEndActionDef){
			Iterator<BscFlowEndAction> ite = getEndActions().iterator();
			while(ite.hasNext()){
				if(action.equals(ite.next())){
					ite.remove();
					listeners.firePropertyChange(EVENT_REMOVE_ACTION, action, null);
					return true;
				}
			}
		}
		return false;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
	}
	
}
