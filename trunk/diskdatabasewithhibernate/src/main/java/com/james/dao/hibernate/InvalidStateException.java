package com.james.dao.hibernate;

public class InvalidStateException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidStateException(String msg) {
        super(msg);
    }
}
