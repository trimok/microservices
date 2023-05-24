package com.sante.patienthistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.bson.BsonValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mongodb.client.result.UpdateResult;
import com.sante.patienthistory.exception.PatientHistoryIdNotValidException;
import com.sante.patienthistory.exception.PatientHistoryNoContentException;
import com.sante.patienthistory.exception.PatientHistoryNotFoundException;
import com.sante.patienthistory.exception.PatientNoteNotValidException;
import com.sante.patienthistory.model.Note;
import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.repository.PatientHistoryRepository;
import com.sante.patienthistory.repository.PatientHistoryRepositoryImpl;
import com.sante.patienthistory.service.IPatientHistoryService;
import com.sante.patienthistory.service.PatientHistoryService;

@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class ServiceTest {

    private Timestamp now = Timestamp.from(Instant.now());

    private IPatientHistoryService patientHistoryService;

    @Mock
    private PatientHistoryRepository patientHistoryRepository;

    @Mock
    private PatientHistoryRepositoryImpl patientHistoryRepositoryImpl;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", formatter);
    private LocalDateTime creationDateNoteAdd = LocalDateTime.parse("04/12/1997 11:10:07", formatter);

    private Note note = new Note(creationDate, "Info");
    private Note noteAdd = new Note(creationDateNoteAdd, "Info note add");

    private PatientHistory patientHistoryDatabase = null;
    private PatientHistory patientHistoryNoDatabase = null;
    private PatientHistory patientHistoryDatabaseWithoutNotes = null;

    private UpdateResult updateResultOK = new UpdateResult() {

	@Override
	public boolean wasAcknowledged() {
	    return false;
	}

	@Override
	public BsonValue getUpsertedId() {
	    return null;
	}

	@Override
	public long getModifiedCount() {
	    return 1;
	}

	@Override
	public long getMatchedCount() {
	    return 1;
	}
    };

    private UpdateResult updateResultKO = new UpdateResult() {

	@Override
	public boolean wasAcknowledged() {
	    return false;
	}

	@Override
	public BsonValue getUpsertedId() {
	    return null;
	}

	@Override
	public long getModifiedCount() {
	    return 0;
	}

	@Override
	public long getMatchedCount() {
	    return 0;
	}
    };

    @BeforeEach
    public void beforeEach() {
	patientHistoryService = new PatientHistoryService(patientHistoryRepository, patientHistoryRepositoryImpl);

	patientHistoryRepository.deleteAll();

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);

	patientHistoryNoDatabase = new PatientHistory();
	patientHistoryNoDatabase.getNotes().add(noteAdd);

	patientHistoryDatabaseWithoutNotes = new PatientHistory();
	patientHistoryDatabaseWithoutNotes.setId(1L);
    }

    /*********************** PATIENT HISTORY SERVICE **************************/

    @Test
    public void patientHistoryServiceCreatePatientHistory() {

	when(patientHistoryRepository.insert(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);
	PatientHistory patientHistorySaved = patientHistoryService.createPatientHistory(patientHistoryDatabase);
	verify(patientHistoryRepository, times(1)).insert(any(PatientHistory.class));
	assertNotNull(patientHistorySaved.getId());
	assertThat(patientHistorySaved.toString()).isEqualTo(patientHistoryDatabase.toString());
    }

    @Test
    public void patientHistoryServiceCreatePatientHistory_PatientHistoryNoContentException() {

	when(patientHistoryRepository.insert(any(PatientHistory.class))).thenReturn(null);
	Executable action = () -> patientHistoryService.createPatientHistory(patientHistoryDatabase);
	assertThrows(PatientHistoryNoContentException.class, action);
	verify(patientHistoryRepository, times(1)).insert(any(PatientHistory.class));
    }

    @Test
    public void patientHistoryServiceCreatePatientHistory_PatientIdNotValidException() {

	Executable action = () -> patientHistoryService.createPatientHistory(patientHistoryNoDatabase);
	assertThrows(PatientHistoryIdNotValidException.class, action);
    }

    @Test
    public void patientHistoryServiceUpdatePatientHistory() {

	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.of(patientHistoryDatabase));
	when(patientHistoryRepository.save(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);
	PatientHistory patientHistoryUpdated = patientHistoryService.updatePatientHistory(patientHistoryDatabase);
	verify(patientHistoryRepository, times(1)).findById(any(Long.class));
	verify(patientHistoryRepository, times(1)).save(any(PatientHistory.class));
	assertThat(patientHistoryUpdated.toString()).isEqualTo(patientHistoryDatabase.toString());
    }

    @Test
    public void patientHistoryServiceUpdatePatientHistory_PatientHistoryNoContentException() {

	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.of(patientHistoryDatabase));
	when(patientHistoryRepository.save(any(PatientHistory.class))).thenReturn(null);
	Executable action = () -> patientHistoryService.updatePatientHistory(patientHistoryDatabase);
	assertThrows(PatientHistoryNoContentException.class, action);
	verify(patientHistoryRepository, times(1)).save(any(PatientHistory.class));
    }

    @Test
    public void patientHistoryServiceUpdatePatientHistory_PatientHistoryNotFoundException() {

	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
	Executable action = () -> patientHistoryService.updatePatientHistory(patientHistoryDatabase);
	assertThrows(PatientHistoryNotFoundException.class, action);
	verify(patientHistoryRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void patientHistoryServiceDeletePatientHistory() {

	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.of(patientHistoryDatabase));
	patientHistoryService.deletePatientHistory(patientHistoryDatabase.getId());
	verify(patientHistoryRepository, times(1)).findById(any(Long.class));
	verify(patientHistoryRepository, times(1)).deleteById(any(Long.class));
    }

    @Test
    public void patientHistoryServiceDeletePatientHistory_PatientHistoryNotFoundException() {

	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
	Executable action = () -> patientHistoryService.deletePatientHistory(patientHistoryDatabase.getId());
	assertThrows(PatientHistoryNotFoundException.class, action);
	verify(patientHistoryRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void patientHistoryServiceGetPatientHistoryById() {

	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.of(patientHistoryDatabase));
	PatientHistory patientHistory = patientHistoryService.getPatientHistory(patientHistoryDatabase.getId());
	assertNotNull(patientHistory);
	assertThat(patientHistory.toString().equals(patientHistoryDatabase.toString()));
	verify(patientHistoryRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void patientHistoryServiceGetPatientHistoryById_PatientHistoryNotFoundException() {

	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
	Executable action = () -> patientHistoryService.getPatientHistory(patientHistoryDatabase.getId());
	assertThrows(PatientHistoryNotFoundException.class, action);
	verify(patientHistoryRepository, times(1)).findById(any(Long.class));
    }

    @Test
    public void patientHistoryServiceGetPatientHistoryById_PatientHistoryIdNotValidException() {
	Executable action = () -> patientHistoryService.getPatientHistory(null);
	assertThrows(PatientHistoryIdNotValidException.class, action);
    }

    @Test
    public void patientHistoryServiceUpdateNote() {

	when(patientHistoryRepositoryImpl.updateNote(any(PatientHistory.class))).thenReturn(updateResultOK);
	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.of(patientHistoryDatabase));
	patientHistoryService.updateNote(patientHistoryDatabase);
	verify(patientHistoryRepository, times(1)).findById(any(Long.class));
	verify(patientHistoryRepositoryImpl, times(1)).updateNote(any(PatientHistory.class));
    }

    @Test
    public void patientHistoryServiceUpdateNote_PatientHistoryIdNotValidException() {
	Executable action = () -> patientHistoryService.updateNote(patientHistoryNoDatabase);
	assertThrows(PatientHistoryIdNotValidException.class, action);
    }

    @Test
    public void patientHistoryServiceUpdateNote_PatientNoteNotValidException() {
	Executable action = () -> patientHistoryService.updateNote(patientHistoryDatabaseWithoutNotes);
	assertThrows(PatientNoteNotValidException.class, action);
    }

    @Test
    public void patientHistoryServiceUpdateNote_PatientHistoryNotFoundException_1() {
	when(patientHistoryRepositoryImpl.updateNote(any(PatientHistory.class))).thenReturn(updateResultKO);
	Executable action = () -> patientHistoryService.updateNote(patientHistoryDatabase);
	assertThrows(PatientHistoryNotFoundException.class, action);
	verify(patientHistoryRepositoryImpl, times(1)).updateNote(any(PatientHistory.class));
    }

    @Test
    public void patientHistoryServiceUpdateNote_PatientHistoryNotFoundException_2() {
	when(patientHistoryRepositoryImpl.updateNote(any(PatientHistory.class))).thenReturn(updateResultOK);
	when(patientHistoryRepository.findById(any(Long.class))).thenReturn(Optional.empty());
	Executable action = () -> patientHistoryService.updateNote(patientHistoryDatabase);
	assertThrows(PatientHistoryNotFoundException.class, action);
	verify(patientHistoryRepository, times(1)).findById(any(Long.class));
	verify(patientHistoryRepositoryImpl, times(1)).updateNote(any(PatientHistory.class));
    }

}
