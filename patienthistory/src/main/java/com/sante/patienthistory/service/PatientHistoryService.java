package com.sante.patienthistory.service;

import static com.sante.patienthistory.constants.Constants.ACTION_CREATE;
import static com.sante.patienthistory.constants.Constants.ACTION_GET;
import static com.sante.patienthistory.constants.Constants.ACTION_UPDATE;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sante.patienthistory.exception.PatientHistoryNoContentException;
import com.sante.patienthistory.exception.PatientHistoryNotFoundException;
import com.sante.patienthistory.exception.PatientIdNotValidException;
import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.repository.PatientHistoryRepository;

@Service
public class PatientHistoryService implements IPatientHistoryService {
    @Autowired
    private PatientHistoryRepository patientHistoryRepository;

    @Override
    public PatientHistory createPatientHistory(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientIdNotValidException(ACTION_CREATE);
	}
	PatientHistory patientHistoryCreated = patientHistoryRepository.insert(patientHistory);
	if (patientHistoryCreated == null) {
	    throw new PatientHistoryNoContentException(ACTION_CREATE);
	} else {
	    return patientHistoryCreated;
	}
    }

    @Override
    public PatientHistory updatePatientHistory(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientIdNotValidException(ACTION_CREATE);
	}

	Optional<PatientHistory> optionalPatientHistoryDatabase = null;
	optionalPatientHistoryDatabase = patientHistoryRepository.findById(patientHistory.getId());
	if (optionalPatientHistoryDatabase.isEmpty()) {
	    throw new PatientHistoryNotFoundException(ACTION_UPDATE);
	} else {
	    PatientHistory patientHistoryDatabase = optionalPatientHistoryDatabase.get();
	    patientHistoryDatabase.getNotes().add((patientHistory.getNotes().iterator().next()));
	    PatientHistory patientHistoryUpdated = patientHistoryRepository.save(patientHistoryDatabase);
	    if (patientHistoryUpdated == null) {
		throw new PatientHistoryNoContentException(ACTION_UPDATE);
	    } else {
		return patientHistoryUpdated;
	    }
	}
    }

    @Override
    public PatientHistory createUpdatePatientHistory(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientIdNotValidException(ACTION_CREATE);
	}
	if (patientHistoryRepository.findById(patientHistory.getId()).isEmpty()) {
	    return createPatientHistory(patientHistory);
	} else {
	    return updatePatientHistory(patientHistory);
	}
    }

    @Override
    public PatientHistory getPatientHistory(Long id) {
	if (id == null) {
	    throw new PatientIdNotValidException(ACTION_CREATE);
	}

	Optional<PatientHistory> patientHistoryOptional = patientHistoryRepository.findById(id);
	if (patientHistoryOptional.isEmpty()) {
	    throw new PatientHistoryNotFoundException(ACTION_GET);
	} else {
	    return patientHistoryOptional.get();
	}
    }
}
