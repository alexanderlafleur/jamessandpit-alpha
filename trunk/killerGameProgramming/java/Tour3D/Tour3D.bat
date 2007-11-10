@echo off
echo Executing Java3D application...

java -cp %CLASSPATH%;ncsa\portfolio.jar Tour3D  %1.txt
echo Finished.