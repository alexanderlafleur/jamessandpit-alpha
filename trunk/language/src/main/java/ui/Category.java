package ui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;

import model.Choice;
import controller.AppController;

public abstract class Category extends JList implements ActionListener {
	private static final long serialVersionUID = 1L;

	private static final int UNFIXED_FONT_SIZE = 12;

	private static final int FIXED_FONT_SIZE = 24;

	private static final int DEFAULT_FONT_SIZE = 12;

	private ActionListener al;

	private AppController controller;

	private boolean fixed;

	public Category(AppController controller) {
		setController(controller);

		setFontSize(DEFAULT_FONT_SIZE);
		setVisibleRowCount(20);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		al = this;
		// mTextArea.setFont(new Font("Bitstream Cyberbit", Font.PLAIN, 28));

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				if (al == null) {
					return;
				}
				Object ob[] = getSelectedValues();
				if (ob.length > 1) {
					return;
				}
				if (me.getClickCount() == 2) {
					System.out
							.println("Sending ACTION_PERFORMED to ActionListener");
					al.actionPerformed(new ActionEvent(this,
							ActionEvent.ACTION_PERFORMED, ob[0].toString()));
					me.consume();
				}
			}
		});
	}

	public void actionPerformed(ActionEvent e) {
		getController().rate();
	}

	public void add(Choice choice) {
		if (!isFixed() || (getModel().getSize() == 0)) {
			addData(getChoiceValue(choice));
			setSelectedIndex(0);
		}
	}

	public void addActionListener(ActionListener al) {
		this.al = al;
	}

	public void addData(Object[] objects) {
		for (Object element : objects) {
			addData((String) element);
		}
	}

	public void addData(String data) {
		if (!isFixed() || (getModel().getSize() == 0)) {

			ListModel model = getModel();
			removeAll();

			List<String> list = toList(model);

			list.add(data);

			setListData(list.toArray());

		} else {
			return;
		}
	}

	public void fix() {
		setFontSize(FIXED_FONT_SIZE);
		fixed = true;
	}

	public abstract String getChoiceValue(Choice choice);

	public AppController getController() {
		return controller;
	}

	public boolean isFixed() {
		return fixed;
	}

	public void reset() {
		removeAll();

		setListData(new Vector());
	}

	private void reset(Object[] objects) {
		reset();

		addData(objects);
	}

	public void setController(AppController controller) {
		this.controller = controller;
	}

	public void setFontSize(int newSize) {
		setFont(new Font("MS-PMincho", Font.PLAIN, newSize));
	}

	public abstract void shuffle();

	public void sort() {
		List<String> strings = toList(getModel());

		Collections.sort(strings);

		reset(strings.toArray());
	}

	protected List<String> toList(ListModel model) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < model.getSize(); i++) {
			list.add((String)model.getElementAt(i));
		}

		return list;
	}

	public void unfix() {
		setFontSize(UNFIXED_FONT_SIZE);
		fixed = false;
	}

	public void valueChanged(ListSelectionEvent e) {
		// Object src =
		e.getSource();

	}

}
