package com.sante.patient.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sante.patient.model.Patient;
import com.sante.patient.repository.PatientRepository;

@Service
public class PatientService implements IPatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient createPatient(Patient patient) {
	return patientRepository.save(patient);
    }

    @Override
    public Patient updatePatient(Patient patient) {
	return patientRepository.save(patient);
    }

    @Override
    public void deletePatient(Long id) {
	patientRepository.deleteById(id);
    }

    @Override
    public List<Patient> getPatients() {
	return patientRepository.findAll();
    }

    @Override
    public Patient getPatient(Long id) {
	return patientRepository.findById(id).get();
    }
}
