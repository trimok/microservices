@echo off
REM Creating a Newline variable (the two blank lines are required!)
set NLM=^


set NL=^^^%NLM%%NLM%^%NLM%%NLM%

echo %NL%%NL%%NL%    ************************************%NL%    mvn package configserver%NL%    ************************************%NL%%NL%
cd microservice-configserver
call mvn package
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package configserver%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
echo %NL%%NL%%NL%    ************************************%NL%    mvn package eurekaserver%NL%    ************************************%NL%%NL%
cd ../microservice-eurekaserver
call mvn package
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package eurekaserver%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
echo %NL%%NL%%NL%    ************************************%NL%    mvn package oauth2server%NL%    ************************************%NL%%NL%
cd ../microservice-oauth2server
call mvn package 
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package oauth2server%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
echo %NL%%NL%%NL%    ************************************%NL%    mvn package gateway%NL%    ************************************%NL%%NL%
cd ../microservice-gateway
call mvn package 
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package gateway%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
echo %NL%%NL%%NL%    ************************************%NL%    mvn package patient%NL%    ************************************%NL%%NL%
cd ../microservice-patient
call mvn package 
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package patient%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
echo %NL%%NL%%NL%    ************************************%NL%    mvn package patienthistory%NL%    ************************************%NL%%NL%
cd ../microservice-patienthistory
call mvn package 
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package patienthistory%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF 
echo %NL%%NL%%NL%    ************************************%NL%    mvn package expert%NL%    ************************************%NL%%NL%
cd ../microservice-expert
call mvn package
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package expert%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
echo %NL%%NL%%NL%    ************************************%NL%    spring boot run configserver %NL%    ************************************%NL%%NL%
cd ../microservice-configserver 
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
echo %NL%%NL%%NL%    ************************************%NL%    spring boot run eurekaserver  %NL%    ************************************%NL%%NL%
cd ../microservice-eurekaserver 
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
echo %NL%%NL%%NL%    ************************************%NL%    spring boot run oauth2server%NL%    ************************************%NL%%NL%
cd ../microservice-oauth2server 
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
echo %NL%%NL%%NL%    ************************************%NL%    spring boot run gateway %NL%    ************************************%NL%%NL%
cd ../microservice-gateway
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
echo %NL%%NL%%NL%    ************************************%NL%    spring boot run patient %NL%    ************************************%NL%%NL%
cd ../microservice-patient
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
echo %NL%%NL%%NL%    ************************************%NL%    spring boot run patienthistory %NL%    ************************************%NL%%NL%
cd ../microservice-patienthistory
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
echo %NL%%NL%%NL%    ************************************%NL%    spring boot run expert %NL%    ************************************%NL%%NL%
cd ../microservice-expert
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
echo %NL%%NL%%NL%    ************************************%NL%    spring boot run clientui %NL%    ************************************%NL%%NL%
cd ../microservice-clientui
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
echo %NL%%NL%%NL%    ************************************%NL%    mvn package clientui  %NL%    ************************************%NL%%NL%
call mvn package
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package clientui%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd..
echo %NL%%NL%%NL%    ************************************%NL%    SUCCESS SUCCESS  %NL%    ************************************%NL%%NL%
pause
exit 0 /b
:EOF
pause
exit 1 /b