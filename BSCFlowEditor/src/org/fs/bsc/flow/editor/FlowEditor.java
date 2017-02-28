package org.fs.bsc.flow.editor;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editparts.ScalableFreeformRootEditPart;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.requests.SimpleFactory;
import org.eclipse.gef.ui.parts.GraphicalEditorWithPalette;
import org.eclipse.gef.ui.parts.GraphicalViewerKeyHandler;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.fs.bsc.flow.XmlBscFlowTransformer;
import org.fs.bsc.flow.editor.action.MenuProvider;
import org.fs.bsc.flow.editor.helper.ComponentHelper;
import org.fs.bsc.flow.editor.part.PartFactory;
import org.fs.bsc.flow.model.BscComponentDef;
import org.fs.bsc.flow.model.BscFlowActionDef;
import org.fs.bsc.flow.model.BscFlowActionDefFactory;
import org.fs.bsc.flow.model.BscFlowDef;
import org.fs.bsc.flow.model.BscFlowEndActionDef;
import org.fs.bsc.flow.model.BscFlowStartActionDef;

public class FlowEditor extends GraphicalEditorWithPalette {
	
	protected static MessageConsole console;
	
	private String projectName;
	
	private PaletteRoot paletteRoot;
	
	private PaletteDrawer customActionGroup;
	
	private BscFlowDef flowDef;
	
	private KeyHandler sharedKeyHandler;
	
	private IFile file;
	
