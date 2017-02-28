package org.fs.bsc.conf;

import org.fs.bsc.BscServiceProvider;
import org.fs.bsc.component.flow.BscFlowResolver;
import org.fs.bsc.component.flow.FelExpressionExecutor;
import org.fs.bsc.component.flow.loader.XmlBscFlowComponentLoader;
import org.fs.bsc.context.BscContextManager;
import org.fs.bsc.event.BscEventManager;
import org.fs.bsc.exec.BscExecuteManager;
import org.fs.bsc.exec.DefaultBscComponentProvider;
import org.fs.bsc.exec.DefaultBscExecuteManager;
import org.fs.bsc.exec.DefaultBscExecutor;
import org.fs.bsc.loader.XmlBscComponentLoader;

public class DefaultBscConfiguration implements BscConfiguration {
	
	private BscExecuteManager executeManager;
	private BscContextManager contextManager;
	private BscEventManager eventManager;
	
	@Override
	public BscExecuteManager getBscExecuteManager() {
		return executeManager;
	}
	
	@Override
	public BscContextManager getBscContextManager() {
		return contextManager;
	}

	@Override
	public BscEventManager getBscEventManager() {
		return eventManager;
	}

	@Override
	public void configure(BscServiceProvider provider) {
		configureEventManager(provider);
		configureContextManager(provider);
		configureExecuteManager(provider);
	}
	
	protected void configureEventManager(BscServiceProvider provider){
		eventManager = new BscEventManager();
	}
	
	protected void configureContextManager(BscServiceProvider provider){
		contextManager = new BscContextManager();
	}
	
	protected void configureExecuteManager(BscServiceProvider provider){
		DefaultBscExecuteManager defaultBscExecuteManager = new DefaultBscExecuteManager();
		DefaultBscComponentProvider defaultBscComponentProvider = new DefaultBscComponentProvider();
		
		XmlBscComponentLoader loader = new XmlBscComponentLoader();
		loader.setConfigFilePath(BscInitParams.getParam("BSC_COMPONENT_FILE_PATH", "/bsc_components.xml"));
		defaultBscComponentProvider.addComponentLoader(loader);
		XmlBscFlowComponentLoader flowComponentLoader = new XmlBscFlowComponentLoader();
		BscFlowResolver flowResolver = new BscFlowResolver();
		flowResolver.setBscServiceProvider(provider);
		flowComponentLoader.setFlowResolver(flowResolver);
		flowComponentLoader.setScanPath(BscInitParams.BASE_PATH + BscInitParams.getParam("BSC_FLOW_FILE_SCAN_PATH", "/flows"));
		flowComponentLoader.setScanSuffix(BscInitParams.getParam("BSC_FLOW_FILE_SCAN_SUFFIX", ".bsc"));
		defaultBscComponentProvider.addComponentLoader(flowComponentLoader);
		
		defaultBscComponentProvider.loadComponents();
		
		defaultBscExecuteManager.setComponentProvider(defaultBscComponentProvider);
		DefaultBscExecutor executor = new DefaultBscExecutor();
		executor.setProvider(provider);
		defaultBscExecuteManager.setExecutor(executor);
		defaultBscExecuteManager.setExpressionExecutor(new FelExpressionExecutor());
		executeManager = defaultBscExecuteManager;
	}

}
