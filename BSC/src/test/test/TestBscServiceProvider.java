package test;

import java.util.HashMap;
import java.util.Map;

import org.fs.bsc.BscServiceProvider;
import org.fs.bsc.component.BscComponent;
import org.fs.bsc.conf.BscInitParams;
import org.fs.bsc.conf.DefaultBscConfiguration;
import org.fs.bsc.context.BscContext;
import org.fs.bsc.context.BscContextManager;
import org.fs.bsc.def.BscFieldDef;
import org.fs.bsc.exec.BscExecuteManager;

public class TestBscServiceProvider {
	public static void main(String[] args) {
		BscInitParams.loadProperties("/bsc_config.properties");
		
		final BscServiceProvider provider = new BscServiceProvider();
		provider.init(new DefaultBscConfiguration());
		
		BscExecuteManager executerManager = provider.getBscExecuteManager();
		
//		BscComponent test1Component = new Test1Component();
//		((DefaultBscExecuteManager) executerManager).getComponentProvider().addComponent("test1", test1Component);
//		BscComponent test2Component = new Test2Component();
//		((DefaultBscExecuteManager) executerManager).getComponentProvider().addComponent("test2", test2Component);
//		
//		BscFlow flow1 = new BscFlow();
//		BscFlowStartAction startAction = new BscFlowStartAction();
//		startAction.setCode("start");
//		List<BscFlowAction> actions = new ArrayList<BscFlowAction>();
//		flow1.setActions(actions);
//		BscFlowAction action1 = new BscFlowAction();
//		actions.add(action1);
//		action1.setCode("t2");
//		action1.setComponentCode("t2");
//		BscFlowEndAction endAction = new BscFlowEndAction();
//		endAction.setCode("end");
//		List<BscFlowEndAction> endActions = new ArrayList<BscFlowEndAction>();
//		flow1.setEndActions(endActions);
//		endActions.add(endAction);
//		List<BscFlowDirection> directions1 = new ArrayList<BscFlowDirection>();
//		BscFlowDirection d1 = new BscFlowDirection();
//		directions1.add(d1);
//		d1.setActionCode("t2");
//		startAction.setDirections(directions1);
//		
//		List<BscFlowDirection> directions2 = new ArrayList<BscFlowDirection>();
//		BscFlowDirection d2 = new BscFlowDirection();
//		directions2.add(d2);
//		d2.setActionCode("end");
//		
//		action1.setDirections(directions2);
//		flow1.setStartAction(startAction);
//		
//		
//		
//		BscFlowComponent flowC1 = new BscFlowComponent();
//		flowC1.setCode("f1");
//		flowC1.setName("testFlow1");
//		flowC1.setDesc("test flow 1");
//		flowC1.setFlow(flow1);
//		BscFlowResolver flowResolver = new BscFlowResolver();
//		flowResolver.setBscServiceProvider(provider);
//		flowC1.setFlowResolver(flowResolver);
//		
//		((DefaultBscExecuteManager) executerManager).getComponentProvider().addComponent(flowC1.getCode(), flowC1);
		
		BscContextManager contextManager = provider.getBscContextManager();
		
//		executerManager.execute("test1", contextManager.newContext());
//		executerManager.execute("t2", contextManager.newContext());
//		executerManager.execute("f1", contextManager.newContext());
//		executerManager.execute("flow1", contextManager.newContext());
		BscContext context = contextManager.newContext();
		context.put("flowId", String.valueOf(System.currentTimeMillis()));
		String componentCode = "flow2";
		executerManager.execute(componentCode, context);
		System.out.println(context);
		BscComponent component = executerManager.getComponent(componentCode);
		Map<String ,Object> resultMap = new HashMap<String, Object>();
		for(BscFieldDef field :component.getOutputFields()){
			resultMap.put(field.getCode(), context.get(field.getCode()));
		}
		System.out.println("result: " + resultMap);
		
//		for(int i = 0; i < 80; i++){
//			new Thread(){
//				@Override
//				public void run() {
//					provider.getBscExecuteManager().execute("flow2", provider.getBscContextManager().newContext());
//				}
//			}.start();
//		}
	}
}
