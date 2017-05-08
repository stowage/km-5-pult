@echo off
set JAVA_HOME=C:\Java\jdk1.5.0_01
set JAVA=%JAVA_HOME%\bin\java
set CP=
for %%i in (ant\lib\*.jar) do call cp.bat %%i
for %%i in (lib\*.jar) do call cp.bat %%i
set CP=%JAVA_HOME%\lib\tools.jar;ant\lib\ant_1.3.jar;%CP%
rem echo %CP%
%JAVA% -classpath %CP% -Djikes.class.path=%JIKESPATH% -Dant.home=lib org.apache.tools.ant.Main %2 %3 %4 %5 %6 -buildfile %1

