package com.james.ui.swing.node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.james.ui.dto.DiskFileDTO;
import com.james.ui.swing.main.DetailPane;

public class FileInfo extends BaseInfo {

    public static final String FIELD_NAME = "Name";

    public static final String FIELD_PATH = "Path";

    public static final String FIELD_SIZE = "Size";

    public static final String FIELD_TYPE = "Type";

    private static final long serialVersionUID = 1L;

    private DiskFileDTO dto;

    public FileInfo(String id, String label, DiskFileDTO fileDTO, BaseNodeHelper helper) {
        super(id, label, helper);

        this.dto = fileDTO;

        createFields(fileDTO);
        createButtons();

    }

    private void createButtons() {
        getButtons().clear();

        DataButton save = new DataButton(SaveHelper.SAVE_FILE, this);
        save.addActionListener(getHelper().getSaveHelper());

        getButtons().add(save);

    }

    private void createFields(DiskFileDTO file) {
        getFields().put(FIELD_NAME, new FormField(FIELD_NAME, file.getName()));
        getFields().put(FIELD_PATH, new FormField(FIELD_PATH, file.getPath()));
        getFields().put(FIELD_SIZE, new FormField(FIELD_SIZE, file.getSize()));
        getFields().put(FIELD_TYPE, new FormField(FIELD_TYPE, file.getType()));
    }

    public DiskFileDTO getDto() {
        return dto;
    }

    public List loadChildren() {
        return new ArrayList();
    }

    public void setDto(DiskFileDTO dto) {
        this.dto = dto;
    }

    public void setFields(Map fields) {
        getFields().clear();

        getFields().putAll(fields);
    }

    public void show(DetailPane detail) {
        detail.removeAll();
        JComponent predecessor = null;

        for (Iterator i = getFields().values().iterator(); i.hasNext();) {
            FormField field = (FormField) i.next();

            predecessor = getLayoutHelper().layoutField(field.getLabel(), field.getField(),
                    predecessor, detail);
        }

        for (Iterator i = getButtons().iterator(); i.hasNext();) {
            JButton button = (JButton) i.next();

            predecessor = getLayoutHelper().layoutButton(button, predecessor, detail);
        }
    }

    // public String toString() {
    // String msg = getHelper().createHierarchyString(this);
    //
    // return msg;
    // }
}