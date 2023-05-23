package com.sante.patienthistory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.sante.patienthistory.model.PatientHistory;

/**
 * @author trimok
 *
 *         Repository standard pour l'historique du patient
 */
@Repository
public interface PatientHistoryRepository extends MongoRepository<PatientHistory, Long> {
}
