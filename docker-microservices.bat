cd microservice-configserver
call mvn package -DskipTests
cd ../microservice-eurekaserver
call mvn package -DskipTests
cd ../microservice-gateway
call mvn package -DskipTests
cd ../microservice-patient
call mvn package -DskipTests
cd ../microservice-patienthistory
call mvn package -DskipTests 
cd ../microservice-expert
call mvn package -DskipTests
cd ../microservice-clientui
call mvn package -DskipTests
cd ..
docker compose up