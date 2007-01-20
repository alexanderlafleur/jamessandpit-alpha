package com.james.ui.swing.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import com.james.ui.swing.node.FormField;
import com.james.ui.swing.node.LayoutHelper;

public class SearchFields extends JPanel {

	private static final long serialVersionUID = 1L;

	private List buttons = new ArrayList();

	private LayoutHelper layoutHelper = new LayoutHelper();

	private FormField nameField = new FormField("Name", "");

	private SpringLayout springLayout = new SpringLayout();

	public SearchFields(SearchController searchController) {
		setLayout(springLayout);

		JButton searchButton = new JButton(SearchController.SEARCH_COMMAND);
		JButton mp3SearchButton = new JButton(
				SearchController.SEARCH_MP3_COMMAND);
		JButton fileSearchButton = new JButton(
				SearchController.SEARCH_FILE_COMMAND);

		searchButton.addActionListener(searchController);
		mp3SearchButton.addActionListener(searchController);
		fileSearchButton.addActionListener(searchController);

		buttons.add(searchButton);
		buttons.add(mp3SearchButton);
		buttons.add(fileSearchButton);

		layoutFields();
	}

	public LayoutHelper getLayoutHelper() {
		return layoutHelper;
	}

	public FormField getNameField() {
		return nameField;
	}

	public SpringLayout getSpringLayout() {
		return springLayout;
	}

	private void layoutFields() {
		JComponent predecessor = null;

		predecessor = getLayoutHelper().layoutField(nameField.getLabel(),
				nameField.getField(), predecessor, this);

		for (Iterator i = buttons.iterator(); i.hasNext();) {
			JButton button = (JButton) i.next();

			predecessor = getLayoutHelper().layoutButton(button, predecessor,
					this);

		}
	}

	public void setLayoutHelper(LayoutHelper layoutHelper) {
		this.layoutHelper = layoutHelper;
	}

	public void setNameField(FormField nameField) {
		this.nameField = nameField;
	}

	public void setSpringLayout(SpringLayout springLayout) {
		this.springLayout = springLayout;
	}
}
