@echo off
echo Executing Java3D application...

java -cp "%CLASSPATH%;vecmath.jar;j3daudio.jar;j3dcore.jar;j3dutils.jar" Checkers3D
echo Finished.