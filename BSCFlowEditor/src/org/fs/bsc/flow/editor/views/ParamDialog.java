package org.fs.bsc.flow.editor.views;

import java.util.ArrayList;
import java.util.List;

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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.fs.bsc.def.BscFieldRelDef;
import org.fs.bsc.flow.editor.Activator;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowActionDef.BscFlowActionDefWrapper;

public  class ParamDialog extends ButtonDialog{
	
	protected static final int PARAM_TAB_TYPE_IN = 1;
	protected static final int PARAM_TAB_TYPE_OUT = 2;
	
	protected static final int BTN_TYPE_ADD = 1;
	protected static final int BTN_TYPE_DEL = 2;
	
	private BscFlowActionDef action;
	
	private List<BscFieldRelDef> actionInputs;
	
	private List<BscFieldRelDef> actionOutputs;
	
	private CommandStack commandStack;
	
	private Object resultObj;

	public ParamDialog(Shell parentShell) {
		super(parentShell);
	}
	
	public Object getResultObj() {
		return resultObj;
	}

	public void setResultObj(Object resultObj) {
		this.resultObj = resultObj;
	}


	@Override
	protected Point getInitialSize() {
		return new Point(500, 400);
	}
	
	@Override
	protected boolean isResizable() {
		return true;
	}
	
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, 0, "确定", true);
		createButton(parent, 1, "取消", false);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
//		return super.createDialogArea(parent);
//		Composite control = new Composite(parent, SWT.NONE);
		Composite control = (Composite) super.createDialogArea(parent);
		control.setLayout(new FillLayout());
		TabFolder paramTab = new TabFolder(control, SWT.NONE);
		createParamTab(paramTab, PARAM_TAB_TYPE_IN, "输入");
		createParamTab(paramTab, PARAM_TAB_TYPE_OUT, "输出");
		control.getShell().setText("输入输出配置");
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
		column1.setText("来源字段");
		
		TreeColumn column2 = new TreeColumn(tree, SWT.NONE);
		column2.setWidth(150);
		column2.setText("目标字段");
		
		treeView.setContentProvider(new ParamContentProvider(type));
		treeView.setLabelProvider(new ParamLabelProvider());
		treeView.setColumnProperties(new String[]{"from", "to"});
		treeView.setCellEditors(new CellEditor[] {
				new TextCellEditor(tree),
				new TextCellEditor(tree)
		});
		treeView.setCellModifier(new CellModifier(treeView, type, commandStack));
		if(PARAM_TAB_TYPE_IN == type){
			treeView.setInput(actionInputs);
		}else if(PARAM_TAB_TYPE_OUT == type){
			treeView.setInput(actionOutputs);
		}
		
		Composite buttonWrapper = new Composite(control, SWT.NONE);
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

	@Override
	protected void setContent(Object content) {
		action = ((BscFlowActionDefWrapper) content).action;
		actionInputs = new ArrayList<BscFieldRelDef>();
		actionOutputs = new ArrayList<BscFieldRelDef>();
		if(action.getInputs() != null){
			actionInputs.addAll(action.getInputs());
		}
		if(action.getOutputs() != null){
			actionOutputs.addAll(action.getOutputs());
		}
	}
	
	@Override
	protected void okPressed() {
		action.setInputs(actionInputs);
		action.setOutputs(actionOutputs);
		resultObj = "ok";
		super.okPressed();
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
			List<BscFieldRelDef> list = (List<BscFieldRelDef>) content;
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
			BscFieldRelDef field = (BscFieldRelDef) obj;
			if(0 == i){
				return field.getFrom();
			}else if(1 == i){
				return field.getTo();
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
//			int paramType = (Integer) btn.getData("paramType");
			List<BscFieldRelDef> list = (List<BscFieldRelDef>) treeViewer.getInput();
			if(BTN_TYPE_ADD == type){
				BscFieldRelDef field = new BscFieldRelDef();
				field.setFrom("field" + System.currentTimeMillis());
				list.add(field);
				treeViewer.refresh();
//				commandStack.execute(new Command(){});
			}else if(BTN_TYPE_DEL == type){
				TreeSelection selection = (TreeSelection) treeViewer.getSelection();
				TreePath[] paths = selection.getPaths();
				if(paths != null && paths.length > 0){
					for(TreePath path : paths){
						BscFieldRelDef field = (BscFieldRelDef) path.getFirstSegment();
						list.remove(field);
					}
					treeViewer.refresh();
//					commandStack.execute(new Command(){});
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
			BscFieldRelDef field = (BscFieldRelDef) obj;
			if("from".equals(prop)){
				value = field.getFrom();
			}else if("to".equals(prop)){
				value = field.getTo();
			}
			if(null == value){
				value = "";
			}
			return value;
		}

		@Override
		public void modify(Object obj, String prop, Object value) {
			TreeItem item = (TreeItem) obj;
			BscFieldRelDef field = (BscFieldRelDef) item.getData();
			String strValue = (String) value;
			if(null == strValue){
				strValue = "";
			}else
				strValue = strValue.trim();
			if("from".equals(prop)){
				if(null == strValue || strValue.equals("")){
					return;//FIXME
				}
				field.setFrom(strValue);
			}else if("to".equals(prop)){
				field.setTo(strValue);
			}
			treeViewer.refresh();
//			commandStack.execute(new Command(){});
		}
		
	}
}