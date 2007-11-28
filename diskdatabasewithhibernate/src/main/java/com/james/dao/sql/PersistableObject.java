/*
 * Created on Jan 30, 2005
 *
 */
package com.james.dao.sql;

import java.sql.SQLException;

/**
 * @author James
 */
public interface PersistableObject {
    public boolean save();

    public void delete() throws SQLException, NotFoundException;

    public long getId();

    public void setId(long Id);
}