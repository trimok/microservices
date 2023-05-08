package com.sante.patient.service;

//import static com.sante.patient.constants.Constants.*;
import static com.sante.patient.constants.Constants.ACTION_CREATE;
import static com.sante.patient.constants.Constants.ACTION_DELETE;
import static com.sante.patient.constants.Constants.ACTION_GET;
import static com.sante.patient.constants.Constants.ACTION_UPDATE;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sante.patient.exception.PatientNoContentException;
import com.sante.patient.exception.PatientNotFoundException;
import com.sante.patient.model.Patient;
import com.sante.patient.repository.PatientRepository;

@Service
public class PatientService implements IPatientService {

    @Autowired
    private PatientRepository patientRepository;

    @Override
    public Patient createPatient(Patient patient) {
	Patient patientCreated = patientRepository.save(patient);
	if (patientCreated == null) {
	    throw new PatientNoContentException(ACTION_CREATE);
	} else {
	    return patientCreated;
	}
    }

    @Override
    public Patient updatePatient(Patient patient) {
	if (patientRepository.findById(patient.getId()).isEmpty()) {
	    throw new PatientNotFoundException(ACTION_UPDATE);
	} else {
	    Patient patientUpdated = patientRepository.save(patient);
	    if (patientUpdated == null) {
		throw new PatientNoContentException(ACTION_UPDATE);
	    } else {
		return patientUpdated;
	    }
	}
    }

    @Override
    public void deletePatient(Long id) {
	if (patientRepository.findById(id).isEmpty()) {
	    throw new PatientNotFoundException(ACTION_DELETE);
	} else {
	    patientRepository.deleteById(id);
	}
    }

    @Override
    public List<Patient> getPatients() {
	return patientRepository.findAll();
    }

    @Override
    public Patient getPatient(Long id) {
	Optional<Patient> patientOptional = patientRepository.findById(id);
	if (patientOptional.isEmpty()) {
	    throw new PatientNotFoundException(ACTION_GET);
	} else {
	    return patientOptional.get();
	}
    }
}
