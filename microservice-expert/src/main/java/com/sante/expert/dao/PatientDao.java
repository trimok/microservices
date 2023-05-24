package com.sante.expert.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         Classe PatientDao
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDao {
    /**
     * Le patient
     */
    private Patient patient;
    /**
     * Historique du patient
     */
    private PatientHistory patientHistory;
}
