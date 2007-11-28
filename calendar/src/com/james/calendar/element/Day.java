package com.james.calendar.element;

import com.james.calendar.util.FileReaderHelper;

public class Day {
    private static final String TAG_CELL = "__CALENDAR_CELL__";

    private String value;

    public Day(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(FileReaderHelper.read("data/cell.fo.part"));

        return sb.toString().replaceAll(TAG_CELL, value);
    }
}
