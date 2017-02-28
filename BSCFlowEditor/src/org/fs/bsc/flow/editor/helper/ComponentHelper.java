package org.fs.bsc.flow.editor.helper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fs.bsc.flow.XmlBscComponentTransformer;
import org.fs.bsc.flow.model.BscComponentDef;

public class ComponentHelper {
	
	protected static Map<String, BscComponentDef> componentMap;
	
	protected static List<BscComponentDef> componentList;
	
	public static void loadComponent(InputStream in){
		Map<String, BscComponentDef> map = new HashMap<String, BscComponentDef>();
		List<BscComponentDef> list =
				XmlBscComponentTransformer.toActionComponent(in);
		for(BscComponentDef component: list){
			map.put(component.getCode(), component);
		}
		componentList = list;
		componentMap = map;
	}
	
	public static BscComponentDef getComponent(String code){
		if(componentMap != null){
			return componentMap.get(code);
		}
		return null;
	}

	public static List<BscComponentDef> getComponentList() {
		return componentList;
	}
	
	
}
