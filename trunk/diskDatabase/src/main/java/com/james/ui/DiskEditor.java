package com.james.ui;


import java.beans.PropertyEditorSupport;

import com.james.fileItems.Disk;
import com.james.helper.DiskHelper;

public class DiskEditor extends PropertyEditorSupport {
    private DiskHelper helper;

    public String getAsText() {
        Disk disk = (Disk) super.getValue();
        if (disk == null)
            return null;
        return "" + disk.getId();
    }

    public DiskHelper getHelper() {
        return helper;
    }

    public void setAsText(String id) {
        Disk disk = getHelper().load(Integer.parseInt(id));

        super.setValue(disk);
        if (disk == null)
            throw new IllegalArgumentException("Unsupported disk id [" + id
                    + "]");
    }

    public void setHelper(DiskHelper helper) {
        this.helper = helper;
    }
}