package com.sante.patienthistory.service;

import static com.sante.patienthistory.constants.Constants.ACTION_CREATE;
import static com.sante.patienthistory.constants.Constants.ACTION_CREATE_UPDATE;
import static com.sante.patienthistory.constants.Constants.ACTION_GET;
import static com.sante.patienthistory.constants.Constants.ACTION_NOTE_DELETE;
import static com.sante.patienthistory.constants.Constants.ACTION_NOTE_UPDATE;
import static com.sante.patienthistory.constants.Constants.ACTION_UPDATE;

import java.util.Optional;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;
import com.sante.patienthistory.exception.PatientHistoryNoContentException;
import com.sante.patienthistory.exception.PatientHistoryNotFoundException;
import com.sante.patienthistory.exception.PatientIdNotValidException;
import com.sante.patienthistory.exception.PatientNoteNotValidException;
import com.sante.patienthistory.model.Note;
import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.repository.IPatientRepositoryCustom;
import com.sante.patienthistory.repository.PatientHistoryRepository;

@Service
public class PatientHistoryService implements IPatientHistoryService {
    @Autowired
    private PatientHistoryRepository patientHistoryRepository;

    @Autowired
    private IPatientRepositoryCustom patientRepositoryImpl;

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
	    throw new PatientIdNotValidException(ACTION_CREATE_UPDATE);
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

    @Override
    public PatientHistory updateNote(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientIdNotValidException(ACTION_NOTE_UPDATE);
	}
	SortedSet<Note> notes = patientHistory.getNotes();
	if (notes.isEmpty() || (((Note) notes.first()).getCreationDate() == null)) {
	    throw new PatientNoteNotValidException(ACTION_NOTE_UPDATE);
	}

	UpdateResult result = patientRepositoryImpl.updateNote(patientHistory);
	if (result.getMatchedCount() == 0 || result.getModifiedCount() == 0) {
	    throw new PatientHistoryNotFoundException(ACTION_NOTE_UPDATE);
	}
	if (result.getModifiedCount() == 0) {
	    throw new PatientHistoryNotFoundException(ACTION_NOTE_UPDATE);
	}

	Optional<PatientHistory> patientHistoryOptional = patientHistoryRepository.findById(patientHistory.getId());
	if (patientHistoryOptional.isEmpty()) {
	    throw new PatientHistoryNotFoundException(ACTION_GET);
	} else {
	    return patientHistoryOptional.get();
	}
    }

    @Override
    public PatientHistory deleteNote(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientIdNotValidException(ACTION_NOTE_DELETE);
	}
	SortedSet<Note> notes = patientHistory.getNotes();
	if (notes.isEmpty() || (((Note) notes.first()).getCreationDate() == null)) {
	    throw new PatientNoteNotValidException(ACTION_NOTE_DELETE);
	}

	UpdateResult result = patientRepositoryImpl.deleteNote(patientHistory);
	if (result.getMatchedCount() == 0 || result.getModifiedCount() == 0) {
	    throw new PatientHistoryNotFoundException(ACTION_NOTE_DELETE);
	}
	if (result.getModifiedCount() == 0) {
	    throw new PatientHistoryNotFoundException(ACTION_NOTE_DELETE);
	}

	Optional<PatientHistory> patientHistoryOptional = patientHistoryRepository.findById(patientHistory.getId());
	if (patientHistoryOptional.isEmpty()) {
	    throw new PatientHistoryNotFoundException(ACTION_GET);
	} else {
	    return patientHistoryOptional.get();
	}
    }
}
