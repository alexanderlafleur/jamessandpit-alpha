package com.james.ui;

import java.beans.PropertyEditorSupport;

public class ActionEditor extends PropertyEditorSupport {
    public String getAsText() {
        Action action = (Action) super.getValue();
        if (action == null) {
            return null;
        }
        return action.getName();
    }

    public void setAsText(String text) {
        Action action = Action.getAction(text);
        super.setValue(action);
        if (action == null)
            throw new IllegalArgumentException("Unsupported action name ["
                    + text + "]");
    }
}