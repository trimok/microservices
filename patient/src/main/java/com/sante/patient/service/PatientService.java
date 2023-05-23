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

import com.sante.patient.exception.PatientConflictException;
import com.sante.patient.exception.PatientNoContentException;
import com.sante.patient.exception.PatientNotFoundException;
import com.sante.patient.model.Patient;
import com.sante.patient.repository.PatientRepository;

/**
 * @author trimok
 *
 *         Le service pour les opérations CRUD sur le patient
 * @see IPatientService
 *
 */
@Service
public class PatientService implements IPatientService {

    @Autowired
    private PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
	super();
	this.patientRepository = patientRepository;
    }

    /**
     * Ajout d'un patient
     */
    @Override
    public Patient createPatient(Patient patient) {
	if (patient.getId() != null) {
	    throw new PatientConflictException(ACTION_CREATE);
	}

	Patient patientCreated = patientRepository.save(patient);
	if (patientCreated == null) {
	    throw new PatientNoContentException(ACTION_CREATE);
	} else {
	    return patientCreated;
	}
    }

    /**
     * Modification d'un patient
     */
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

    /**
     * Suppression d'un patient
     */
    @Override
    public void deletePatient(Long id) {
	if (patientRepository.findById(id).isEmpty()) {
	    throw new PatientNotFoundException(ACTION_DELETE);
	} else {
	    patientRepository.deleteById(id);
	}
    }

    /**
     * Obtention de la liste des patients
     */
    @Override
    public List<Patient> getPatients() {
	return patientRepository.findAll();
    }

    /**
     * Obtention d'un patient
     */
    @Override
    public Patient getPatient(Long id) {
	Optional<Patient> patientOptional = patientRepository.findById(id);
	if (patientOptional.isEmpty()) {
	    throw new PatientNotFoundException(ACTION_GET);
	} else {
	    return patientOptional.get();
	}
    }

    /**
     * Suppression de tous les patients
     */
    @Override
    public void deleteAllPatient() {
	patientRepository.deleteAll();
    }
}
