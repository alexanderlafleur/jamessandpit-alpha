package com.james.ui;

import java.beans.PropertyEditorSupport;

import com.james.fileItems.Dir;
import com.james.helper.DirHelper;

public class DirEditor extends PropertyEditorSupport {
    private DirHelper helper;

    @Override
    public String getAsText() {
        Dir dir = (Dir) super.getValue();
        if (dir == null) {
            return null;
        }
        return "" + dir.getId();
    }

    public DirHelper getHelper() {
        return helper;
    }

    @Override
    public void setAsText(String id) {
        Dir dir = getHelper().load(Integer.parseInt(id));

        super.setValue(dir);
        if (dir == null) {
            throw new IllegalArgumentException("Unsupported dir id [" + id + "]");
        }
    }

    public void setHelper(DirHelper helper) {
        this.helper = helper;
    }
}