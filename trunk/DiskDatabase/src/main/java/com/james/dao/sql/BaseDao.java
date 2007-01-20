/*
 * Created on Jan 30, 2005
 *
 */
package com.james.dao.sql;

/**
 * @author James
 * 
 * 
 * username The connection username to be passed to our JDBC driver to establish
 * a connection. password The connection password to be passed to our JDBC
 * driver to establish a connection. url The connection URL to be passed to our
 * JDBC driver to establish a connection. driverClassName The fully qualified
 * Java class name of the JDBC driver to be used. connectionProperties The
 * connection properties that will be sent to our JDBC driver when establishing
 * new connections.
 * 
 * Format of the string must be [propertyName=property;]*
 * 
 * NOTE - The "user" and "password" properties will be passed explicitly, so
 * they do not need to be included here.
 * 
 * 
 */
public abstract class BaseDao {

}