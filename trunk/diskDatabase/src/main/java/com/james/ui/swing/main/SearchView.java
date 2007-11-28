package com.james.ui.swing.main;

import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.james.ui.swing.node.BaseNodeHelper;

public class SearchView extends JPanel {

	private static final long serialVersionUID = 1L;

	private SearchController controller;

	public SearchView(DetailPane detailPane, BaseNodeHelper baseNodeHelper) {
		setLayout(new GridLayout(2, 1));

		controller = new SearchController(detailPane, baseNodeHelper);

		add(controller.getFields());
		// add(fields, BorderLayout.NORTH);

		JScrollPane scrollingResults = new JScrollPane(controller.getResults());
		// add(scrollingResults, BorderLayout.CENTER);
		add(scrollingResults);
	}
}