	static{
		console = new MessageConsole("BSC Editor console", Activator.imageDescriptorFromPlugin(Activator.PLUGIN_ID, "/icons/sample.gif"));
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{console});
		System.setOut(new PrintStream(console.newMessageStream()));
	}

	public FlowEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}
	
	@Override
	public void createPartControl(Composite parent) {
		TabFolder tab = new TabFolder(parent, SWT.NONE);
		
		createInfoTab(tab);
		
		FlowParamTab paramTab = new FlowParamTab("参数配置", tab, getCommandStack(), flowDef);
		paramTab.create();
		
		Composite flowControl = new Composite(tab, SWT.NONE);
		flowControl.setLayout(new FillLayout());
		TabItem flowTab = new TabItem(tab, SWT.NONE);
		flowTab.setText("流程设计");
		flowTab.setControl(flowControl);
		super.createPartControl(flowControl);
		
		createSourceTab(tab);
		
		tab.setSelection(flowTab);
		
//		firePropertyChange(257);
	}
	
	protected Composite createInfoTab(TabFolder tab){
		Composite infoControl = new Composite(tab, SWT.NONE);
		infoControl.setLayout(new GridLayout(2, false));
		TabItem infoTab = new TabItem(tab, SWT.NONE);
		infoTab.setText("流程信息");
		infoTab.setControl(infoControl);
		Label codeLabel = new Label(infoControl, SWT.NONE);
		codeLabel.setText("流程标识");
		Text codeText = new Text(infoControl, SWT.BORDER|SWT.READ_ONLY);
		codeText.setText(flowDef.getCode());
		codeText.setLayoutData(new GridData(400, 20));
		Label nameLabel = new Label(infoControl, SWT.NONE);
		nameLabel.setText("流程名称");
		Text nameText = new Text(infoControl, SWT.BORDER);
		nameText.setLayoutData(new GridData(400, 20));
		nameText.setText(flowDef.getName());
		nameText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				flowDef.setName(((Text) event.getSource()).getText());
				getCommandStack().execute(new Command() {});
			}
		});
		Label descLabel = new Label(infoControl, SWT.NONE);
		descLabel.setText("流程描述");
		Text descText = new Text(infoControl,
				SWT.BORDER|SWT.MULTI|SWT.V_SCROLL|SWT.WRAP);
		descText.setLayoutData(new GridData(400, 200));
		descText.setText(flowDef.getDesc());
		descText.addModifyListener(new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent event) {
				flowDef.setDesc(((Text) event.getSource()).getText());
				getCommandStack().execute(new Command() {});
			}
		});
		return infoControl;
	}
	
	protected Composite createSourceTab(TabFolder tab){
		Composite sourceControl = new Composite(tab, SWT.NONE);
		sourceControl.setLayout(new FillLayout());
		TabItem sourceTab = new TabItem(tab, SWT.NONE);
		sourceTab.setText("代码预览");
		sourceTab.setControl(sourceControl);
		final Text sourceText = new Text(sourceControl,
				SWT.BORDER|SWT.WRAP|SWT.MULTI|SWT.V_SCROLL|SWT.H_SCROLL|SWT.READ_ONLY);
		tab.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent selectionevent) {
				TabItem item = (TabItem) selectionevent.item;
				if("代码预览".equals(item.getText())){
					ByteArrayOutputStream out = new ByteArrayOutputStream();
		    		XmlBscFlowTransformer.toXML(flowDef, out);
					try {
						sourceText.setText(new String(out.toByteArray(), "UTF-8"));
					} catch (UnsupportedEncodingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		return sourceControl;
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		synchronized(this){
			if (null == paletteRoot) {
				paletteRoot = new PaletteRoot();
				PaletteGroup ctrlGroup = new PaletteGroup("控制");
				ToolEntry selectionEntry = new SelectionToolEntry();
				selectionEntry.setLabel("选择");
				ctrlGroup.add(selectionEntry);
				ctrlGroup.add(new ConnectionCreationToolEntry("连接", "", null, null, null));
				paletteRoot.add(ctrlGroup);
				
				PaletteDrawer baseActionGroup = new PaletteDrawer("基础组件");
				paletteRoot.add(baseActionGroup);
				baseActionGroup.add(new CombinedTemplateCreationEntry("开始", "",
						BscFlowStartActionDef.class, new SimpleFactory(BscFlowStartActionDef.class), null, null));
				baseActionGroup.add(new CombinedTemplateCreationEntry("结束", "",
						BscFlowEndActionDef.class, new SimpleFactory(BscFlowEndActionDef.class), null, null));
				
				BscComponentDef callComponent = new BscComponentDef();
				callComponent.setCode("callComponent");
				callComponent.setName("调用组件");
				baseActionGroup.add(new CombinedTemplateCreationEntry("调用组件", "",
						BscFlowEndActionDef.class, new BscFlowActionDefFactory(callComponent, BscFlowActionDef.ACTION_TYPE_CALL), null, null));
			}
			return paletteRoot;
		}
	}

	@Override
	protected void initializeGraphicalViewer() {
		getGraphicalViewer().setContents(flowDef);
		// getGraphicalViewer().addDropTargetListener(new
		// TemplateTransferDropTargetListener(getGraphicalViewer()));

	}
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		
//		IFile file =((IFileEditorInput) getEditorInput()).getFile();
//    	File currentFile = file.getLocation().toFile();
    	try{
    		ByteArrayOutputStream out = new ByteArrayOutputStream();
    		XmlBscFlowTransformer.toXML(flowDef, out);
    		file.setContents(new ByteArrayInputStream(out.toByteArray()), true, false, monitor);
    	}catch(Exception e){
    		throw new RuntimeException(e);
    	}
    	getCommandStack().markSaveLocation();
    	setPartName(file.getName());
	}
	
	@Override
	protected void setInput(IEditorInput input) {
		super.setInput(input);
		file = ((IFileEditorInput) input).getFile();
		projectName = file.getProject().getName();
		try {
			flowDef = XmlBscFlowTransformer.toBscFlow(file.getContents(false));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		System.out.println("[FlowEditor:open] " + flowDef);//FIXME
		setPartName(file.getName());
		
		synchronized(this){
			if(null == customActionGroup){
				PaletteDrawer actionGroup = new PaletteDrawer("扩展组件");
				InputStream in;
				try {
//					String projectName = ((IResource) ((StructuredSelection) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection()).getFirstElement()).getProject().getName();
					in = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName)
							.getFile(BscFlowEditorInitParams.getParam("BSC_COMPONENT_FILE_PATH ", "/src/bsc_components.xml")).getContents();
				} catch (CoreException e) {
					throw new RuntimeException(e);
				}
				ComponentHelper.loadComponent(in);
				List<BscComponentDef> componentList =
						ComponentHelper.getComponentList();
				for(BscComponentDef component: componentList){
					BscFlowActionDefFactory factory = new BscFlowActionDefFactory(component);
					String name = component.getName();
					if(null == name || "".equals(name.trim())){
						name = component.getCode();
					}
					actionGroup.add(new CombinedTemplateCreationEntry(name,
							component.getDesc(), BscFlowActionDef.class, factory, null, null));
				}
				customActionGroup = actionGroup;
				paletteRoot.add(actionGroup);
				System.out.println("[FlowEditor:load custom components]");//FIXME
			}
		}
	}
	
	@Override
	protected void initializePaletteViewer() {
		// TODO Auto-generated method stub
		super.initializePaletteViewer();
	}
	
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		getGraphicalViewer()
				.setRootEditPart(new ScalableFreeformRootEditPart());
		getGraphicalViewer().setEditPartFactory(new PartFactory());

		GraphicalViewer viewer = getGraphicalViewer();
		KeyHandler keyHandler = new GraphicalViewerKeyHandler(getGraphicalViewer());
		keyHandler.setParent(getCommonKeyHandler());
		getGraphicalViewer().setKeyHandler(keyHandler);

		ContextMenuProvider menuProvider = new MenuProvider(viewer,
				getActionRegistry());
		viewer.setContextMenu(menuProvider);
		getSite().registerContextMenu(menuProvider, viewer);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Object getAdapter(Class type) {
		if(type.isAssignableFrom(IContentOutlinePage.class)){
			return new FlowOutlinePage(flowDef, getEditDomain(), getSelectionSynchronizer());
		}
		return super.getAdapter(type);
	}
	
	@Override
	public void commandStackChanged(EventObject event) {
		super.commandStackChanged(event);
		
		if(isDirty() && !getPartName().startsWith("*")){
			setPartName("*" + file.getName());
		}
		firePropertyChange(257);
	}
	
	@Override
	protected void createActions() {
		super.createActions();
//		ActionRegistry registry = getActionRegistry();
		
		//TODO

//		IAction action = new OpenImplClassAction(this);
//		registry.registerAction(action);
//		getSelectionActions().add(OpenImplClassAction.ACTIONID);
	}
	
	protected synchronized KeyHandler getCommonKeyHandler() {
		if (sharedKeyHandler == null) {
			sharedKeyHandler = new KeyHandler();
			sharedKeyHandler.put(KeyStroke.getPressed('', 127, 0),
					getActionRegistry().getAction(ActionFactory.DELETE.getId()));// DEL
			sharedKeyHandler.put(KeyStroke.getPressed('\032', 122, SWT.CTRL),
					getActionRegistry().getAction(ActionFactory.UNDO.getId()));// CTRL+z
			sharedKeyHandler.put(KeyStroke.getPressed('\031', 121, SWT.CTRL),
					getActionRegistry().getAction(ActionFactory.REDO.getId()));// CTRL+y
			sharedKeyHandler.put(KeyStroke.getPressed('\001', 97, SWT.CTRL),
					getActionRegistry().getAction(ActionFactory.SELECT_ALL.getId()));// CTRL+a
		}
		return sharedKeyHandler;
	}
}
