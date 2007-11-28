package com.james.ui.swing.node;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class LayoutHelper {

    public JComponent layoutButton(JButton button, JComponent predecessor, JPanel container) {

        container.add(button);

        SpringLayout layout = (SpringLayout) container.getLayout();

        if (predecessor == null) {
            layout.putConstraint(SpringLayout.WEST, button, 5, SpringLayout.WEST, container);

            layout.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.NORTH, container);

        } else {
            layout.putConstraint(SpringLayout.WEST, button, 5, SpringLayout.WEST, container);

            layout.putConstraint(SpringLayout.NORTH, button, 5, SpringLayout.SOUTH, predecessor);

        }

        return button;
    }

    public JComponent layoutField(JLabel label, JTextField field, JComponent predecessor, JPanel container) {

        container.add(label);
        container.add(field);

        SpringLayout layout = (SpringLayout) container.getLayout();

        if (predecessor == null) {
            // Layout label
            layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, container);

            layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.NORTH, container);

            // Layout field
            layout.putConstraint(SpringLayout.WEST, field, 5, SpringLayout.EAST, label);
            layout.putConstraint(SpringLayout.NORTH, field, 5, SpringLayout.NORTH, container);

            // Resize this pane
            // layout.putConstraint(SpringLayout.EAST, detail,
            // 5, SpringLayout.EAST, label);
            // springLayout.putConstraint(SpringLayout.SOUTH, this, 5,
            // SpringLayout.NORTH, field);

        } else {
            // Layout label
            layout.putConstraint(SpringLayout.WEST, label, 5, SpringLayout.WEST, container);

            layout.putConstraint(SpringLayout.NORTH, label, 5, SpringLayout.SOUTH, predecessor);

            // Layout field
            layout.putConstraint(SpringLayout.WEST, field, 5, SpringLayout.EAST, label);
            layout.putConstraint(SpringLayout.NORTH, field, 5, SpringLayout.SOUTH, predecessor);

            // Resize this pane
            // layout.putConstraint(SpringLayout.EAST, detail,
            // 5, SpringLayout.EAST, label);
            // springLayout.putConstraint(SpringLayout.SOUTH, this, 5,
            // SpringLayout.NORTH, field);
        }

        return label;

    }
}