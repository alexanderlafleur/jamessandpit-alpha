@echo off
echo Executing Java3D application...

java -Xmx128m -cp "%CLASSPATH%;ncsa\portfolio.jar" Terra3D  %1
echo Finished.