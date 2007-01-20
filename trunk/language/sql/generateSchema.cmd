rem @echo off

set M2_REPO=C:\dev\maven_repo\repository

set HIBERNATE_CLASSPATH=.;C:\projects\language\target\classes
set HIBERNATE_CLASSPATH=%HIBERNATE_CLASSPATH%;%M2_REPO%\org\hibernate\hibernate\3.0.5\hibernate-3.0.5.jar
set HIBERNATE_CLASSPATH=%HIBERNATE_CLASSPATH%;%M2_REPO%\commons-logging\commons-logging\1.0.3\commons-logging-1.0.3.jar
set HIBERNATE_CLASSPATH=%HIBERNATE_CLASSPATH%;%M2_REPO%\hsqldb\hsqldb\1.8.0.1\hsqldb-1.8.0.1.jar
set HIBERNATE_CLASSPATH=%HIBERNATE_CLASSPATH%;%M2_REPO%\mysql\mysql-connector-java\3.1.12\mysql-connector-java-3.1.12.jar
set HIBERNATE_CLASSPATH=%HIBERNATE_CLASSPATH%;%M2_REPO%\xml-apis\xml-apis\1.0.b2\xml-apis-1.0.b2.jar
set HIBERNATE_CLASSPATH=%HIBERNATE_CLASSPATH%;%M2_REPO%\dom4j\dom4j\1.6\dom4j-1.6.jar
set HIBERNATE_CLASSPATH=%HIBERNATE_CLASSPATH%;%M2_REPO%\commons-collections\commons-collections\2.1\commons-collections-2.1.jar
set HIBERNATE_CLASSPATH=%HIBERNATE_CLASSPATH%;%M2_REPO%\javax\transaction\jta\1.0.1B\jta-1.0.1B.jar

set PROPERTY_FILE=hibernate.properties

set CONFIG_FILE=hibernate.cfg.xml

echo
java -cp %HIBERNATE_CLASSPATH% org.hibernate.tool.hbm2ddl.SchemaExport --config=%CONFIG_FILE% --properties=%PROPERTY_FILE% 
