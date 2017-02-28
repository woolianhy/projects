package org.fs.bsc.flow.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.fs.bsc.def.BscFieldDef;
import org.fs.bsc.flow.model.BscFlowDef;

public class FlowParamTab {
	
	private String tabTitle;
	
	private CommandStack commandStack;
	
	private TabFolder tab;
	
	private BscFlowDef flow;
	
	protected static final int PARAM_TAB_TYPE_IN = 1;
	protected static final int PARAM_TAB_TYPE_OUT = 2;
	
	protected static final int BTN_TYPE_ADD = 1;
	protected static final int BTN_TYPE_DEL = 2;
	
	public FlowParamTab(String tabTitle, TabFolder tab, CommandStack commandStack, BscFlowDef flow) {
		this.tabTitle = tabTitle;
		this.tab = tab;
		this.commandStack = commandStack;
		this.flow = flow;
	}
	
	public Composite create(){
		Composite control = new Composite(tab, SWT.NONE);
		control.setLayout(new FillLayout());
		TabItem flowTab = new TabItem(tab, SWT.NONE);
		flowTab.setText(tabTitle);
		flowTab.setControl(control);
		TabFolder paramTab = new TabFolder(control, SWT.NONE);
		createParamTab(paramTab, PARAM_TAB_TYPE_IN, "输入");
		createParamTab(paramTab, PARAM_TAB_TYPE_OUT, "输出");
		return control;
	}
	
	public Composite createParamTab(TabFolder paramTab, int type, String tabTitle){
		Composite control = new Composite(paramTab, SWT.NONE);
		control.setLayout(new GridLayout());
		control.setLayoutData(new GridData(1808));
		TabItem flowTab = new TabItem(paramTab, SWT.NONE);
		flowTab.setText(tabTitle);
		flowTab.setControl(control);
		
		TreeViewer treeView = new TreeViewer(control, SWT.FULL_SELECTION|SWT.MULTI|SWT.BORDER);
		
		Tree tree = treeView.getTree();
		tree.setLayout(new GridLayout());
		tree.setLayoutData(new GridData(1808));
		tree.setHeaderVisible(true);
		tree.setLinesVisible(true);
		
		TreeColumn column1 = new TreeColumn(tree, SWT.NONE);
		column1.setWidth(150);
		column1.setText("字段代码");
		
		TreeColumn column2 = new TreeColumn(tree, SWT.NONE);
		column2.setWidth(250);
		column2.setText("字段名称");
		
		TreeColumn column3 = new TreeColumn(tree, SWT.NONE);
		column3.setWidth(200);
		column3.setText("字段描述");
		
		TreeColumn column4 = new TreeColumn(tree, SWT.NONE);
		column4.setWidth(150);
		column4.setText("字段格式");
		
		treeView.setContentProvider(new ParamContentProvider(type));
		treeView.setLabelProvider(new ParamLabelProvider());
		treeView.setColumnProperties(new String[]{"code", "name", "desc", "format"});
		treeView.setCellEditors(new CellEditor[] {
				new TextCellEditor(tree),
				new TextCellEditor(tree),
				new TextCellEditor(tree),
				new TextCellEditor(tree)
		});
		treeView.setCellModifier(new CellModifier(treeView, type, commandStack));
		treeView.setInput(flow);
		
		Composite buttonWrapper = new Composite(control, SWT.NONE);
		buttonWrapper.setLayout(new GridLayout());
		buttonWrapper.setLayout(new GridLayout(3, false));
		Button btnAdd = new Button(buttonWrapper, SWT.NONE);
		btnAdd.setText("新增");
		btnAdd.setData("type", BTN_TYPE_ADD);
		btnAdd.setData("paramType", type);
		btnAdd.addListener(13, new BtnListener(treeView, commandStack));
		
		Button btnDel = new Button(buttonWrapper, SWT.NONE);
		btnDel.setText("删除");
		btnDel.setData("type", BTN_TYPE_DEL);
		btnDel.setData("paramType", type);
		btnDel.addListener(13, new BtnListener(treeView, commandStack));
		
		return control;
	}
	
	static class ParamContentProvider implements ITreeContentProvider {
		
		private int type;
		
		public ParamContentProvider(int type) {
			this.type = type;
		}
		
		@Override
		public void inputChanged(Viewer arg0, Object arg1, Object arg2) {}
		@Override
		public void dispose() {}
		@Override
		public Object[] getChildren(Object arg0) {
			return getChildren(arg0);
		}
		@Override
		public Object[] getElements(Object content) {
			BscFlowDef flow = (BscFlowDef) content;
			List<BscFieldDef> list = null;
			if(PARAM_TAB_TYPE_IN == type){
				list = flow.getInputFields();
			}else if(PARAM_TAB_TYPE_OUT == type){
				list = flow.getOutputFields();
			}
			return null == list ? new Object[]{} : list.toArray();
		}
		@Override
		public Object getParent(Object arg0) {
			return null;
		}
		@Override
		public boolean hasChildren(Object content) {
			return false;
		}
	}
	
