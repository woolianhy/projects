package org.fs.bsc.loader;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fs.bsc.component.BscComponent;
import org.fs.bsc.exception.BscException;
import org.fs.bsc.exec.BscComponentProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlBscComponentLoader implements BscComponentLoader {
	
	private String configFilePath;
	
	public XmlBscComponentLoader() {
		configFilePath = "/bsc_components.xml";
	}
	
	public String getConfigFilePath() {
		return configFilePath;
	}

	public void setConfigFilePath(String configFilePath) {
		this.configFilePath = configFilePath;
	}

	@Override
	public void loadComponents(BscComponentProvider provider) {
		try{
			InputStream in = getClass().getResourceAsStream(configFilePath);
			DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document doc = docBuilder.parse(in);
			Element root = doc.getDocumentElement();
			NodeList nodes = root.getChildNodes();
			for(int i = 0, len = nodes.getLength(); i < len; i++){
				Node node = nodes.item(i);
				if("component".equals(node.getNodeName())){
					String code = getNodeAttrValue(node, "code");
					String cls = getNodeAttrValue(node, "class");
					Object inst = Class.forName(cls).newInstance();
					if(inst instanceof BscComponent){
						BscComponent component = (BscComponent) inst;
						component.setCode(code);
						provider.addComponent(code, component);
					}else{
						throw new BscException("Class [" + cls + "] is not extends BscComponent !");
					}
				}
			}
		}catch(Exception e){
			throw new BscException(e);
		}
	}
	
	private String getNodeAttrValue(Node node, String name){
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
