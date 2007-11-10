@echo off

rem set HYPERSONIC_JAR=%MAVEN_REPO%\hsqldb\jars\hsqldb-1.7.3.3.jar
set HYPERSONIC_JAR=%MAVEN_REPO%/hsqldb/hsqldb/1.8.0.1/hsqldb-1.8.0.1.jar
set CP=
set CP=%HYPERSONIC_JAR%

echo using jar %HYPERSONIC_JAR%

%JAVA_HOME%\bin\java -cp "%HYPERSONIC_JAR%;bin" org.hsqldb.Server