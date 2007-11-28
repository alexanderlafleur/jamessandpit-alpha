package com.james.calendar.element;

import java.util.ArrayList;
import java.util.List;

import com.james.calendar.util.FileReaderHelper;

public class FOPCalendar {
    private Header header = new Header();

    private List<Page> pages = new ArrayList<Page>();

    private int year;

    public FOPCalendar(int year) {
        this.year = year;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(FileReaderHelper.read("data/calendar.start.fo.part"));
        sb.append(header.toString());
        sb.append(FileReaderHelper.read("data/prepage.start.fo.part"));

        for (Page page : pages) {
            sb.append(page.toString());
        }

        sb.append(FileReaderHelper.read("data/prepage.end.fo.part"));
        sb.append(FileReaderHelper.read("data/calendar.end.fo.part"));

        return sb.toString();
    }
}