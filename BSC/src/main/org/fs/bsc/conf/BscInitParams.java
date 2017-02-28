package org.fs.bsc.conf;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.util.Properties;

public class BscInitParams {
	public static final String BASE_PATH;
	
	private static Properties prop;
	
	static {
		try{
			BASE_PATH = URLDecoder.decode(BscInitParams.class.getResource("/").getPath(), "UTF-8");
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	public static void loadProperties(String path){
		try{
			Properties tmpProp = new Properties();
			tmpProp.load(new FileInputStream(BASE_PATH + path));
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
