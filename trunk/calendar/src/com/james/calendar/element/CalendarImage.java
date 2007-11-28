package com.james.calendar.element;

import com.james.calendar.util.FileReaderHelper;

public class CalendarImage {
    private static final String TAG_IMAGE = "__CALENDAR_IMAGE__";

    private static final String TAG_IMAGE_CAPTION = "__CALENDAR_IMAGE_CAPTION__";

    private String caption;

    private String path;

    public CalendarImage(String caption, String path) {
        this.path = path;
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public String getPath() {
        return path;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        String s = FileReaderHelper.read("data/image.fo.part");

        return s.replaceAll(TAG_IMAGE, path).replaceAll(TAG_IMAGE_CAPTION, caption);
    }
}
