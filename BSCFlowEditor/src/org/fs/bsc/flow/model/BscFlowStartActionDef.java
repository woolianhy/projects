package org.fs.bsc.flow.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.fs.bsc.component.flow.BscFlowDirection;
import org.fs.bsc.component.flow.BscFlowStartAction;

public class BscFlowStartActionDef extends BscFlowStartAction {
	
	public static final String EVENT_ACTION_CHANGE = "start action change";
	
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	private DisplayDef display;

	public DisplayDef getDisplay() {
		return display;
	}

	public void setDisplay(DisplayDef display) {
		this.display = display;
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
}
