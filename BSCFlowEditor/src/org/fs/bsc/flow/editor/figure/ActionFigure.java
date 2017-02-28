package org.fs.bsc.flow.editor.figure;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Image;

public class ActionFigure extends RectangleFigure {
	private Label label;
	
	public ActionFigure() {
		label = new Label();
		label.setIcon(new Image(null, this.getClass().getResourceAsStream("/icons/sample.gif")));
		add(label);
	}
	
	public String getText(){
		return label.getText();
	}
	
	public void setText(String text){
		label.setText(text);
	}
	
	@Override
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		label.setBounds(rect);
	}
}
