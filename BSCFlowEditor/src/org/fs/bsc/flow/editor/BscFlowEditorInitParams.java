package org.fs.bsc.flow.editor;

import java.util.Properties;

public class BscFlowEditorInitParams {
	
	private static Properties prop;
	
	static {
		try{
			loadProperties("/bsc_editor_config.properties");
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static void loadProperties(String path){
		try{
			Properties tmpProp = new Properties();
			tmpProp.load(BscFlowEditorInitParams.class.getResourceAsStream(path));
			prop = tmpProp;
		}catch(Exception e){
			throw new RuntimeException("BscInitParams.loadProperties: error", e);
		}
	}
	
	public static String getParam(String key){
		if(null == prop){
			return null;
		}else{
			return prop.getProperty(key);
		}
	}
	
	public static String getParam(String key, String defaultValue){
		if(null == prop){
			return defaultValue;
		}else{
			return prop.getProperty(key, defaultValue);
		}
	}
}
