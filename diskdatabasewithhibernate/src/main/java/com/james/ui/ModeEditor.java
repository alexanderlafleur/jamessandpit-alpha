/**
 * 
 */
package com.james.ui;

import java.beans.PropertyEditorSupport;

public class ModeEditor extends PropertyEditorSupport {
    @Override
    public String getAsText() {
        Mode mode = (Mode) super.getValue();
        if (mode == null) {
            return null;
        }
        return mode.getCode();
    }

    @Override
    public void setAsText(String text) {
        Mode mode = Mode.getMode(text);
        super.setValue(mode);
        if (mode == null) {
            throw new IllegalArgumentException("Unsupported mode string [" + text + "]");
        }
    }
}