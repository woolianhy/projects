package org.fs.bsc.flow.editor.views;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.widgets.Shell;

public abstract class ButtonDialog extends Dialog {
	
	public ButtonDialog(Shell parentShell) {
		super(parentShell);
	}
	
	protected abstract void setContent(Object content);
}
