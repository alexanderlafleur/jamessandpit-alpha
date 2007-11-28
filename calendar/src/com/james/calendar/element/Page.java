package com.james.calendar.element;

import com.james.calendar.util.FileReaderHelper;

public class Page {
    private CalendarImage image;

    private Month month;

    public Page(String imageTitle, String imageName, MonthEnum monthEnum) {
        image = new CalendarImage(imageTitle, imageName);
    }

    public CalendarImage getImage() {
        return image;
    }

    public void setImage(CalendarImage image) {
        this.image = image;
    }

    public Month getMonth() {
        return month;
    }

    public void setMonth(Month month) {
        this.month = month;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        sb.append(FileReaderHelper.read("data/page.start.fo.part"));

        sb.append(image.toString());
        sb.append(month.toString());

        sb.append(FileReaderHelper.read("data/page.end.fo.part"));

        return sb.toString();
    }
}
