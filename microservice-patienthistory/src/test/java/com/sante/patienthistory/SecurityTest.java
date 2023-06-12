package com.sante.patienthistory;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.patienthistory.model.Note;
import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.service.IPatientHistoryService;

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IPatientHistoryService patientHistoryService;

    private final static String ROLE_PATIENT_HISTORY_USER = "ROLE_PATIENT_HISTORY_USER";

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", formatter);
    private LocalDateTime creationDateNoteAdd = LocalDateTime.parse("04/12/1997 11:10:07", formatter);

    private Note note = null;
    private Note noteAdd = null;

    private PatientHistory patientHistoryDatabase = null;
    private PatientHistory patientHistoryNoDatabase = null;
    private PatientHistory patientHistoryDatabaseWithoutNotes = null;

    @BeforeEach
    public void beforeEach() {

	note = new Note(creationDate, "Info");
	noteAdd = new Note(creationDateNoteAdd, "Info note add");

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);

	patientHistoryNoDatabase = new PatientHistory();
	patientHistoryNoDatabase.getNotes().add(noteAdd);

	patientHistoryDatabaseWithoutNotes = new PatientHistory();
	patientHistoryDatabaseWithoutNotes.setId(1L);

	patientHistoryService.deleteAllPatientHistory();
    }

    @AfterEach
    public void afterEach() {
	patientHistoryService.deleteAllPatientHistory();
    }

    /************************************
     * TEST INVALID TOKEN
     ************************************/

    @Test
    public void testInvalidToken() throws Exception {

	// Creation d'un patientHistory
	PatientHistory patientHistoryCreated = patientHistoryService.createPatientHistory(patientHistoryDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/patientHistory/" + patientHistoryCreated.getId())
			.with(jwt().authorities(new SimpleGrantedAuthority("INVALID_ROLE"))))
		.andExpect(status().is(403)).andReturn();
    }

}
