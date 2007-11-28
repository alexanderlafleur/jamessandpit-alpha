/*
 * Created on Mar 7, 2005
 *
 */
package com.james.dao.sql;

/**
 * @author James
 */
public class NotFoundException extends RuntimeException {

    private static final long serialVersionUID = 8971785364359928335L;

    public NotFoundException(String msg) {
        super(msg);
    }

}