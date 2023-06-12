package com.sante.clientui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.clientui.controller.ClientuiController;
import com.sante.clientui.dao.PatientDao;
import com.sante.clientui.model.Note;
import com.sante.clientui.model.Patient;
import com.sante.clientui.model.PatientHistory;
import com.sante.clientui.model.Risque;
import com.sante.clientui.service.GatewayService;

@WebMvcTest({ ClientuiController.class })
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class ControllerTest {

    private Timestamp now = Timestamp.from(Instant.now());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GatewayService gatewayService;

    private Patient patientDatabase;
    private Patient patientDatabaseNotValid;
    private Patient patient;
    private Patient patientNotValid;

    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private LocalDateTime creationDate = LocalDateTime.parse("04/12/1957 11:10:07", dateTimeFormatter);
    private LocalDateTime creationDateNoteAdd = LocalDateTime.parse("04/12/1997 11:10:07", dateTimeFormatter);

    private Note note = new Note(creationDate, "Info");
    private Note noteAdd = new Note(creationDateNoteAdd, "Info note add");

    private PatientHistory patientHistoryDatabase = null;
    private PatientHistory patientHistoryNoDatabase = null;
    private PatientHistory patientHistoryDatabaseWithoutNotes = null;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    private PatientDao patientDaoDatabase;

    @BeforeEach
    public void beforeEach() {
	patient = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientNotValid = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08");

	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientDatabaseNotValid = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08");

	patientHistoryDatabase = new PatientHistory();
	patientHistoryDatabase.setId(1L);
	patientHistoryDatabase.getNotes().add(note);

	patientHistoryNoDatabase = new PatientHistory();
	patientHistoryNoDatabase.getNotes().add(noteAdd);

	patientHistoryDatabaseWithoutNotes = new PatientHistory();
	patientHistoryDatabaseWithoutNotes.setId(1L);

	patientDaoDatabase = new PatientDao(patientDatabase, patientHistoryDatabase);

    }

    @AfterAll
    public void afterAll() {
    }

    @Test
    public void getPatients() throws Exception {

	List<Patient> patients = new ArrayList<>();
	patients.add(patientDatabase);

	when(gatewayService.getPatients()).thenReturn(patients);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/"))

		.andExpect(status().is(200))
		.andExpect(model().attributeDoesNotExist("error_get_list_patient"))
		.andExpect(model().attributeDoesNotExist("error_get_list_patient_access_forbidden"))
		.andExpect(view().name("home"));

	verify(gatewayService, times(1)).getPatients();
    }

    @Test
    public void getPatients_access_forbidden() throws Exception {

	List<Patient> patients = new ArrayList<>();
	patients.add(patientDatabase);

	when(gatewayService.getPatients()).thenThrow(new RuntimeException("403"));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(status().is(200))
		.andExpect(model().attributeDoesNotExist("error_get_list_patient"))
		.andExpect(model().attributeExists("error_get_list_patient_access_forbidden"))
		.andExpect(view().name("home"));

	verify(gatewayService, times(1)).getPatients();
    }

    @Test
    public void getPatients_exception() throws Exception {

	List<Patient> patients = new ArrayList<>();
	patients.add(patientDatabase);

	when(gatewayService.getPatients()).thenThrow(new RuntimeException(""));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/"))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("error_get_list_patient"))
		.andExpect(model().attributeDoesNotExist("error_get_list_patient_access_forbidden"))
		.andExpect(view().name("home"));

	verify(gatewayService, times(1)).getPatients();
    }

    @Test
    public void showNewPatientForm() throws Exception {
	mockMvc
		.perform(MockMvcRequestBuilders.get("/add"))
		.andExpect(status().is(200))
		.andExpect(view().name("add"));

    }

    @Test
    public void savePatient() throws Exception {

	when(gatewayService.createPatient(any(Patient.class))).thenReturn(patient);

	mockMvc
		.perform(MockMvcRequestBuilders.post("/save")
			.flashAttr("patient", patient))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_add_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).createPatient(any(Patient.class));
    }

    @Test
    public void savePatientNotValid() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.post("/save")
			.flashAttr("patient", patientNotValid))
		.andExpect(status().is(200))
		.andExpect(model().attributeDoesNotExist("error_add_patient"))
		.andExpect(view().name("add"));
    }

    @Test
    public void savePatient_exception() throws Exception {

	when(gatewayService.createPatient(any(Patient.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.post("/save")
			.flashAttr("patient", patient))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_add_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).createPatient(any(Patient.class));
    }

    @Test
    public void updatePatient() throws Exception {

	when(gatewayService.updatePatient(any(Patient.class))).thenReturn(patientDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.post("/update")
			.flashAttr("patient", patientDatabase))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_update_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    public void updatePatientNotValid() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.post("/update")
			.flashAttr("patient", patientDatabaseNotValid))
		.andExpect(status().is(200))
		.andExpect(model().attributeDoesNotExist("error_update_patient"))
		.andExpect(view().name("update"));
    }

    @Test
    public void updatePatient_exception() throws Exception {

	when(gatewayService.updatePatient(any(Patient.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.post("/update")
			.flashAttr("patient", patientDatabase))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_update_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    public void showFormForUpdate() throws Exception {
	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/update/1"))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attributeDoesNotExist("error_get_patient"))
		.andExpect(view().name("update"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
    }

    @Test
    public void showFormForUpdate_exception() throws Exception {
	when(gatewayService.getPatient(any(Long.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.get("/update/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));

    }

    @Test
    public void deletePatient() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.get("/delete/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_delete_patient"))
		.andExpect(model().attributeDoesNotExist("error_delete_patient_history"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).deletePatient(any(Long.class));
	verify(gatewayService, times(1)).deletePatientHistory(any(Long.class));
    }

    @Test
    public void deletePatient_exception_patient() throws Exception {

	doThrow(new RuntimeException()).when(gatewayService).deletePatient(any(Long.class));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/delete/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_delete_patient"))
		.andExpect(model().attributeDoesNotExist("error_delete_patient_history"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).deletePatient(any(Long.class));
	verify(gatewayService, times(1)).deletePatientHistory(any(Long.class));
    }

    @Test
    public void deletePatient_exception_patientHistory() throws Exception {

	doThrow(new RuntimeException()).when(gatewayService).deletePatientHistory(any(Long.class));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/delete/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_delete_patient"))
		.andExpect(model().attributeExists("error_delete_patient_history"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).deletePatient(any(Long.class));
	verify(gatewayService, times(1)).deletePatientHistory(any(Long.class));
    }

    @Test
    public void deletePatient_exception_all() throws Exception {

	doThrow(new RuntimeException()).when(gatewayService).deletePatient(any(Long.class));
	doThrow(new RuntimeException()).when(gatewayService).deletePatientHistory(any(Long.class));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/delete/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_delete_patient"))
		.andExpect(model().attributeExists("error_delete_patient_history"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).deletePatient(any(Long.class));
	verify(gatewayService, times(1)).deletePatientHistory(any(Long.class));
    }

    @Test
    public void showNewPatientHistoryForm() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/add/1"))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attributeDoesNotExist("error_get_patient"))
		.andExpect(view().name("note_add"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
    }

    @Test
    public void showNewPatientHistoryForm_exception() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/add/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
    }

    @Test
    public void viewNotesHomePage() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenReturn(patientHistoryDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/notes/1"))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attributeExists("patientHistory"))
		.andExpect(view().name("note_home"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void viewNotesHomePage_access_forbidden() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenThrow(new RuntimeException("403"));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/notes/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_patient_history_access_forbidden"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void viewNotesHomePage_exceptionPatientHistory() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenThrow(new RuntimeException(""));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/notes/1"))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attributeExists("patientHistory"))
		.andExpect(view().name("note_home"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void viewNotesHomePage_exceptionPatient() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenThrow(new RuntimeException(""));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/notes/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
    }

    @Test
    public void savePatientHistory() throws Exception {

	when(gatewayService.createUpdatePatientHistory(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);
	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.post("/note/save")
			.flashAttr("patientHistory", patientHistoryDatabase).flashAttr("note", note))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patient"))
		.andExpect(model().attributeExists("patientHistory"))
		.andExpect(view().name("note_home"));

	verify(gatewayService, times(1)).createUpdatePatientHistory(any(PatientHistory.class));
	verify(gatewayService, times(1)).getPatient(any(Long.class));

    }

    @Test
    public void savePatientHistory_notValid() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.post("/note/save")
			.flashAttr("patientHistory", patientHistoryNoDatabase).flashAttr("note", note))
		.andExpect(status().is(400));
    }

    @Test
    public void savePatientHistory_exceptions() throws Exception {

	when(gatewayService.createUpdatePatientHistory(any(PatientHistory.class))).thenThrow(new RuntimeException());
	when(gatewayService.getPatient(any(Long.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.post("/note/save")
			.flashAttr("patientHistory", patientHistoryDatabase).flashAttr("note", note))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_add_patient_history"))
		.andExpect(model().attributeExists("error_get_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).createUpdatePatientHistory(any(PatientHistory.class));
	verify(gatewayService, times(1)).getPatient(any(Long.class));

    }

    @Test
    public void savePatientHistory_exceptionGetPatient() throws Exception {

	when(gatewayService.createUpdatePatientHistory(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);
	when(gatewayService.getPatient(any(Long.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.post("/note/save")
			.flashAttr("patientHistory", patientHistoryDatabase).flashAttr("note", note))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).createUpdatePatientHistory(any(PatientHistory.class));
	verify(gatewayService, times(1)).getPatient(any(Long.class));

    }

    @Test
    public void deleteNote() throws Exception {
	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/delete/1/" + creationDate.toString()))
		.andExpect(status().is(302))
		.andExpect(view().name("redirect:/homenotes/1"));

	verify(gatewayService, times(1)).deleteNote(any(PatientHistory.class));
    }

    @Test
    public void deleteNote_exception() throws Exception {

	doThrow(new RuntimeException()).when(gatewayService).deleteNote(any(PatientHistory.class));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/delete/1/" + creationDate.toString()))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_delete_note"))
		.andExpect(view().name("redirect:/homenotes/1"));

	verify(gatewayService, times(1)).deleteNote(any(PatientHistory.class));
    }

    @Test
    public void showNoteFormForUpdate() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenReturn(patientHistoryDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/update/1/" + creationDate.toString()))
		.andExpect(status().is(200))
		.andExpect(view().name("note_update"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void showNoteFormForUpdate_exceptionPatient() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/update/1/" + creationDate.toString()))
		.andExpect(model().attributeExists("error_get_patient"))
		.andExpect(status().is(302))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
    }

    @Test
    public void showNoteFormForUpdate_exceptionPatientHistory() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/update/1/" + creationDate.toString()))
		.andExpect(model().attributeExists("error_get_patient_history"))
		.andExpect(status().is(302))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void showNoteFormForUpdate_exceptionNote() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenReturn(patientHistoryDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/note/update/1/" + creationDateNoteAdd.toString()))
		.andExpect(model().attributeExists("error_get_note"))
		.andExpect(status().is(302))
		.andExpect(view().name("redirect:/homenotes/1"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void updateNote() throws Exception {

	when(gatewayService.updateNote(any(PatientHistory.class))).thenReturn(patientHistoryDatabase);

	mockMvc
		.perform(MockMvcRequestBuilders.post("/note/update")
			.flashAttr("patientHistory", patientHistoryDatabase).flashAttr("note", note))
		.andExpect(status().is(302))
		.andExpect(model().attributeDoesNotExist("error_update_note"))
		.andExpect(view().name("redirect:/homenotes/1"));

	verify(gatewayService, times(1)).updateNote(any(PatientHistory.class));
    }

    @Test
    public void updateNote_exception() throws Exception {

	doThrow(new RuntimeException()).when(gatewayService).updateNote(any(PatientHistory.class));

	mockMvc
		.perform(MockMvcRequestBuilders.post("/note/update")
			.flashAttr("patientHistory", patientHistoryDatabase).flashAttr("note", note))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_update_note"))
		.andExpect(view().name("redirect:/homenotes/1"));

	verify(gatewayService, times(1)).updateNote(any(PatientHistory.class));
    }

    @Test
    public void showRisqueForm() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenReturn(patientHistoryDatabase);
	when(gatewayService.getRisque(any(PatientDao.class))).thenReturn(Risque.AUCUN_RISQUE);

	mockMvc
		.perform(MockMvcRequestBuilders.get("/risque/1"))
		.andExpect(status().is(200))
		.andExpect(model().attributeExists("patientDao"))
		.andExpect(model().attributeExists("risque"))
		.andExpect(view().name("risque"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
	verify(gatewayService, times(1)).getRisque(any(PatientDao.class));
    }

    @Test
    public void showRisqueForm_exceptionPatient() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenThrow(new RuntimeException());

	mockMvc
		.perform(MockMvcRequestBuilders.get("/risque/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_patient"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
    }

    @Test
    public void showRisqueForm_patientHistory_forbidden_access() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenThrow(new RuntimeException("403"));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/risque/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_patient_history_access_forbidden"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
    }

    @Test
    public void showRisqueForm_exceptionRisque_access_forbidden() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenReturn(patientHistoryDatabase);
	when(gatewayService.getRisque(any(PatientDao.class))).thenThrow(new RuntimeException("403"));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/risque/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_risque_access_forbidden"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
	verify(gatewayService, times(1)).getRisque(any(PatientDao.class));
    }

    @Test
    public void showRisqueForm_exceptionRisque_standard() throws Exception {

	when(gatewayService.getPatient(any(Long.class))).thenReturn(patientDatabase);
	when(gatewayService.getPatientHistory(any(Long.class))).thenReturn(patientHistoryDatabase);
	when(gatewayService.getRisque(any(PatientDao.class))).thenThrow(new RuntimeException(""));

	mockMvc
		.perform(MockMvcRequestBuilders.get("/risque/1"))
		.andExpect(status().is(302))
		.andExpect(model().attributeExists("error_get_risque"))
		.andExpect(view().name("redirect:/"));

	verify(gatewayService, times(1)).getPatient(any(Long.class));
	verify(gatewayService, times(1)).getPatientHistory(any(Long.class));
	verify(gatewayService, times(1)).getRisque(any(PatientDao.class));
    }

}