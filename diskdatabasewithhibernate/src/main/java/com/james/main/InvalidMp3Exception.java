package com.james.main;

public class InvalidMp3Exception extends Exception {

    private static final long serialVersionUID = 7648897376492283437L;

    public InvalidMp3Exception(String string) {
        super(string);
    }

    public InvalidMp3Exception(String string, Exception e) {
        super(string, e);
    }

}
