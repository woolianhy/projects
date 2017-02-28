package org.fs.bsc.flow.editor.wizard;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;

public class BscFlowNewWizard extends Wizard implements INewWizard{
	
	private BscFlowCreatePage page;
	
	@Override
	public void addPages() {
		addPage(page);
	}
	
	@Override
	public boolean performFinish() {
		return page.finish();
	}

	@Override
	public void init(IWorkbench iworkbench,
			IStructuredSelection istructuredselection) {
		setWindowTitle("创建 BSC 文件");
		page = new BscFlowCreatePage(iworkbench, istructuredselection);
	}
}
