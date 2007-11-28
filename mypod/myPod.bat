SET CLASSES=./lib/myPod.jar;./lib/kunststoff.jar;./lib/id3libvdheide.jar;./lib/skinlf.jar;./lib/nextlf.jar;%CLASSPATH%

start javaw -cp %CLASSES% de.axelwernicke.mypod.myPod
REM java -cp %CLASSES% de.axelwernicke.mypod.myPod
