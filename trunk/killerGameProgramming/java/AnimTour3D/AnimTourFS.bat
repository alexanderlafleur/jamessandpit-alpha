@echo off
echo Executing Fullscreen version of AnimTour3D application...

java -cp %CLASSPATH%;ncsa\portfolio.jar -Dsun.java2d.noddraw=true AnimTourFS
echo Finished.