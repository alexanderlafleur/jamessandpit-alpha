package com.james.ui.swing.node;

import javax.swing.JLabel;
import javax.swing.JTextField;

public class FormField {

    private static final int DEFAULT_FIELD_WIDTH = 20;

    private JTextField field;

    private JLabel label;

    private String labelValue;

    private String fieldValue;

    public FormField(String labelValue, String fieldValue) {
        this.labelValue = labelValue;
        this.fieldValue = fieldValue;
    }

    public JTextField getField() {
        if (field == null) {
            field = new JTextField(fieldValue, DEFAULT_FIELD_WIDTH);
        }
        return field;
    }

    public JLabel getLabel() {
        if (label == null) {
            label = new JLabel(labelValue);

        }
        return label;
    }

    public void setField(JTextField field) {
        this.field = field;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

}
