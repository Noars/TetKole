@echo on

SET JAVA_HOME=..\lib\jre
SET PATH=%JAVA_HOME\bin;%PATH%

java -cp "..\lib\*" -Xms256m -Xmx1g -Dlogging.appender.console.level=OFF -jar ..\lib\TetKole.jar

pause
