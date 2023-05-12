package com.sante.patient.service;

import java.util.List;

import com.sante.patient.model.Patient;

public interface IPatientService {

    Patient createPatient(Patient patient);

    Patient updatePatient(Patient patient);

    void deletePatient(Long id);

    List<Patient> getPatients();

    Patient getPatient(Long id);

    void deleteAllPatient();
}
