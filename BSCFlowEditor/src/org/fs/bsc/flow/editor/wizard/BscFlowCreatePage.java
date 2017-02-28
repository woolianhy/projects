package org.fs.bsc.flow.editor.wizard;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;
import org.fs.bsc.flow.XmlBscFlowTransformer;
import org.fs.bsc.flow.model.BscFlowDef;

public class BscFlowCreatePage extends WizardNewFileCreationPage {
	
	private static final String DEFAULT_EXTENSION = "bsc";
	
	private IWorkbench workbench;

	public BscFlowCreatePage(IWorkbench workbench, IStructuredSelection selection) {
	    super("newBlFile", selection);
	    this.workbench = workbench;

	    setTitle("创建" + DEFAULT_EXTENSION + "文件");
	    setDescription("创建一个新的" + DEFAULT_EXTENSION + "文件");
	    setFileName("newBsc" + System.currentTimeMillis() + "." + DEFAULT_EXTENSION);
	  }

	@Override
	protected InputStream getInitialContents() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		BscFlowDef flow = new BscFlowDef();
		String fileName = getFileName();
		fileName = fileName.substring(0, fileName.lastIndexOf("." + DEFAULT_EXTENSION));
		flow.setCode(fileName);
		XmlBscFlowTransformer.toXML(flow, out);
		return new ByteArrayInputStream(out.toByteArray());
	}

	public boolean finish() {
		IFile file = createNewFile();
		IWorkbenchPage currPage = workbench.getActiveWorkbenchWindow().getActivePage();
		try {
			IDE.openEditor(currPage, file);
			return true;
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	
}
