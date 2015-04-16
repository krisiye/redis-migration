setlocal

@REM Set the current directory to the installation directory
set INSTALLDIR=%~dp0

if exist %INSTALLDIR%\jre\bin\java.exe (
 set JAVA_CMD=%INSTALLDIR%\jre\bin\java.exe
) else (
 @REM Use JAVA_HOME if it is set
 if "%JAVA_HOME%"=="" (
  set JAVA_CMD=java
 ) else (
  set JAVA_CMD="%JAVA_HOME%\bin\java.exe"
 )
)

%JAVA_CMD% -cp "%INSTALLDIR%\lib\*;%INSTALLDIR%\script-lib\*" org.beyondpn.redis.migration.Main %*

@REM Exit using the same code returned from Java
EXIT /B %ERRORLEVEL%