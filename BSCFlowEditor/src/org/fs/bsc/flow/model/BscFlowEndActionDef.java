package org.fs.bsc.flow.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.fs.bsc.component.flow.BscFlowDirection;
import org.fs.bsc.component.flow.BscFlowEndAction;

public class BscFlowEndActionDef extends BscFlowEndAction {
public static final String EVENT_ACTION_CHANGE = "end action change";
	
	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	
	private DisplayDef display;
	
	private List<BscFlowDirection> sourceDirections;

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

	public void notifyChange(Object oldValue, Object newValue){
		listeners.firePropertyChange(EVENT_ACTION_CHANGE, oldValue, newValue);
	}
	
	public void addPropertyChangeListener(PropertyChangeListener l) {
		listeners.addPropertyChangeListener(l);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener l) {
		listeners.removePropertyChangeListener(l);
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
}
