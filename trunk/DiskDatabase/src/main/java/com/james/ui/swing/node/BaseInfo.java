package com.james.ui.swing.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.james.ui.swing.main.DetailPane;

public abstract class BaseInfo {
    private List buttons = new ArrayList();

    private Map fields = new HashMap();

    private BaseNodeHelper helper;

    private String id;

    private String label;

    private LayoutHelper layoutHelper = new LayoutHelper();

    public BaseInfo(String id, String label, BaseNodeHelper helper) {
        this.id = id;
        this.label = label;
        this.helper = helper;
    }

    // public abstract void createButtons();

    public List getButtons() {
        return buttons;
    }

    public Map getFields() {
        return fields;
    }

    public BaseNodeHelper getHelper() {
        return helper;
    }

    public String getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public LayoutHelper getLayoutHelper() {
        return layoutHelper;
    }

    public abstract List loadChildren();

    public void setButtons(List buttons) {
        this.buttons = buttons;
    }

    public void setFields(Map fields) {
        this.fields = fields;
    }

    public void setHelper(BaseNodeHelper helper) {
        this.helper = helper;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setLayoutHelper(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    public abstract void show(DetailPane detail);

    public String toString() {
        return getLabel();
    }

}