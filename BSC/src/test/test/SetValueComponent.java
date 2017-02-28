package test;

import java.util.Map;

import org.fs.bsc.component.AbstractBscComponent;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.context.BscContext;

public class SetValueComponent extends AbstractBscComponent {

	@Override
	public String execute(BscContext context) {
		@SuppressWarnings("unchecked")
		Map<String, Object> params =
				(Map<String, Object>) context.getExtParam(BscFlowAction.CONTEXT_PARAM_NAME);
		String names = (String) params.get("names");
		String values = (String) params.get("values");
		if(names != null && values != null){
			String[] nameArr = names.split("\\|");
			String[] valueArr = values.split("\\|");
			for(int i = 0, len = nameArr.length; i < len; i++){
				context.put(nameArr[i], valueArr[i]);
			}
		}
		return RESULT_SUCCESS;
	}

}
