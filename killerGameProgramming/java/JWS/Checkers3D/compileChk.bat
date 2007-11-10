@echo off
echo Compiling Checkers3D files...

javac -classpath "%CLASSPATH%;vecmath.jar;j3daudio.jar;j3dcore.jar;j3dutils.jar" *.java
echo Finished.