package org.fs.bsc.exec;

import java.util.HashMap;
import java.util.Map;

import org.fs.bsc.BscServiceProvider;
import org.fs.bsc.component.BscComponent;
import org.fs.bsc.context.BscContext;
import org.fs.bsc.event.BscComponentEvent;

public class DefaultBscExecutor implements BscExecutor {

	private BscServiceProvider provider;
	
	@Override
	public String execute(BscComponent component, BscContext context) {
		String result;
		provider.getEventManager().publishEvent(new BscComponentEvent(component, BscComponentEvent.START, null));
		try{
			long start = System.currentTimeMillis();
			result = component.execute(context);
			long end = System.currentTimeMillis();
			Map<String, Object> event = new HashMap<String, Object>();
			event.put(BscComponentEvent.EVENT_END_PARAM_STARTTIME, start);
			event.put(BscComponentEvent.EVENT_END_PARAM_ENDTIME, end);
			event.put(BscComponentEvent.EVENT_END_PARAM_RESULT, result);
			provider.getEventManager().publishEvent(new BscComponentEvent(component, BscComponentEvent.END, event));
		}catch(RuntimeException e){
			provider.getEventManager().publishEvent(new BscComponentEvent(component, BscComponentEvent.EXCEPTION, e));
			throw e;
		}
		return result;
	}

	public BscServiceProvider getProvider() {
		return provider;
	}

	public void setProvider(BscServiceProvider provider) {
		this.provider = provider;
	}

	 
}
