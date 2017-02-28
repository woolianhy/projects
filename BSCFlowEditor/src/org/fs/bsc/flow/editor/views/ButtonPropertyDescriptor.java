package org.fs.bsc.flow.editor.views;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.DialogCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.views.properties.PropertyDescriptor;

@SuppressWarnings("rawtypes")
public class ButtonPropertyDescriptor extends PropertyDescriptor {
	
	private Class clazz;
	
	private Object content;

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public ButtonPropertyDescriptor(Object id, String displayName, Class clazz, Object content) {
		this(id, displayName);
		this.clazz = clazz;
		this.content = content;
	}
	
	public ButtonPropertyDescriptor(Object id, String displayName) {
		super(id, displayName);
		setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object element) {
				return "[点击编辑]";
			}
		});
	}
	
	@Override
	public CellEditor createPropertyEditor(Composite parent) {
//		ButtonCellEditor cellEditor = new ButtonCellEditor(parent, clazz);
		DialogCellEditor cellEditor = new DialogCellEditor(parent){
			private Label defaultLabel;
			
			@Override
			protected Control createContents(Composite cell) {
				defaultLabel = new Label(cell, 16384);
		        defaultLabel.setFont(cell.getFont());
		        defaultLabel.setBackground(cell.getBackground());
		        return defaultLabel;
			}
			
			@Override
			protected Object openDialogBox(Control control) {
				Shell shell = getControl().getDisplay().getActiveShell();
				ParamDialog dialog = new ParamDialog(shell);
				dialog.setContent(getValue());
				dialog.open();
				return dialog.getResultObj();
			}};
		return cellEditor;
	}

	static class ButtonCellEditor extends CellEditor{
		
		private Object value;
		
		private Class clazz;

		public Class getClazz() {
			return clazz;
		}

		public void setClazz(Class clazz) {
			this.clazz = clazz;
		}
		
		public ButtonCellEditor(Composite composite, Class clazz) {
			super(composite);
			this.clazz = clazz;
		}

		@Override
		protected Control createControl(Composite parent) {
			Composite composite = new Composite(parent ,SWT.NONE);
			composite.setLayout(new GridLayout(2, false));
			return composite;
		}

		@Override
		protected Object doGetValue() {
			return value;
		}

		@Override
		protected void doSetFocus() {
		}

		@Override
		protected void doSetValue(Object obj) {
			this.value = obj;
		}
		
		@Override
		public void activate() {
			super.activate();
			showDialog();
		}
		
		public void showDialog(){
			try{
				Shell shell = getControl().getDisplay().getActiveShell();
				ButtonDialog dialog = (ButtonDialog) getClazz().getConstructor(Shell.class).newInstance(shell);
				dialog.setContent(getValue());
				dialog.open();
			}catch(Exception e){
				throw new RuntimeException(e);
			}
		}
	}
}
