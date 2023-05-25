cd microservice-configserver
call mvn package
cd ../microservice-eurekaserver
call mvn package
cd ../microservice-gateway
call mvn package
cd ../microservice-patient
call mvn package
cd ../microservice-patienthistory
call mvn package
cd ../microservice-expert
call mvn package
cd ../microservice-clientui
call mvn package
cd ..
docker compose up