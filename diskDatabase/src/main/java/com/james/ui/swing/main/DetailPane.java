package com.james.ui.swing.main;

import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.james.ui.swing.node.BaseInfo;

public class DetailPane extends JPanel {

	private static final long serialVersionUID = 1L;

	private SpringLayout springLayout;

	public DetailPane() {
		springLayout = new SpringLayout();
		this.setLayout(springLayout);
	}

	public SpringLayout getSpringLayout() {
		return springLayout;
	}

	public void setSpringLayout(SpringLayout springLayout) {
		this.springLayout = springLayout;
	}

	public void show(BaseInfo info) {
		info.show(this);

		this.doLayout();
		repaint();
	}
}