package com.sante.expert;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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

import com.sante.expert.controller.ExpertController;
import com.sante.expert.dao.Note;
import com.sante.expert.dao.Patient;
import com.sante.expert.dao.PatientDao;
import com.sante.expert.dao.PatientHistory;
import com.sante.expert.exception.DeclencheurNotFoundException;
import com.sante.expert.exception.RisqueNotFoundException;
import com.sante.expert.model.Risque;
import com.sante.expert.service.IDeclencheurService;
import com.sante.expert.service.IRegleService;

@WebMvcTest({ ExpertController.class })
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IRegleService regleService;

    @MockBean
    IDeclencheurService declencheurService;

    private List<String> keywords = Util.getKeywords();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    private DateTimeFormatter formatter_2 = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", formatter_2);

    private Note note = new Note(creationDate, "Info");

    private Patient patientDatabase;
    private PatientHistory patientHistoryDatabase;

    private PatientDao patientDao;

    @BeforeEach
    public void beforeEach() {

	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09 ");

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);

	patientDao = new PatientDao(patientDatabase, patientHistoryDatabase);
    }

    @AfterAll
    public void afterAll() {
    }

    /************ EXPERT CONTROLLER **********/

    @Test
    public void findRisque() throws Exception {

	when(regleService.findRisque(any(PatientDao.class), anyList())).thenReturn(Risque.APPARITION_PRECOCE);
	when(declencheurService.findKeywords()).thenReturn(keywords);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.post("/expert").content(Util.mapper.writeValueAsString(patientDao))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(200)).andReturn();

	Risque risque = Util.getRisqueFromMvcResult(mvcResult);
	assertNotNull(risque);
	assertThat(risque).isEqualTo(Risque.APPARITION_PRECOCE);
	verify(regleService, times(1)).findRisque(any(PatientDao.class), anyList());
	verify(declencheurService, times(1)).findKeywords();
    }

    @Test
    public void findRisque_RisqueNotFoundException() throws Exception {

	when(declencheurService.findKeywords()).thenReturn(keywords);
	when(regleService.findRisque(any(PatientDao.class), anyList()))
		.thenThrow(new RisqueNotFoundException("RISQUE_NOT_FOUND"));

	mockMvc
		.perform(MockMvcRequestBuilders.post("/expert").content(Util.mapper.writeValueAsString(patientDao))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404));

	verify(regleService, times(1)).findRisque(any(PatientDao.class), anyList());
	verify(declencheurService, times(1)).findKeywords();
    }

    @Test
    public void findRisque_DeclencheurNotFoundException_1() throws Exception {

	when(declencheurService.findKeywords()).thenThrow(new DeclencheurNotFoundException("DECLENCHEUR_NOT_FOUND"));
	when(regleService.findRisque(any(PatientDao.class), anyList())).thenReturn(Risque.APPARITION_PRECOCE);

	mockMvc
		.perform(MockMvcRequestBuilders.post("/expert").content(Util.mapper.writeValueAsString(patientDao))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404));

	verify(regleService, times(0)).findRisque(any(PatientDao.class), anyList());
	verify(declencheurService, times(1)).findKeywords();
    }
}