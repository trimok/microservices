package com.sante.patient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.sante.patient.model.Patient;
import com.sante.patient.service.IPatientService;

@ContextConfiguration
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.MethodName.class)
public class IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    IPatientService patientService;

    private final static String ROLE_PATIENT_USER = "ROLE_PATIENT_USER";

    private Map<String, Object> sessionAttrs = null;

    private Patient patientDatabase;
    private Patient patient;
    private Patient patientToBeSavedNotValid;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private LocalDate dateNaissance = LocalDate.parse("04/12/1957", formatter);

    @BeforeEach
    public void beforeEach() {

	patient = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientDatabase = new Patient(1L, "Tristan", "Mokobodzki", dateNaissance, "M", "1 rue du Louvre, PARIS",
		"06 01 02 08 09");

	patientToBeSavedNotValid = new Patient(null, "Tristan", "Mokobodzki", dateNaissance, "MF",
		"1 rue du Louvre, PARIS",
		"06 01 02 08 09");
    }

    @AfterEach
    public void afterEach() {
	patientService.deleteAllPatient();
    }

    /*************************** PATIENT CRUD ************************************/

    @Test
    public void getPatients() throws Exception {

	// Creation d'un patient
	Patient patientCreated = patientService.createPatient(patient);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.get("/patient")
			.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER))))
		.andExpect(status().is(200)).andReturn();

	List<Patient> patientsReturn = Util.getListPatientFromMvcResult(mvcResult);
	assertNotNull(patientsReturn);
	assertThat(patientsReturn.size() == 1);
	assertThat(patientsReturn.get(0).toString().equals(patientCreated.toString()));
    }

    @Test
    public void getPatient() throws Exception {

	// Creation d'un patient
	Patient patientCreated = patientService.createPatient(patient);

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.get("/patient/" + patientCreated.getId())
			.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER))))
		.andExpect(status().is(200)).andReturn();

	Patient patient = Util.getPatientFromMvcResult(mvcResult);
	assertThat(patient.toString().equals(patientCreated.toString()));
    }

    @Test
    public void getPatient_PatientNotFoundException() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.get("/patient/2")
			.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER))))
		.andExpect(status().is(404));
    }

    @Test
    public void savePatient() throws Exception {

	MvcResult mvcResult = mockMvc
		.perform(MockMvcRequestBuilders.post("/patient")
			.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER)))
			.content(Util.mapper.writeValueAsString(patient))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(201)).andReturn();

	Patient patientSaved = Util.getPatientFromMvcResult(mvcResult);
	assertNotNull(patientSaved.getId());
	assertThat(patientSaved.toString().equals(patientDatabase.toString()));
    }

    @Test
    public void savePatient_MethodArgumentNotValidException() throws Exception {

	mockMvc
		.perform(
			MockMvcRequestBuilders.post("/patient")
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER)))
				.content(Util.mapper.writeValueAsString(patientToBeSavedNotValid))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andReturn();
    }

    @Test
    public void savePatient_PatientConflictException() throws Exception {

	mockMvc
		.perform(
			MockMvcRequestBuilders.post("/patient")
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER)))
				.content(Util.mapper.writeValueAsString(patientDatabase))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(409)).andReturn();
    }

    @Test
    public void updatePatient() throws Exception {

	// Creation d'un patient
	Patient patientToBeUpdated = patientService.createPatient(patient);
	patientToBeUpdated.setPrenom("Daniel");

	MvcResult mvcResult = mockMvc
		.perform(
			MockMvcRequestBuilders.put("/patient")
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER)))
				.content(Util.mapper.writeValueAsString(patientToBeUpdated))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(200)).andReturn();

	Patient patientUpdated = Util.getPatientFromMvcResult(mvcResult);
	assertNotNull(patientUpdated.getId());
	assertThat(patientUpdated.toString().equals(patientToBeUpdated.toString()));
    }

    @Test
    public void updatePatient_PatientNotFoundException() throws Exception {

	// Creation d'un patient
	Patient patientToBeUpdated = patientService.createPatient(patient);
	patientToBeUpdated.setPrenom("Daniel");
	patientToBeUpdated.setId(2L);

	mockMvc
		.perform(MockMvcRequestBuilders.put("/patient")
			.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER)))
			.content(Util.mapper.writeValueAsString(patientToBeUpdated))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(404)).andReturn();
    }

    @Test
    public void updatePatient_MethodArgumentNotValidException() throws Exception {
	// Creation d'un patient
	Patient patientNotValidToBeUpdated = patientService.createPatient(patient);
	patientNotValidToBeUpdated.setGenre("MF");

	mockMvc
		.perform(
			MockMvcRequestBuilders.put("/patient")
				.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER)))
				.content(Util.mapper.writeValueAsString(patientNotValidToBeUpdated))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().is(400)).andReturn();
    }

    @Test
    public void deletePatient() throws Exception {
	// Creation d'un patient
	patientService.createPatient(patient);

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patient/1")
			.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER))))
		.andExpect(status().is(200)).andReturn();
    }

    @Test
    public void deletePatient_PatientNotFoundException() throws Exception {

	mockMvc
		.perform(MockMvcRequestBuilders.delete("/patient/1")
			.with(jwt().authorities(new SimpleGrantedAuthority(ROLE_PATIENT_USER))))
		.andExpect(status().is(404));
    }

}