package com.sante.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sante.patient.model.Patient;

import jakarta.transaction.Transactional;

/**
 * @author trimok
 *
 *         Repository pour le patient
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE patient", nativeQuery = true)
    void truncateTable();
}
