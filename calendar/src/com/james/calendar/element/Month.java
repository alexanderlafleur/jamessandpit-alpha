package com.james.calendar.element;

import java.util.ArrayList;
import java.util.List;

import com.james.calendar.util.FileReaderHelper;

public class Month {
    private static final String TAG_MONTH = "__CALENDAR_MONTH__";

    private static final String TAG_MONTH_YEAR = "__CALENDAR_MONTH_YEAR__";

    private MonthEnum month;

    private int year;

    private List<Week> weeks = new ArrayList<Week>();

    public Month(int year) {
        this.year = year;
    }

    public void addWeek(Week week) {
        weeks.add(week);
    }

    public MonthEnum getMonth() {
        return month;
    }

    public List<Week> getWeeks() {
        return weeks;
    }

    public void setMonth(MonthEnum month) {
        this.month = month;
    }

    public void setWeeks(List<Week> weeks) {
        this.weeks = weeks;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(FileReaderHelper.read("data/month.start.fo.part"));

        for (Week row : weeks) {
            sb.append(row.toString());
        }

        sb.append(FileReaderHelper.read("data/month.end.fo.part"));

        return sb.toString().replaceAll(TAG_MONTH, month.getName()).replaceAll(TAG_MONTH_YEAR, String.valueOf(year));
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
