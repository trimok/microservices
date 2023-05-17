package com.sante.patienthistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.patienthistory.model.Note;
import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.service.IPatientHistoryService;

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IPatientHistoryService patientHistoryService;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", formatter);
    private LocalDateTime creationDateNoteAdd = LocalDateTime.parse("04/12/1997 11:10:07", formatter);

    private Note note = new Note(creationDate, "Info");
    private Note noteAdd = new Note(creationDateNoteAdd, "Info note add");

    private PatientHistory patientHistoryDatabase = null;
    private PatientHistory patientHistoryNoDatabase = null;
    private PatientHistory patientHistoryDatabaseWithoutNotes = null;

    @BeforeEach
    public void beforeEach() {

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);

	patientHistoryNoDatabase = new PatientHistory();
	patientHistoryNoDatabase.getNotes().add(noteAdd);

	patientHistoryDatabaseWithoutNotes = new PatientHistory();
	patientHistoryDatabaseWithoutNotes.setId(1L);
    }

    @AfterEach
    public void afterEach() {
	patientHistoryService.deleteAllPatientHistory();
    }

    /*************************** PATIENT CRUD ************************************/

    @Test
    public void getPatientHistory() throws Exception {

	// Creation d'un patientHistory
	PatientHistory patientHistoryCreated = patientHistoryService.createPatientHistory(patientHistoryNoDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.get("/patientHistory/" + patientHistoryCreated.getId()))
		.andExpect(status().is(200)).andReturn();

	PatientHistory patientHistory = Util.getPatientHistoryFromMvcResult(mvcResult);
	assertThat(patientHistory.toString().equals(patientHistoryCreated.toString()));
    }

    @Test
    public void getPatientHistory_PatientHistoryNotFoundException() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.get("/patientHistory/2"))
		.andExpect(status().is(404));
    }

    @Test
    public void savePatientHistory() throws Exception {

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.post("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(201)).andReturn();

	PatientHistory patientHistorySaved = Util.getPatientHistoryFromMvcResult(mvcResult);
	assertNotNull(patientHistorySaved.getId());
	assertThat(patientHistorySaved.toString().equals(patientHistoryDatabase.toString()));
    }

    /*
     * 
     * @Test public void savePatientHistory_MethodArgumentNotValidException() throws
     * Exception {
     * 
     * mockMvc .perform( MockMvcRequestBuilders.post("/patientHistory")
     * .content(Util.mapper.writeValueAsString(patientHistoryToBeSavedNotValid))
     * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON))
     * .andExpect(status().is(400)).andReturn(); }
     * 
     * @Test public void savePatientHistory_PatientHistoryConflictException() throws
     * Exception {
     * 
     * mockMvc .perform( MockMvcRequestBuilders.post("/patientHistory")
     * .content(Util.mapper.writeValueAsString(patientHistoryDatabase))
     * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON))
     * .andExpect(status().is(409)).andReturn(); }
     * 
     * @Test public void updatePatientHistory() throws Exception {
     * 
     * // Creation d'un patientHistory PatientHistory patientHistoryToBeUpdated =
     * patientHistoryService.createPatientHistory(patientHistory);
     * patientHistoryToBeUpdated.setPrenom("Daniel");
     * 
     * MvcResult mvcResult = mockMvc
     * .perform(MockMvcRequestBuilders.put("/patientHistory")
     * .content(Util.mapper.writeValueAsString(patientHistoryToBeUpdated))
     * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON))
     * .andExpect(status().is(200)).andReturn();
     * 
     * PatientHistory patientHistoryUpdated =
     * Util.getPatientHistoryFromMvcResult(mvcResult);
     * assertNotNull(patientHistoryUpdated.getId());
     * assertThat(patientHistoryUpdated.toString().equals(patientHistoryToBeUpdated.
     * toString())); }
     * 
     * @Test public void updatePatientHistory_PatientHistoryNotFoundException()
     * throws Exception {
     * 
     * // Creation d'un patientHistory PatientHistory patientHistoryToBeUpdated =
     * patientHistoryService.createPatientHistory(patientHistory);
     * patientHistoryToBeUpdated.setPrenom("Daniel");
     * patientHistoryToBeUpdated.setId(2L);
     * 
     * mockMvc .perform(MockMvcRequestBuilders.put("/patientHistory")
     * .content(Util.mapper.writeValueAsString(patientHistoryToBeUpdated))
     * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON))
     * .andExpect(status().is(404)).andReturn(); }
     * 
     * @Test public void updatePatientHistory_MethodArgumentNotValidException()
     * throws Exception { // Creation d'un patientHistory PatientHistory
     * patientHistoryNotValidToBeUpdated =
     * patientHistoryService.createPatientHistory(patientHistory);
     * patientHistoryNotValidToBeUpdated.setGenre("MF");
     * 
     * mockMvc .perform( MockMvcRequestBuilders.put("/patientHistory")
     * .content(Util.mapper.writeValueAsString(patientHistoryNotValidToBeUpdated))
     * .contentType(MediaType.APPLICATION_JSON) .accept(MediaType.APPLICATION_JSON))
     * .andExpect(status().is(400)).andReturn(); }
     * 
     * @Test public void deletePatientHistory() throws Exception { // Creation d'un
     * patientHistory patientHistoryService.createPatientHistory(patientHistory);
     * 
     * mockMvc .perform(MockMvcRequestBuilders.delete("/patientHistory/1"))
     * .andExpect(status().is(200)).andReturn(); }
     * 
     * @Test public void deletePatientHistory_PatientHistoryNotFoundException()
     * throws Exception {
     * 
     * mockMvc .perform(MockMvcRequestBuilders.delete("/patientHistory/1"))
     * .andExpect(status().is(404)); }
     */
}