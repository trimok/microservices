@echo off
REM Creating a Newline variable (the two blank lines are required!)
set NLM=^


set NL=^^^%NLM%%NLM%^%NLM%%NLM%

cd microservice-configserver
call mvn package
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package configserver%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-eurekaserver
call mvn package
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package eurekaserver%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-oauth2server
call mvn package 
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package oauth2server%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-gateway
call mvn package 
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package gateway%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-patient
call mvn package 
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package patient%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-patienthistory
call mvn package 
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package patienthistory%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF 
cd ../microservice-expert
call mvn package
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package expert%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-configserver
start /b mvn spring-boot:run
ping 127.0.0.1 -n 15 > nul
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn spring-boot:run configserver%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF 
cd ../microservice-eurekaserver
start /b mvn spring-boot:run
ping 127.0.0.1 -n 15 > nul
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn spring-boot:run eurekaserver%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-oauth2server
start /b mvn spring-boot:run
ping 127.0.0.1 -n 15 > nul
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn spring-boot:run oauth2server%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-gateway
start /b mvn spring-boot:run
ping 127.0.0.1 -n 15 > nul
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn spring-boot:run gateway%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-patient
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn spring-boot:run patient%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-patienthistory
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn spring-boot:run patienthistory%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-expert
start /b mvn spring-boot:run -Dspring-boot.run.profiles=test
ping 127.0.0.1 -n 15 > nul
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn spring-boot:run expert%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd ../microservice-clientui
start /b mvn spring-boot:run
ping 127.0.0.1 -n 15 > nul
if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn spring-boot:run clientui%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
rem call mvn package
rem if errorlevel 1 echo  %NL%%NL%%NL%    ************************************%NL%    Erreur dans  mvn package clientui%NL%    ************************************%NL%%NL% & cd.. & pause & goto :EOF
cd..
:EOF
pause