@echo off

echo Making BugRunner Jar...
jar cvmf mainClass.txt BugRunner.jar *.class Images Sounds
echo.

echo Indexing jar...
jar i BugRunner.jar
echo done
