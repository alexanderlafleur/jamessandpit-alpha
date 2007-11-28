package com.james.calendar.util;

import java.util.Calendar;

import com.james.calendar.element.Day;
import com.james.calendar.element.FOPCalendar;
import com.james.calendar.element.Month;
import com.james.calendar.element.MonthEnum;
import com.james.calendar.element.Page;
import com.james.calendar.element.Week;

public class CalendarCreator {

    private static CalendarCreator instance = new CalendarCreator();

    public static CalendarCreator newInstance() {
        return instance;
    }

    private CalendarCreator() {

    }

    public FOPCalendar create(int year) {
        FOPCalendar calendar = new FOPCalendar(year);

        String path = "data";

        Calendar c = Calendar.getInstance();
        c.setFirstDayOfWeek(Calendar.SUNDAY);
        c.set(year, Calendar.JANUARY, 1, 0, 0);

        for (int curMonthIndex = c.get(Calendar.JANUARY); curMonthIndex <= Calendar.DECEMBER; curMonthIndex++) {

            Month month = createMonth(c, year);

            Page page = new Page("Meow", path + "/cat_eyes.jpg", MonthEnum.JANUARY);
            page.setMonth(month);
            calendar.addPage(page);
        }
        return calendar;
    }

    // for (int dayOfMonth = 1, maxDaysInMonth =
    // c.getActualMaximum(Calendar.DAY_OF_MONTH); dayOfMonth <
    // maxDaysInMonth; dayOfMonth++) {

    private Month createMonth(Calendar c, int year) {
        Month month = new Month(year);

        month.setMonth(getMonthEnum(c.get(Calendar.MONTH)));

        for (int weekIndex = 1, maxWeeksInMonth = c.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH); weekIndex <= maxWeeksInMonth; weekIndex++) {

            Week week = new Week();

            while (true) {
                int dayOfMonth = c.get(Calendar.DAY_OF_MONTH);

                int curWeek = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);

                if (curWeek != weekIndex) {
                    break;
                }

                // int dayOfWeek = c.get(Calendar.DAY_OF_WEEK), maxDaysInWeek =
                // c.getActualMaximum(Calendar.DAY_OF_WEEK); dayOfWeek <
                // maxDaysInWeek; dayOfWeek++) {

                // int weekInMonth = c.get(Calendar.DAY_OF_WEEK_IN_MONTH);

                int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

                switch (dayOfWeek) {
                case Calendar.MONDAY:
                    week.setMonday(new Day(String.valueOf(dayOfMonth)));
                    break;
                case Calendar.TUESDAY:
                    week.setTuesday(new Day(String.valueOf(dayOfMonth)));
                    break;
                case Calendar.WEDNESDAY:
                    week.setWednesday(new Day(String.valueOf(dayOfMonth)));
                    break;
                case Calendar.THURSDAY:
                    week.setThursday(new Day(String.valueOf(dayOfMonth)));
                    break;
                case Calendar.FRIDAY:
                    week.setFriday(new Day(String.valueOf(dayOfMonth)));
                    break;
                case Calendar.SATURDAY:
                    week.setSaturday(new Day(String.valueOf(dayOfMonth)));
                    break;
                case Calendar.SUNDAY:
                    week.setSunday(new Day(String.valueOf(dayOfMonth)));
                    break;
                }

                c.add(Calendar.DAY_OF_MONTH, 1);
            }

            month.addWeek(week);
        }

        return month;
    }

    private MonthEnum getMonthEnum(int monthIndex) {

        MonthEnum me;

        switch (monthIndex) {
        case Calendar.JANUARY:
            me = MonthEnum.JANUARY;
            break;
        case Calendar.FEBRUARY:
            me = MonthEnum.FEBRUARY;
            break;
        case Calendar.MARCH:
            me = MonthEnum.MARCH;
            break;
        case Calendar.APRIL:
            me = MonthEnum.APRIL;
            break;
        case Calendar.MAY:
            me = MonthEnum.MAY;
            break;
        case Calendar.JUNE:
            me = MonthEnum.JUNE;
            break;
        case Calendar.JULY:
            me = MonthEnum.JULY;
            break;
        case Calendar.AUGUST:
            me = MonthEnum.AUGUST;
            break;
        case Calendar.SEPTEMBER:
            me = MonthEnum.SEPTEMBER;
            break;
        case Calendar.OCTOBER:
            me = MonthEnum.OBTOBER;
            break;
        case Calendar.NOVEMBER:
            me = MonthEnum.NOVEMBER;
            break;
        case Calendar.DECEMBER:
            me = MonthEnum.DECEMBER;
            break;
        default:
            me = null;
        }

        return me;
    }
}

// calendar.addPage("Meow", path + "/cat_eyes.jpg",
// MonthEnum.FEBRUARY);
// calendar.addPage("Meow", path + "/cat_eyes.jpg",
// MonthEnum.MARCH);
// calendar.addPage("Meow", path + "/cat_eyes.jpg",
// MonthEnum.APRIL);
// calendar.addPage("Meow", path + "/cat_eyes.jpg", MonthEnum.MAY);
// calendar.addPage("Meow", path + "/cat_eyes.jpg", MonthEnum.JUNE);
// calendar.addPage("Meow", path + "/cat_eyes.jpg", MonthEnum.JULY);
// calendar.addPage("Meow", path + "/cat_eyes.jpg",
// MonthEnum.AUGUST);
// calendar.addPage("Meow", path + "/cat_eyes.jpg",
// MonthEnum.SEPTEMBER);
// calendar.addPage("Meow", path + "/cat_eyes.jpg",
// MonthEnum.OBTOBER);
// calendar.addPage("Meow", path + "/cat_eyes.jpg",
// MonthEnum.NOVEMBER);
// calendar.addPage("Meow", path + "/cat_eyes.jpg",
// MonthEnum.DECEMBER);
