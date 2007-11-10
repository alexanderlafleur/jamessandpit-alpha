@echo off
echo Executing Java3D application...

java -cp "%CLASSPATH%;ncsa\portfolio.jar" NetTour3D  %1 %2 %3 tour1.txt
echo Finished.