package com.james.calendar.element;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class MonthEnum {
    public static final MonthEnum APRIL = new MonthEnum("April");

    public static final MonthEnum AUGUST = new MonthEnum("August");

    public static final MonthEnum DECEMBER = new MonthEnum("December");

    public static final MonthEnum FEBRUARY = new MonthEnum("February");

    public static final MonthEnum JANUARY = new MonthEnum("January");

    public static final MonthEnum JULY = new MonthEnum("July");

    public static final MonthEnum JUNE = new MonthEnum("June");

    public static final MonthEnum MARCH = new MonthEnum("March");

    public static final MonthEnum MAY = new MonthEnum("May");

    private static int nextId = 0;

    public static final MonthEnum NOVEMBER = new MonthEnum("November");

    public static final MonthEnum OBTOBER = new MonthEnum("October");

    public static final MonthEnum SEPTEMBER = new MonthEnum("September");

    private final int id;

    private String name;

    private MonthEnum(String name) {
        this.id = nextId++;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MonthEnum)) {
            return super.equals(o);
        }

        if (this == o) {
            return true;
        }

        MonthEnum m = (MonthEnum) o;

        return new EqualsBuilder().append(getId(), m.getId()).append(getName(), m.getName()).isEquals();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).append(id).append(name).toHashCode();
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}