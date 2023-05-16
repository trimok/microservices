package com.sante.patienthistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;

import com.mongodb.client.result.UpdateResult;
import com.sante.patienthistory.model.Note;
import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.repository.IPatientRepositoryCustom;
import com.sante.patienthistory.repository.PatientHistoryRepository;

import lombok.extern.slf4j.Slf4j;

@DataMongoTest
@Slf4j
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class RepositoryTest {
    @Autowired
    private PatientHistoryRepository patientHistoryRepository;

    @Autowired
    private IPatientRepositoryCustom nodeRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", formatter);
    private LocalDateTime creationDateNoteAdd = LocalDateTime.parse("04/12/1997 11:10:07", formatter);

    private Note note = new Note(creationDate, "Info");
    private Note noteAdd = new Note(creationDateNoteAdd, "Info note add");

    private PatientHistory patientHistoryDatabase = new PatientHistory();

    @BeforeAll
    public void beforeAll() {
	patientHistoryRepository.deleteAll();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);
    }

    @BeforeEach
    public void beforeEach() {

    }

    @AfterEach
    public void afterEach() {
	patientHistoryRepository.deleteAll();
    }

    @Test
    public void addNoteCreatePatientHistory() {
	PatientHistory patientHistorySaved = patientHistoryRepository.insert(patientHistoryDatabase);
	assertNotNull(patientHistorySaved.getId());
	assertThat(patientHistorySaved.toString().equals(patientHistoryDatabase.toString()));
	Set<Note> notes = patientHistorySaved.getNotes();
	assertThat(notes.size() == 1);
    }

    @Test
    public void addNoteUpdatePatientHistory() {
	PatientHistory patientHistorySaved = patientHistoryRepository.insert(patientHistoryDatabase);
	patientHistorySaved.getNotes().add(noteAdd);
	PatientHistory patientHistoryUpdated = patientHistoryRepository.save(patientHistorySaved);
	log.info(patientHistoryUpdated.toString());
	assertThat(patientHistoryUpdated.toString().equals(patientHistorySaved.toString()));
	Set<Note> notes = patientHistoryUpdated.getNotes();
	assertThat(notes.size() == 2);
    }

    @Test
    public void getPatientHistory() {
	PatientHistory patientHistorySaved = patientHistoryRepository.insert(patientHistoryDatabase);
	Optional<PatientHistory> patientHistory = patientHistoryRepository.findById(patientHistorySaved.getId());
	assertTrue(patientHistory.isPresent());
    }

    @Test
    public void updateNote() {
	PatientHistory patientHistorySaved = patientHistoryRepository.insert(patientHistoryDatabase);
	Note note = patientHistorySaved.getNotes().first();
	String infoModifiee = "Info modifiee";
	note.setInfo(infoModifiee);

	UpdateResult result = nodeRepository.updateNote(patientHistorySaved);
	assert (result.getModifiedCount() > 0);

	Optional<PatientHistory> patientHistory = patientHistoryRepository.findById(patientHistorySaved.getId());
	assertTrue(patientHistory.isPresent());
	SortedSet<Note> notes = patientHistory.get().getNotes();
	assertTrue(notes.size() == 1);
	Note noteUpdated = (Note) notes.first();
	assertThat(noteUpdated.getInfo().equals(infoModifiee));
    }

    @Test
    public void deleteNote() {
	PatientHistory patientHistorySaved = patientHistoryRepository.insert(patientHistoryDatabase);

	UpdateResult result = nodeRepository.deleteNote(patientHistorySaved);
	assert (result.getModifiedCount() > 0);

	Optional<PatientHistory> patientHistory = patientHistoryRepository.findById(patientHistorySaved.getId());
	assertTrue(patientHistory.isPresent());
	SortedSet<Note> notes = patientHistory.get().getNotes();
	assertTrue(notes.size() == 0);
    }
}
