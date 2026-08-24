@echo off
cls

:: Define as variáveis para a sessão atual (Sem precisar de Admin)
set NODE_HOME=C:\dev\node
set JAVA_HOME=C:\dev\jdk-25.0.3
set MAVEN_HOME=C:\dev\maven

:: Atualiza o PATH colocando as suas versões na frente das do sistema
set PATH=%NODE_HOME%;%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%

echo ===================================================
echo   Ambiente de Desenvolvimento Configurado (Sem Admin)
echo ===================================================
echo.

:: Mostra as versões na tela para garantir que deu certo
echo Java: 
cmd /c java -version
echo.
echo Maven:
cmd /c mvn -v
echo.
echo Node:
cmd /c node -v
echo.
echo ===================================================
echo Pronto para codar! Pode rodar seus comandos abaixo.
echo ===================================================
echo.

:: Mantém o prompt de comando aberto
cmd /k