	static class ParamLabelProvider implements ITableLabelProvider {
		@Override
		public void removeListener(ILabelProviderListener ilabelproviderlistener) {}
		@Override
		public boolean isLabelProperty(Object obj, String s) {
			return false;
		}
		@Override
		public void dispose() {}
		@Override
		public void addListener(ILabelProviderListener ilabelproviderlistener) {}
		@Override
		public String getColumnText(Object obj, int i) {
			BscFieldDef field = (BscFieldDef) obj;
			if(0 == i){
				return field.getCode();
			}else if(1 == i){
				return field.getName();
			}else if(2 == i){
				return field.getDesc();
			}else if(3 == i){
				return field.getFormat();
			}
			return "";
		}
		@Override
		public Image getColumnImage(Object obj, int i) {
			if(0 == i){
				return Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/sample.gif").createImage();
			}
			return null;
		}
	}
	
	static class BtnListener implements Listener {
		private TreeViewer treeViewer;
		
		private CommandStack commandStack;
		
		public BtnListener(TreeViewer treeViewer, CommandStack commandStack) {
			this.treeViewer = treeViewer;
			this.commandStack = commandStack;
		}
		
		
		@Override
		public void handleEvent(Event event) {
			Button btn = (Button) event.widget;
			int type = (Integer) btn.getData("type");
			int paramType = (Integer) btn.getData("paramType");
			BscFlowDef flow = (BscFlowDef) treeViewer.getInput();
			if(BTN_TYPE_ADD == type){
				BscFieldDef field = new BscFieldDef();
				field.setCode("field" + System.currentTimeMillis());
				if(PARAM_TAB_TYPE_IN == paramType){
					if(null == flow.getInputFields()){
						flow.setInputFields(new ArrayList<BscFieldDef>());
					}
					flow.getInputFields().add(field);
				}else if(PARAM_TAB_TYPE_OUT == paramType){
					if(null == flow.getOutputFields()){
						flow.setOutputFields(new ArrayList<BscFieldDef>());
					}
					flow.getOutputFields().add(field);
				}
				treeViewer.refresh();
				commandStack.execute(new Command(){});
			}else if(BTN_TYPE_DEL == type){
				TreeSelection selection = (TreeSelection) treeViewer.getSelection();
				TreePath[] paths = selection.getPaths();
				if(paths != null && paths.length > 0){
					for(TreePath path : paths){
						BscFieldDef field = (BscFieldDef) path.getFirstSegment();
						if(PARAM_TAB_TYPE_IN == paramType){
							flow.getInputFields().remove(field);
						}else if(PARAM_TAB_TYPE_OUT == paramType){
							flow.getOutputFields().remove(field);
						}
					}
					treeViewer.refresh();
					commandStack.execute(new Command(){});
				}
			}
			
		}
	}
	
	static class CellModifier implements ICellModifier{
		
		private TreeViewer treeViewer;
		
		private int paramType;
		
		private CommandStack commandStack;
		
		public CellModifier(TreeViewer treeViewer, int paramType, CommandStack commandStack) {
			this.treeViewer = treeViewer;
			this.paramType = paramType;
			this.commandStack = commandStack;
		}

		@Override
		public boolean canModify(Object obj, String s) {
			return true;
		}

		@Override
		public Object getValue(Object obj, String prop) {
			String value = null;
			BscFieldDef field = (BscFieldDef) obj;
			if("code".equals(prop)){
				value = field.getCode();
			}else if("name".equals(prop)){
				value = field.getName();
			}else if("desc".equals(prop)){
				value = field.getDesc();
			}else if("format".equals(prop)){
				value = field.getFormat();
			}
			if(null == value){
				value = "";
			}
			return value;
		}

		@Override
		public void modify(Object obj, String prop, Object value) {
			TreeItem item = (TreeItem) obj;
			BscFieldDef field = (BscFieldDef) item.getData();
			String strValue = (String) value;
			if(null == strValue){
				strValue = "";
			}else
				strValue = strValue.trim();
			if("code".equals(prop)){
				if(null == strValue || strValue.equals("")){
					return;//FIXME
				}
				BscFlowDef flow = (BscFlowDef) treeViewer.getInput();
				if(PARAM_TAB_TYPE_IN == paramType){
					for(BscFieldDef tmpField : flow.getInputFields()){
						if(field != tmpField){
							if(strValue.equals(tmpField.getCode())){
								return;//FIXME
							}
						}
					}
				}else if(PARAM_TAB_TYPE_OUT == paramType){
					for(BscFieldDef tmpField : flow.getOutputFields()){
						if(field != tmpField){
							if(strValue.equals(tmpField.getCode())){
								return;//FIXME
							}
						}
					}
				}
				field.setCode(strValue);
			}else if("name".equals(prop)){
				field.setName(strValue);
			}else if("desc".equals(prop)){
				field.setDesc(strValue);
			}else if("format".equals(prop)){
				field.setFormat(strValue);
			}
			treeViewer.refresh();
			commandStack.execute(new Command(){});
		}
		
	}
}
