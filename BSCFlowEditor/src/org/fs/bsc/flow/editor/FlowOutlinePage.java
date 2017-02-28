package org.fs.bsc.flow.editor;

import org.eclipse.gef.EditDomain;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.SelectionSynchronizer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.fs.bsc.flow.editor.part.OutlinePartFactory;
import org.fs.bsc.flow.model.BscFlowDef;

public class FlowOutlinePage extends ContentOutlinePage {

	private EditDomain editDomain;

	private SelectionSynchronizer selectionSynchronizer;

	private BscFlowDef flow;

	private Control outline;

	public FlowOutlinePage(BscFlowDef flow, EditDomain editDomain,
			SelectionSynchronizer selectionSynchronizer) {
		super(new TreeViewer());
		this.flow = flow;
		this.editDomain = editDomain;
		this.selectionSynchronizer = selectionSynchronizer;
	}

	@Override
	public void createControl(Composite parent) {
		// super.createControl(parent);
		outline = getViewer().createControl(parent);
		getViewer().setEditDomain(editDomain);
		getViewer().setEditPartFactory(new OutlinePartFactory());
		getViewer().setContents(flow);
		selectionSynchronizer.addViewer(getViewer());
	}

	@Override
	public Control getControl() {
		return outline;
	}

	@Override
	public void dispose() {
		selectionSynchronizer.removeViewer(getViewer());
		super.dispose();
	}
}
