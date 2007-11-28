package com.james.ui.swing.node;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComponent;

import com.james.ui.dto.MP3DTO;
import com.james.ui.swing.main.DetailPane;

public class MP3Info extends BaseInfo {

    public static final String FIELD_ALBUM = "Album";

    public static final String FIELD_ARTIST = "Artist";

    public static final String FIELD_BAND = "Band";

    public static final String FIELD_BITRATE = "Bitrate";

    public static final String FIELD_COMMENT = "Comment";

    public static final String FIELD_DATE = "Date";

    public static final String FIELD_GENRE = "Genre";

    public static final String FIELD_ID = "Id";

    public static final String FIELD_LENGTH = "Length";

    public static final String FIELD_NAME = "Name";

    public static final String FIELD_SIZE = "Size";

    public static final String FIELD_TITLE = "Title";

    public static final String FIELD_TRACK = "Track";

    public static final String FIELD_TYPE = "Type";

    public static final String FIELD_YEAR = "Year";

    private static final long serialVersionUID = 1L;

    private MP3DTO dto;

    private Map fields = new HashMap();

    private LayoutHelper layoutHelper = new LayoutHelper();

    public MP3Info(String id, String label, MP3DTO mp3DTO, BaseNodeHelper helper) {
        super(id, label, helper);

        this.dto = mp3DTO;

        createFields(mp3DTO);
        createButtons();
    }

    private void createButtons() {
        getButtons().clear();

        DataButton save = new DataButton(SaveHelper.SAVE_MP3, this);
        save.addActionListener(getHelper().getSaveHelper());

        getButtons().add(save);

    }

    private void createFields(MP3DTO mp3) {
        fields.put(FIELD_ALBUM, new FormField(FIELD_ALBUM, mp3.getAlbum()));

        fields.put(FIELD_ARTIST, new FormField(FIELD_ARTIST, mp3.getArtist()));
        fields.put(FIELD_BAND, new FormField(FIELD_BAND, mp3.getBand()));
        fields.put(FIELD_COMMENT, new FormField(FIELD_COMMENT, mp3.getComment()));
        fields.put(FIELD_DATE, new FormField(FIELD_DATE, mp3.getDate()));
        fields.put(FIELD_GENRE, new FormField(FIELD_GENRE, mp3.getGenre()));
        fields.put(FIELD_ID, new FormField(FIELD_ID, mp3.getId()));
        fields.put(FIELD_NAME, new FormField(FIELD_NAME, mp3.getName()));
        fields.put(FIELD_TITLE, new FormField(FIELD_TITLE, mp3.getTitle()));
        fields.put(FIELD_TRACK, new FormField(FIELD_TRACK, mp3.getTrack()));
        fields.put(FIELD_TYPE, new FormField(FIELD_TYPE, mp3.getType()));
        fields.put(FIELD_BITRATE, new FormField(FIELD_BITRATE, String.valueOf(mp3.getBitrate())));
        fields.put(FIELD_LENGTH, new FormField(FIELD_LENGTH, String.valueOf(mp3.getLength())));
        fields.put(FIELD_SIZE, new FormField(FIELD_SIZE, String.valueOf(mp3.getSize())));
        fields.put(FIELD_YEAR, new FormField(FIELD_YEAR, String.valueOf(mp3.getYear())));

    }

    public MP3DTO getDto() {
        return dto;
    }

    @Override
    public Map getFields() {
        return fields;
    }

    @Override
    public LayoutHelper getLayoutHelper() {
        return layoutHelper;
    }

    @Override
    public List loadChildren() {
        return new ArrayList();
    }

    public void setDto(MP3DTO mp3DTO) {
        this.dto = mp3DTO;
    }

    @Override
    public void setFields(Map fields) {
        this.fields = fields;
    }

    @Override
    public void setLayoutHelper(LayoutHelper layoutHelper) {
        this.layoutHelper = layoutHelper;
    }

    @Override
    public void show(DetailPane detail) {
        detail.removeAll();
        JComponent predecessor = null;

        for (Iterator i = getFields().values().iterator(); i.hasNext();) {
            FormField field = (FormField) i.next();

            predecessor = getLayoutHelper().layoutField(field.getLabel(), field.getField(), predecessor, detail);
        }
        for (Iterator i = getButtons().iterator(); i.hasNext();) {
            JButton button = (JButton) i.next();

            predecessor = getLayoutHelper().layoutButton(button, predecessor, detail);

        }
    }

    // public String toHierarcyString() {
    // String msg = new String();
    //
    // MP3DTO dto = getDto();
    //
    // msg += toParentString(dto.getParent()) + dto.getName();
    //
    // return msg;
    // }
    //
    // private String toParentString(DirDTO dto) {
    // String msg = new String();
    //
    // if (dto != null) {
    // msg += dto.getName();
    //
    // msg += " " + toParentString(dto.getParent());
    // }
    //
    // return msg;
    // }
    //
    // private String toParentString(DiskDTO dto) {
    // String msg = new String();
    //
    // if (dto != null) {
    // msg += dto.getLabel();
    // msg += " " + toParentString(dto.getRoot());
    // }
    //
    // return msg;
    // }

    @Override
    public String toString() {
        return getDto().toString();
    }

    // private JLabel addField(FormField detailPaneField,
    // Component predecessor, DetailPane detail) {
    //
    // JLabel label = new JLabel(detailPaneField.getLabel());
    // JTextField field = new JTextField(detailPaneField.getValue(),
    // DEFAULT_FIELD_WIDTH);
    //
    // detail.add(label);
    // detail.add(field);
    //
    // if (predecessor == null) {
    // // Layout label
    // detail.getSpringLayout().putConstraint(SpringLayout.WEST, label, 5,
    // SpringLayout.WEST, detail);
    //
    // detail.getSpringLayout().putConstraint(SpringLayout.NORTH, label,
    // 5, SpringLayout.NORTH, detail);
    //
    // // Layout field
    // detail.getSpringLayout().putConstraint(SpringLayout.WEST, field, 5,
    // SpringLayout.EAST, label);
    // detail.getSpringLayout().putConstraint(SpringLayout.NORTH, field,
    // 5, SpringLayout.NORTH, detail);
    //
    // // Resize this pane
    // // detail.getSpringLayout().putConstraint(SpringLayout.EAST, detail,
    // // 5, SpringLayout.EAST, label);
    // // springLayout.putConstraint(SpringLayout.SOUTH, this, 5,
    // // SpringLayout.NORTH, field);
    //
    // } else {
    // // Layout label
    // detail.getSpringLayout().putConstraint(SpringLayout.WEST, label, 5,
    // SpringLayout.WEST, detail);
    //
    // detail.getSpringLayout().putConstraint(SpringLayout.NORTH, label,
    // 5, SpringLayout.SOUTH, predecessor);
    //
    // // Layout field
    // detail.getSpringLayout().putConstraint(SpringLayout.WEST, field, 5,
    // SpringLayout.EAST, label);
    // detail.getSpringLayout().putConstraint(SpringLayout.NORTH, field,
    // 5, SpringLayout.SOUTH, predecessor);
    //
    // // Resize this pane
    // // detail.getSpringLayout().putConstraint(SpringLayout.EAST, detail,
    // // 5, SpringLayout.EAST, label);
    // // springLayout.putConstraint(SpringLayout.SOUTH, this, 5,
    // // SpringLayout.NORTH, field);
    // }
    //
    // return label;
    // }
}