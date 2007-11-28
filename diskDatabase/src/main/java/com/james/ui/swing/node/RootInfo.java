package com.james.ui.swing.node;

import java.util.Iterator;
import java.util.List;

import javax.swing.JComponent;

import com.james.ui.swing.main.DetailPane;

public class RootInfo extends BaseInfo {

	private static final long serialVersionUID = 1L;

	public RootInfo(String id, String label, BaseNodeHelper helper) {
		super(id, label, helper);
	}

	public void createButtons() {

	}

	public List loadChildren() {
		List wallets = getHelper().loadWallets();

		return wallets;
	}

	public void show(DetailPane detail) {
		detail.removeAll();
		JComponent predecessor = null;

		for (Iterator i = getFields().values().iterator(); i.hasNext();) {
			FormField field = (FormField) i.next();

			predecessor = getLayoutHelper().layoutField(field.getLabel(),
					field.getField(), predecessor, detail);
		}
	}
}
