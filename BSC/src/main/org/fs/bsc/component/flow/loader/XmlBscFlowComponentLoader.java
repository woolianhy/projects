package org.fs.bsc.component.flow.loader;

import java.io.File;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.fs.bsc.component.flow.BscFlow;
import org.fs.bsc.component.flow.BscFlowAction;
import org.fs.bsc.component.flow.BscFlowComponent;
import org.fs.bsc.component.flow.BscFlowDirection;
import org.fs.bsc.component.flow.BscFlowEndAction;
import org.fs.bsc.component.flow.BscFlowResolver;
import org.fs.bsc.component.flow.BscFlowStartAction;
import org.fs.bsc.def.BscFieldDef;
import org.fs.bsc.def.BscFieldRelDef;
import org.fs.bsc.exception.BscException;
import org.fs.bsc.exec.BscComponentProvider;
import org.fs.bsc.loader.BscComponentLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlBscFlowComponentLoader implements BscComponentLoader {
	
	public static final String DEFAULT_SCAN_PATH;
	
	private DirectionComparator directionComparator = new DirectionComparator();
	
	static{
		try{
			DEFAULT_SCAN_PATH = URLDecoder.decode(XmlBscFlowComponentLoader.class.getResource("/").getPath(), "UTF-8") + "/flows";
		}catch(Exception e){
			throw new BscException("[XmlBscFlowComponentLoader] fail to init defautl path !", e);
		}
	}
	
	private BscFlowResolver flowResolver;
	
	private DocumentBuilder docBuilder;
	
	private String scanPath = DEFAULT_SCAN_PATH;
	
	private String scanSuffix = ".bsc";
	
	@Override
	public void loadComponents(BscComponentProvider provider) {
		try{
			if(null == docBuilder){
				docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			}
			File dir = new File(scanPath);
			if(null == dir || !dir.isDirectory()){
				throw new RuntimeException("component dir[" + scanPath + "] not found");
			}
			loadComponentsByDir(dir, provider);
		}catch(Exception e){
			throw new BscException("加载BSC流程组件异常", e);
		}
	}
	
	private void loadComponentsByDir(File dir, BscComponentProvider provider) throws Exception{
		for(File file: dir.listFiles()){
			if(file.isDirectory()){
				loadComponentsByDir(file, provider);
			}else if(file.isFile()){
				try{
					loadComponentByFile(file, provider);
				}catch(Exception e){
					throw new BscException("加载XML异常：" + file.getAbsolutePath(), e);
				}
			}
		}
	}
	
	private void loadComponentByFile(File file, BscComponentProvider provider) throws Exception{
		if(!file.getName().endsWith(scanSuffix)){
			return;
		}
		Document doc = docBuilder.parse(file);
		Element root = doc.getDocumentElement();
		BscFlow flow = new BscFlow();
		flow.setCode(root.getAttribute("code"));
		flow.setName(root.getAttribute("name"));
		flow.setDesc(root.getAttribute("desc"));
		BscFlowComponent component = new BscFlowComponent();
		List<BscFieldDef> inputFields = new ArrayList<BscFieldDef>();
		List<BscFieldDef> outputFields = new ArrayList<BscFieldDef>();
		component.setCode(flow.getCode());
		component.setName(flow.getName());
		component.setDesc(flow.getDesc());
		component.setFlowResolver(flowResolver);
		component.setFlow(flow);
		component.setInputFields(inputFields);
		component.setOutputFields(outputFields);
		BscFlowStartAction startAction = new BscFlowStartAction();
		List<BscFlowEndAction> endActions = new ArrayList<BscFlowEndAction>();
		List<BscFlowAction> actions = new ArrayList<BscFlowAction>();
		flow.setStartAction(startAction);
		flow.setEndActions(endActions);
		flow.setActions(actions);
		NodeList nodes = root.getChildNodes();
		for(int i = 0, len = nodes.getLength(); i < len; i++){
			Node node = nodes.item(i);
			if("startAction".equals(node.getNodeName())){
				if(startAction.getCode() != null){
					throw new BscException("duplicate startAction");
				}
				setAction(node, startAction);
			}else if("endActions".equals(node.getNodeName())){
				NodeList childNodes = node.getChildNodes();
				for(int j = 0, lenJ = childNodes.getLength(); j < lenJ; j++){
					Node childNode = childNodes.item(j);
					if(Node.TEXT_NODE == childNode.getNodeType()){
						continue;
					}
					if("endAction".equals(childNode.getNodeName())){
						BscFlowEndAction endAction = new BscFlowEndAction();
						endActions.add(endAction);
						setAction(childNode, endAction);
					}
				}
			}else if("actions".equals(node.getNodeName())){
				NodeList childNodes = node.getChildNodes();
				for(int j = 0, lenJ = childNodes.getLength(); j < lenJ; j++){
					Node childNode = childNodes.item(j);
					if(Node.TEXT_NODE == childNode.getNodeType()){
						continue;
					}
					if("action".equals(childNode.getNodeName())){
						BscFlowAction action = new BscFlowAction();
						actions.add(action);
						setAction(childNode, action);
					}
				}
			}else if("inputFields".equals(node.getNodeName())){
				NodeList childNodes = node.getChildNodes();
				for(int j = 0, lenJ = childNodes.getLength(); j < lenJ; j++){
					Node childNode = childNodes.item(j);
					if(Node.TEXT_NODE == childNode.getNodeType()){
						continue;
					}
					if("field".equals(childNode.getNodeName())){
						BscFieldDef field = new BscFieldDef();
						field.setCode(getNodeAttrValue(childNode, "code"));
						field.setName(getNodeAttrValue(childNode, "name"));
						field.setDesc(getNodeAttrValue(childNode, "desc"));
						field.setFormat(getNodeAttrValue(childNode, "format"));
						inputFields.add(field);
					}
				}
			}else if("outputFields".equals(node.getNodeName())){
				NodeList childNodes = node.getChildNodes();
				for(int j = 0, lenJ = childNodes.getLength(); j < lenJ; j++){
					Node childNode = childNodes.item(j);
					if(Node.TEXT_NODE == childNode.getNodeType()){
						continue;
					}
					if("field".equals(childNode.getNodeName())){
						BscFieldDef field = new BscFieldDef();
						field.setCode(getNodeAttrValue(childNode, "code"));
						field.setName(getNodeAttrValue(childNode, "name"));
						field.setDesc(getNodeAttrValue(childNode, "desc"));
						field.setFormat(getNodeAttrValue(childNode, "format"));
						outputFields.add(field);
					}
				}
			}
		}
		provider.addComponent(component.getCode(), component);
	}
	
	private void setAction(Node node, BscFlowAction action){
		action.setCode(getNodeAttrValue(node, "code"));
		action.setName(getNodeAttrValue(node, "name"));
		action.setDesc(getNodeAttrValue(node, "desc"));
		action.setComponentCode(getNodeAttrValue(node, "componentCode"));
		
		NodeList childNodes = node.getChildNodes();
		for(int i = 0, len = childNodes.getLength(); i < len; i++){
			Node childNode = childNodes.item(i);
			if("directions".equals(childNode.getNodeName())){
				List<BscFlowDirection> directions = new ArrayList<BscFlowDirection>();
				action.setDirections(directions);
				NodeList directionNodes = childNode.getChildNodes();
				for(int j = 0, lenJ = directionNodes.getLength(); j < lenJ; j++){
					Node directionNode = directionNodes.item(j);
					if(Node.TEXT_NODE == directionNode.getNodeType()){
						continue;
					}
					if("direction".equals(directionNode.getNodeName())){
						BscFlowDirection direction = new BscFlowDirection();
						directions.add(direction);
						direction.setActionCode(getNodeAttrValue(directionNode, "actionCode"));
						direction.setExpression(getNodeAttrValue(directionNode, "condition"));
					}
				}
				Collections.sort(directions, directionComparator);
			}else if("inputs".equals(childNode.getNodeName())){
				List<BscFieldRelDef> inputs = new ArrayList<BscFieldRelDef>();
				action.setInputs(inputs);
				NodeList inputNodes = childNode.getChildNodes();
				for(int j = 0, lenJ = inputNodes.getLength(); j < lenJ; j++){
					Node inputNode = inputNodes.item(j);
					if(Node.TEXT_NODE == inputNode.getNodeType()){
						continue;
					}
					if("fieldRel".equals(inputNode.getNodeName())){
						BscFieldRelDef fieldRel = new BscFieldRelDef();
						fieldRel.setFrom(getNodeAttrValue(inputNode, "from"));
						fieldRel.setTo(getNodeAttrValue(inputNode, "to"));
						inputs.add(fieldRel);
					}
				}
			}else if("outputs".equals(childNode.getNodeName())){
				List<BscFieldRelDef> outputs = new ArrayList<BscFieldRelDef>();
				action.setOutputs(outputs);
				NodeList outputNodes = childNode.getChildNodes();
				for(int j = 0, lenJ = outputNodes.getLength(); j < lenJ; j++){
					Node outputNode = outputNodes.item(j);
					if(Node.TEXT_NODE == outputNode.getNodeType()){
						continue;
					}
					if("fieldRel".equals(outputNode.getNodeName())){
						BscFieldRelDef fieldRel = new BscFieldRelDef();
						fieldRel.setFrom(getNodeAttrValue(outputNode, "from"));
						fieldRel.setTo(getNodeAttrValue(outputNode, "to"));
						outputs.add(fieldRel);
					}
				}
			}else if("params".equals(childNode.getNodeName())){
				Map<String, Object> params = new HashMap<String, Object>();
				action.setParams(params);
				NodeList paramNodes = childNode.getChildNodes();
				for(int j = 0, lenJ = paramNodes.getLength(); j < lenJ; j++){
					Node paramNode = paramNodes.item(j);
					if(Node.TEXT_NODE == paramNode.getNodeType()){
						continue;
					}
					if("param".equals(paramNode.getNodeName())){
						params.put(getNodeAttrValue(paramNode, "name"),
								getNodeAttrValue(paramNode, "value"));
					}
				}
			}
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

	public void setFlowResolver(BscFlowResolver flowResolver) {
		this.flowResolver = flowResolver;
	}
	
	public void setScanPath(String scanPath) {
		this.scanPath = scanPath;
	}
	
	public void setScanSuffix(String scanSuffix) {
		this.scanSuffix = scanSuffix;
	}
}
