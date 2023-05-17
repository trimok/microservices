package com.sante.patienthistory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.patienthistory.controller.PatientHistoryController;
import com.sante.patienthistory.exception.PatientHistoryIdNotValidException;
import com.sante.patienthistory.exception.PatientHistoryNoContentException;
import com.sante.patienthistory.exception.PatientHistoryNotFoundException;
import com.sante.patienthistory.exception.PatientNoteNotValidException;
import com.sante.patienthistory.model.Note;
import com.sante.patienthistory.model.PatientHistory;
import com.sante.patienthistory.service.IPatientHistoryService;

@WebMvcTest({ PatientHistoryController.class })
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
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

    @AfterAll
    public void afterAll() {
    }

    /************ PATIENT HISTORY CONTROLLER **********/
    @Test
    public void getPatientHistory() throws Exception {
	when(patientHistoryService.getPatientHistory(any(Long.class))).thenReturn(patientHistoryDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.get("/patientHistory/1"))
		.andExpect(status().is(200)).andReturn();

	PatientHistory patientHistory = Util.getPatientHistoryFromMvcResult(mvcResult);
	assertThat(patientHistory.toString().equals(patientHistoryDatabase.toString()));

	verify(patientHistoryService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void getPatientHistory_PatientHistoryIdNotValidException() throws Exception {

	when(patientHistoryService.getPatientHistory(any(Long.class)))
		.thenThrow(new PatientHistoryIdNotValidException());

	mockMvc
		.perform(MockMvcRequestBuilders.get("/patientHistory/1"))
		.andExpect(status().is(400));

	verify(patientHistoryService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void getPatientHistory_PatientHistoryNotFoundException() throws Exception {

	when(patientHistoryService.getPatientHistory(any(Long.class)))
		.thenThrow(new PatientHistoryNotFoundException());

	mockMvc
		.perform(MockMvcRequestBuilders.get("/patientHistory/1"))
		.andExpect(status().is(404));

	verify(patientHistoryService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void createPatientHistory() throws Exception {

	when(patientHistoryService.createPatientHistory(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.post("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(201)).andReturn();

	PatientHistory patientHistorySaved = Util.getPatientHistoryFromMvcResult(mvcResult);
	assertNotNull(patientHistorySaved.getId());
	assertThat(patientHistorySaved.toString().equals(patientHistoryDatabase.toString()));

	verify(patientHistoryService, times(1)).createPatientHistory(any(PatientHistory.class));
    }

    @Test
    public void createPatientHistory_PatientHistoryNoContentException() throws Exception {

	when(patientHistoryService.createPatientHistory(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryNoContentException());

	mockMvc
		.perform(MockMvcRequestBuilders.post("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(204)).andReturn();

	verify(patientHistoryService, times(1)).createPatientHistory(any(PatientHistory.class));
    }

    @Test
    public void createPatientHistory_PatientHistoryIdNotValidException() throws Exception {

	when(patientHistoryService.createPatientHistory(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryNoContentException());

	mockMvc
		.perform(MockMvcRequestBuilders.post("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(204)).andReturn();

	verify(patientHistoryService, times(1)).createPatientHistory(any(PatientHistory.class));
    }

    @Test
    public void updatePatientHistory() throws Exception {

	when(patientHistoryService.updatePatientHistory(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.put("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(200)).andReturn();

	PatientHistory patientHistoryUpdated = Util.getPatientHistoryFromMvcResult(mvcResult);
	assertNotNull(patientHistoryUpdated.getId());
	assertThat(patientHistoryUpdated.toString().equals(patientHistoryDatabase.toString()));

	verify(patientHistoryService, times(1)).updatePatientHistory(any(PatientHistory.class));
    }

    @Test
    public void updatePatientHistory_PatientHistoryNoContentException() throws Exception {

	when(patientHistoryService.updatePatientHistory(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryNoContentException());

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(204)).andReturn();

	verify(patientHistoryService, times(1)).updatePatientHistory(any(PatientHistory.class));
    }

    @Test
    public void updatePatientHistory_PatientHistoryNotFoundException() throws Exception {

	when(patientHistoryService.updatePatientHistory(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryNotFoundException());

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404)).andReturn();

	verify(patientHistoryService, times(1)).updatePatientHistory(any(PatientHistory.class));
    }

    @Test
    public void updatePatientHistory_PatientHistoryIdNotValidException() throws Exception {

	when(patientHistoryService.updatePatientHistory(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryIdNotValidException());

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andReturn();

	verify(patientHistoryService, times(1)).updatePatientHistory(any(PatientHistory.class));
    }

    @Test
    public void createUpdatePatientHistory_PatientHistoryIdNotValidException() throws Exception {

	when(patientHistoryService.createUpdatePatientHistory(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryIdNotValidException());

	mockMvc
		.perform(MockMvcRequestBuilders.post("/patientHistory/add")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andReturn();

	verify(patientHistoryService, times(1)).createUpdatePatientHistory(any(PatientHistory.class));
    }

    @Test
    public void deletePatientHistory() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patientHistory/1"))
		.andExpect(status().is(200)).andReturn();

	verify(patientHistoryService, times(1)).deletePatientHistory(any(Long.class));
    }

    @Test
    public void deletePatientHistory_PatientHistoryNotFoundException() throws Exception {

	doThrow(new PatientHistoryNotFoundException()).when(patientHistoryService)
		.deletePatientHistory(any(Long.class));

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patientHistory/1"))
		.andExpect(status().is(404));

	verify(patientHistoryService, times(1)).deletePatientHistory(any(Long.class));
    }

    @Test
    public void updateNote() throws Exception {

	when(patientHistoryService.updateNote(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.put("/patientHistory/note")
			.content(Util.mapper.writeValueAsString(patientHistoryDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(200)).andReturn();

	PatientHistory patientHistoryUpdated = Util.getPatientHistoryFromMvcResult(mvcResult);
	assertNotNull(patientHistoryUpdated.getId());
	assertThat(patientHistoryUpdated.toString().equals(patientHistoryDatabase.toString()));

	verify(patientHistoryService, times(1)).updateNote(any(PatientHistory.class));
    }

    public void updateNote_PatientHistoryIdNotValidException() throws Exception {

	when(patientHistoryService.updateNote(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryIdNotValidException());

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andReturn();

	verify(patientHistoryService, times(1)).updateNote(any(PatientHistory.class));
    }

    public void updateNote_PatientNoteNotValidException() throws Exception {

	when(patientHistoryService.updateNote(any(PatientHistory.class)))
		.thenThrow(new PatientNoteNotValidException());

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andReturn();

	verify(patientHistoryService, times(1)).updateNote(any(PatientHistory.class));
    }

    public void updateNote_PatientHistoryNotFoundException() throws Exception {

	when(patientHistoryService.updateNote(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryNotFoundException());

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404)).andReturn();

	verify(patientHistoryService, times(1)).updateNote(any(PatientHistory.class));
    }

    @Test
    public void deleteNote() throws Exception {

	when(patientHistoryService.deleteNote(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.delete("/patientHistory/note")
			.content(Util.mapper.writeValueAsString(patientHistoryDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(200)).andReturn();

	PatientHistory patientHistoryUpdated = Util.getPatientHistoryFromMvcResult(mvcResult);
	assertNotNull(patientHistoryUpdated.getId());
	assertThat(patientHistoryUpdated.toString().equals(patientHistoryDatabase.toString()));

	verify(patientHistoryService, times(1)).deleteNote(any(PatientHistory.class));
    }

    public void deleteNote_PatientHistoryIdNotValidException() throws Exception {

	when(patientHistoryService.deleteNote(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryIdNotValidException());

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andReturn();

	verify(patientHistoryService, times(1)).deleteNote(any(PatientHistory.class));
    }

    public void deleteNote_PatientNoteNotValidException() throws Exception {

	when(patientHistoryService.deleteNote(any(PatientHistory.class)))
		.thenThrow(new PatientNoteNotValidException());

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andReturn();

	verify(patientHistoryService, times(1)).deleteNote(any(PatientHistory.class));
    }

    public void deleteNote_PatientHistoryNotFoundException() throws Exception {

	when(patientHistoryService.deleteNote(any(PatientHistory.class)))
		.thenThrow(new PatientHistoryNotFoundException());

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patientHistory")
			.content(Util.mapper.writeValueAsString(patientHistoryNoDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404)).andReturn();

	verify(patientHistoryService, times(1)).deleteNote(any(PatientHistory.class));
    }

}