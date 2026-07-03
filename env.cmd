@echo off
cls

:: Define variables for the current session (No Admin rights required)
set NODE_HOME=C:\dev\node
set JAVA_HOME=C:\dev\jdk-25.0.3
set MAVEN_HOME=C:\dev\maven

:: Update the PATH by placing your preferred versions ahead of system defaults
set PATH=%NODE_HOME%;%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo ===================================================
echo     Development Environment Configured (No Admin)
echo ===================================================
echo.

:: Display versions to verify everything is configured correctly
echo Java:
cmd /c java -version
echo.
echo Maven:
cmd /c mvn -v
echo.
echo Node.js:
cmd /c node -v
echo.
echo ===================================================
echo Ready to code! You can run your commands below.
echo ===================================================
echo.

:: Keep the command prompt window open
cmd /k