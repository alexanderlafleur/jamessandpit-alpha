package com.james.calendar.element;

import com.james.calendar.util.FileReaderHelper;

public class Header {

    @Override
    public String toString() {
        return FileReaderHelper.read("data/header.fo.part");
    }
}
