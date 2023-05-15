package com.sante.patienthistory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sante.patienthistory.model.PatientHistory;

@Repository
public interface PatientHistoryRepository extends MongoRepository<PatientHistory, Long> {
}
