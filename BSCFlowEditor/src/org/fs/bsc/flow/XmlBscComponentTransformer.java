package org.fs.bsc.flow;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fs.bsc.flow.model.BscComponentDef;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlBscComponentTransformer {
	public static List<BscComponentDef> toActionComponent(InputStream in) {
		try{
			List<BscComponentDef> list = new ArrayList<BscComponentDef>();
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(in);
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			for(int i = 0, len = nodes.getLength(); i < len; i++){
				Node node = nodes.item(i);
				if("component".equals(node.getNodeName())){
					BscComponentDef component = new BscComponentDef();
					Map<String, Object> params = new HashMap<String, Object>();
					component.setParams(params);
					component.setCode(getNodeAttrValue(node, "code"));
					component.setName(getNodeAttrValue(node, "name"));
					component.setDesc(getNodeAttrValue(node, "desc"));
					NodeList childNodes = node.getChildNodes();
					for(int j = 0, lenJ = childNodes.getLength(); j < lenJ; j++){
						Node childNode = childNodes.item(j);
						if("params".equals(childNode.getNodeName())){
							NodeList paramNodes = childNode.getChildNodes();
							for(int k = 0, lenK = paramNodes.getLength(); k < lenK; k++){
								Node paramNode = paramNodes.item(k);
								if("param".equals(paramNode.getNodeName())){
									Map<String, Object> paramMap = new HashMap<String, Object>();
									String code = getNodeAttrValue(paramNode, "code");
									params.put(code, paramMap);
									paramMap.put("code", code);
									paramMap.put("name", getNodeAttrValue(paramNode, "name"));
									paramMap.put("desc", getNodeAttrValue(paramNode, "desc"));
									paramMap.put("format", getNodeAttrValue(paramNode, "format"));
									paramMap.put("defaultValue", getNodeAttrValue(paramNode, "defaultValue"));
								}
							}
						}
					}
					list.add(component);
				}
			}
			return list;
		}catch(Exception e){
			throw new RuntimeException(e);
		}
	}
	
	private static String getNodeAttrValue(Node node, String name){
		NamedNodeMap attrMap = node.getAttributes();
		if(null == attrMap){
			return null;
		}
		Node attr = attrMap.getNamedItem(name);
		if(null == attr){
			return null;
		}
		return attr.getNodeValue();
	}
}
