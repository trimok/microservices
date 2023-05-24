package com.sante.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sante.patient.model.Patient;

/**
 * @author trimok
 *
 *         Repository pour le patient
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

}