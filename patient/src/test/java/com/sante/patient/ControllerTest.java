package com.sante.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.patient.controller.PatientController;
import com.sante.patient.exception.PatientConflictException;
import com.sante.patient.exception.PatientNoContentException;
import com.sante.patient.exception.PatientNotFoundException;
import com.sante.patient.model.Patient;
import com.sante.patient.service.IPatientService;

@WebMvcTest({ PatientController.class })
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class ControllerTest {

    private Timestamp now = Timestamp.from(Instant.now());

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    IPatientService patientService;

    private Patient patientDatabase;
    private Patient patient;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    @BeforeEach
    public void beforeEach() {
	patient = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");
    }

    @AfterAll
    public void afterAll() {
    }

    /************ PATIENT CONTROLLER **********/

    @Test
    public void getPatients() throws Exception {

	List<Patient> patients = new ArrayList<>();
	patients.add(patientDatabase);

	when(patientService.getPatients()).thenReturn(patients);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.get("/patient"))
		.andExpect(status().is(200)).andReturn();

	List<Patient> patientsReturn = Util.getListPatientFromMvcResult(mvcResult);
	assertNotNull(patientsReturn);
	assertThat(patientsReturn.size() == 1);
	assertThat(patientsReturn.get(0).toString().equals(patientDatabase.toString()));

	verify(patientService, times(1)).getPatients();
    }

    @Test
    public void getPatient() throws Exception {

	List<Patient> patients = new ArrayList<>();
	patients.add(patientDatabase);

	when(patientService.getPatient(any(Long.class))).thenReturn(patientDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.get("/patient/1"))
		.andExpect(status().is(200)).andReturn();

	Patient patient = Util.getPatientFromMvcResult(mvcResult);
	assertThat(patient.toString().equals(patientDatabase.toString()));

	verify(patientService, times(1)).getPatient(any(Long.class));
    }

    @Test
    public void getPatient_PatientNotFoundException() throws Exception {

	List<Patient> patients = new ArrayList<>();
	patients.add(patientDatabase);

	when(patientService.getPatient(any(Long.class))).thenThrow(new PatientNotFoundException());

	mockMvc
		.perform(MockMvcRequestBuilders.get("/patient/1"))
		.andExpect(status().is(404));

	verify(patientService, times(1)).getPatient(any(Long.class));
    }

    @Test
    public void savePatient() throws Exception {

	when(patientService.createPatient(any(Patient.class))).thenReturn(patientDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.post("/patient").content(Util.mapper.writeValueAsString(patient))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(201)).andReturn();

	Patient patientSaved = Util.getPatientFromMvcResult(mvcResult);
	assertNotNull(patientSaved.getId());
	assertThat(patientSaved.toString().equals(patientDatabase.toString()));

	verify(patientService, times(1)).createPatient(any(Patient.class));
    }

    @Test
    public void savePatient_PatientNoContentException() throws Exception {

	when(patientService.createPatient(any(Patient.class))).thenThrow(new PatientNoContentException());

	mockMvc
		.perform(MockMvcRequestBuilders.post("/patient").content(Util.mapper.writeValueAsString(patient))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(204)).andReturn();

	verify(patientService, times(1)).createPatient(any(Patient.class));
    }

    @Test
    public void savePatient_PatientConflictException() throws Exception {

	when(patientService.createPatient(any(Patient.class))).thenThrow(new PatientConflictException());

	mockMvc
		.perform(
			MockMvcRequestBuilders.post("/patient").content(Util.mapper.writeValueAsString(patientDatabase))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(409)).andReturn();

	verify(patientService, times(1)).createPatient(any(Patient.class));
    }

    @Test
    public void updatePatient() throws Exception {

	when(patientService.updatePatient(any(Patient.class))).thenReturn(patientDatabase);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.put("/patient").content(Util.mapper.writeValueAsString(patientDatabase))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(200)).andReturn();

	Patient patientUpdated = Util.getPatientFromMvcResult(mvcResult);
	assertNotNull(patientUpdated.getId());
	assertThat(patientUpdated.toString().equals(patientDatabase.toString()));

	verify(patientService, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    public void updatePatient_PatientNoContentException() throws Exception {

	when(patientService.updatePatient(any(Patient.class))).thenThrow(new PatientNoContentException());

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patient").content(Util.mapper.writeValueAsString(patient))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(204)).andReturn();

	verify(patientService, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    public void updatePatient_PatientNotFoundException() throws Exception {

	when(patientService.updatePatient(any(Patient.class))).thenThrow(new PatientNotFoundException());

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patient").content(Util.mapper.writeValueAsString(patient))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404)).andReturn();

	verify(patientService, times(1)).updatePatient(any(Patient.class));
    }

    @Test
    public void deletePatient() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patient/1"))
		.andExpect(status().is(200)).andReturn();

	verify(patientService, times(1)).deletePatient(any(Long.class));
    }

    @Test
    public void deletePatient_PatientNotFoundException() throws Exception {

	doThrow(new PatientNotFoundException()).when(patientService).deletePatient(any(Long.class));

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patient/1"))
		.andExpect(status().is(404));

	verify(patientService, times(1)).deletePatient(any(Long.class));
    }

}