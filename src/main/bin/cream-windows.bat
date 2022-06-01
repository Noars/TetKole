@echo on

SET JAVA_HOME=..\lib\jre

java -cp "..\lib\*" -Xms256m -Xmx1g -Dlogging.appender.console.level=OFF -jar ../lib/translateAudioFiles.jar

pause
