package com.sante.patienthistory.service;

import static com.sante.patienthistory.constants.Constants.ACTION_CREATE;
import static com.sante.patienthistory.constants.Constants.ACTION_CREATE_UPDATE;
import static com.sante.patienthistory.constants.Constants.ACTION_DELETE;
import static com.sante.patienthistory.constants.Constants.ACTION_GET;
import static com.sante.patienthistory.constants.Constants.ACTION_NOTE_DELETE;
import static com.sante.patienthistory.constants.Constants.ACTION_NOTE_UPDATE;
import static com.sante.patienthistory.constants.Constants.ACTION_UPDATE;

import java.util.Optional;
import java.util.SortedSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;
import com.sante.patienthistory.exception.PatientHistoryIdNotValidException;
import com.sante.patienthistory.exception.PatientHistoryNoContentException;
import com.sante.patienthistory.exception.PatientHistoryNotFoundException;
import com.sante.patienthistory.exception.PatientNoteNotValidException;
import com.sante.patienthistory.model.Note;
import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.repository.PatientHistoryCustomRepository;
import com.sante.patienthistory.repository.PatientHistoryRepository;

/**
 * @author trimok
 *
 *
 *         Service responsable des opérations CRUD sur l'historique de patient
 */
@Service
public class PatientHistoryService implements IPatientHistoryService {
    @Autowired
    private PatientHistoryRepository patientHistoryRepository;

    @Autowired
    private PatientHistoryCustomRepository patientHistoryRepositoryImpl;

    public PatientHistoryService(PatientHistoryRepository patientHistoryRepository,
	    PatientHistoryCustomRepository patientHistoryRepositoryImpl) {
	super();
	this.patientHistoryRepository = patientHistoryRepository;
	this.patientHistoryRepositoryImpl = patientHistoryRepositoryImpl;
    }

    /**
     * Ajout d'une note avec Création d'un historique de patient
     */
    @Override
    public PatientHistory createPatientHistory(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientHistoryIdNotValidException(ACTION_CREATE);
	}
	PatientHistory patientHistoryCreated = patientHistoryRepository.insert(patientHistory);
	if (patientHistoryCreated == null) {
	    throw new PatientHistoryNoContentException(ACTION_CREATE);
	} else {
	    return patientHistoryCreated;
	}
    }

    /**
     * Ajout d'une note avec Modification d'un historique de patient
     */
    @Override
    public PatientHistory updatePatientHistory(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientHistoryIdNotValidException(ACTION_CREATE);
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

    /**
     * Ajout d'une note (création ou modification de l'historique du patient)
     */
    @Override
    public PatientHistory createUpdatePatientHistory(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientHistoryIdNotValidException(ACTION_CREATE_UPDATE);
	}
	if (patientHistoryRepository.findById(patientHistory.getId()).isEmpty()) {
	    return createPatientHistory(patientHistory);
	} else {
	    return updatePatientHistory(patientHistory);
	}
    }

    /**
     * Obtention d'un historique de patient
     */
    @Override
    public PatientHistory getPatientHistory(Long id) {
	if (id == null) {
	    throw new PatientHistoryIdNotValidException(ACTION_CREATE);
	}

	Optional<PatientHistory> patientHistoryOptional = patientHistoryRepository.findById(id);
	if (patientHistoryOptional.isEmpty()) {
	    throw new PatientHistoryNotFoundException(ACTION_GET);
	} else {
	    return patientHistoryOptional.get();
	}
    }

    /**
     * Mise à jour d'une note
     */
    @Override
    public PatientHistory updateNote(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientHistoryIdNotValidException(ACTION_NOTE_UPDATE);
	}
	SortedSet<Note> notes = patientHistory.getNotes();
	if (notes.isEmpty() || (((Note) notes.first()).getCreationDate() == null)) {
	    throw new PatientNoteNotValidException(ACTION_NOTE_UPDATE);
	}

	UpdateResult result = patientHistoryRepositoryImpl.updateNote(patientHistory);
	if (result.getMatchedCount() == 0) {
	    throw new PatientHistoryNotFoundException(ACTION_NOTE_UPDATE);
	}

	Optional<PatientHistory> patientHistoryOptional = patientHistoryRepository.findById(patientHistory.getId());
	if (patientHistoryOptional.isEmpty()) {
	    throw new PatientHistoryNotFoundException(ACTION_GET);
	} else {
	    return patientHistoryOptional.get();
	}
    }

    /**
     * Suppression d'une note
     */
    @Override
    public PatientHistory deleteNote(PatientHistory patientHistory) {
	if (patientHistory.getId() == null) {
	    throw new PatientHistoryIdNotValidException(ACTION_NOTE_DELETE);
	}
	SortedSet<Note> notes = patientHistory.getNotes();
	if (notes.isEmpty() || (((Note) notes.first()).getCreationDate() == null)) {
	    throw new PatientNoteNotValidException(ACTION_NOTE_DELETE);
	}

	UpdateResult result = patientHistoryRepositoryImpl.deleteNote(patientHistory);
	if (result.getMatchedCount() == 0 || result.getModifiedCount() == 0) {
	    throw new PatientHistoryNotFoundException(ACTION_NOTE_DELETE);
	}

	Optional<PatientHistory> patientHistoryOptional = patientHistoryRepository.findById(patientHistory.getId());
	if (patientHistoryOptional.isEmpty()) {
	    throw new PatientHistoryNotFoundException(ACTION_GET);
	} else {
	    return patientHistoryOptional.get();
	}
    }

    /**
     * Suppression de l'historique de patient, par son id
     */
    @Override
    public void deletePatientHistory(Long id) {
	if (patientHistoryRepository.findById(id).isEmpty()) {
	    throw new PatientHistoryNotFoundException(ACTION_DELETE);
	} else {
	    patientHistoryRepository.deleteById(id);
	}
    }

    /**
     * Suppression de tous les historiques de patient
     */
    @Override
    public void deleteAllPatientHistory() {
	patientHistoryRepository.deleteAll();
    }
}
