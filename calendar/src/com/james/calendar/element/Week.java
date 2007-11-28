package com.james.calendar.element;

import com.james.calendar.util.FileReaderHelper;

public class Week {
    private Day friday = new Day("");

    private Day monday = new Day("");

    private Day saturday = new Day("");

    private Day sunday = new Day("");

    private Day thursday = new Day("");

    private Day tuesday = new Day("");

    private Day wednesday = new Day("");

    public Day getFriday() {
        return friday;
    }

    public Day getMonday() {
        return monday;
    }

    public Day getSaturday() {
        return saturday;
    }

    public Day getSunday() {
        return sunday;
    }

    public Day getThursday() {
        return thursday;
    }

    public Day getTuesday() {
        return tuesday;
    }

    public Day getWednesday() {
        return wednesday;
    }

    public void setFriday(Day friday) {
        this.friday = friday;
    }

    public void setMonday(Day monday) {
        this.monday = monday;
    }

    public void setSaturday(Day saturday) {
        this.saturday = saturday;
    }

    public void setSunday(Day sunday) {
        this.sunday = sunday;
    }

    public void setThursday(Day thursday) {
        this.thursday = thursday;
    }

    public void setTuesday(Day tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(Day wednesday) {
        this.wednesday = wednesday;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(FileReaderHelper.read("data/row.start.fo.part"));

        sb.append(sunday);
        sb.append(monday);
        sb.append(tuesday);
        sb.append(wednesday);
        sb.append(thursday);
        sb.append(friday);
        sb.append(saturday);

        sb.append(FileReaderHelper.read("data/row.end.fo.part"));

        return sb.toString();
    }
}
