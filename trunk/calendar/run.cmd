@echo off

SET FOP_CLASSPATH=c:\dev\fop-0.20.5\build\fop.jar
SET FOP_CLASSPATH=%FOP_CLASSPATH%;c:\dev\fop-0.20.5\build\fop.jar
SET FOP_CLASSPATH=%FOP_CLASSPATH%;c:\dev\fop-0.20.5\lib\avalon-framework-cvs-20020806.jar
SET FOP_CLASSPATH=%FOP_CLASSPATH%;c:\dev\fop-0.20.5\lib\batik.jar
SET FOP_CLASSPATH=%FOP_CLASSPATH%;C:\dev\Jimi\JimiProClasses.zip

ant -lib "%CLASSPATH%;%FOP_CLASSPATH%" newPDF