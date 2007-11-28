package com.james.ui.swing.node;

import javax.swing.JButton;

public class DataButton extends JButton {

    private static final long serialVersionUID = 1L;

    private BaseInfo info;

    public DataButton(String label, BaseInfo info) {
        super(label);

        this.info = info;
    }

    public BaseInfo getInfo() {
        return info;
    }

    public void setInfo(BaseInfo info) {
        this.info = info;
    }
}
