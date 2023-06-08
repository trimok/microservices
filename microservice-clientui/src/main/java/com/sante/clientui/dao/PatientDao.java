package com.sante.clientui.dao;

import com.sante.clientui.model.Patient;
import com.sante.clientui.model.PatientHistory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author trimok
 *
 *         PatientDao
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientDao {
    /**
     * Patient
     */
    private Patient patient;
    /**
     * Patient History
     */
    private PatientHistory patientHistory;
}
