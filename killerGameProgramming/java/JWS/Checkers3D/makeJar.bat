@echo off

echo Making Checkers3D jar...
jar cvmf mainClass.txt Checkers3D.jar *.class
echo.

echo Indexing jar...
jar i Checkers3D.jar
echo done
